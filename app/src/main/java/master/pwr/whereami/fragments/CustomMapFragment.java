package master.pwr.whereami.fragments;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import master.pwr.whereami.R;
import master.pwr.whereami.models.MapUpdate;

public class CustomMapFragment extends Fragment
{
    public static final String TAG = CustomMapFragment.class.getName();
    private static final int DEFAULT_ZOOM = 5;

    private Gson json = new Gson();
    private Bundle backup;
    private View.OnClickListener callback;
    private LatLng DEFAULT_POSITION;
    private GoogleMap map;
    private Marker deviceMarker;
    private Circle accuracyMarker;
    private TextView statusView;
    private Button locateButton;

    public CustomMapFragment()
    {
        // Required empty public constructor
    }

    public static CustomMapFragment newInstance()
    {
        return new CustomMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_custom_map, container, false);

        String[] positionData;
        if (getArguments() != null)
        {
            positionData = getArguments().getString("data").split(":");
        }
        else
        {
            positionData = new String[]{"51.5009489", "18.006975"};
        }

        DEFAULT_POSITION = new LatLng(
                Float.parseFloat(positionData[0]),
                Float.parseFloat(positionData[1]));

        locateButton = (Button) v.findViewById(R.id.button);
        statusView = (TextView) v.findViewById(R.id.status);

        locateButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Mapa nie jest jeszcze gotowa.", Toast.LENGTH_SHORT).show();
            }
        });

        MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                map = googleMap;
                if (backup == null)
                {
                    setupMap(DEFAULT_POSITION, 10000.0f, DEFAULT_ZOOM);
                }
                else
                {
                    restoreView();
                }

                locateButton.setOnClickListener(callback);
            }
        });

        return v;
    }

    private void setupMap(LatLng position, double radius, int zoom)
    {
        deviceMarker = map.addMarker(new MarkerOptions().position(position));
        accuracyMarker = map.addCircle(new CircleOptions()
                .center(position)
                .radius(radius)
                .strokeWidth(5.0f)
                .fillColor(0x302072FF)
                .strokeColor(0x2072FF));

        moveCameraToPosition(map, position, zoom);
    }

    public void setOnLocateButtonListener(View.OnClickListener listener)
    {
        callback = listener;
    }

    public void updateMap(MapUpdate update)
    {
        int zoom = calculateZoom(update.getAccuracy());
        setStatusText(update);
        //animateMarker(deviceMarker, accuracyMarker, update.getPosition(), update.getAccuracy());
        deviceMarker.setPosition(update.getPosition());
        accuracyMarker.setCenter(update.getPosition());
        accuracyMarker.setRadius(update.getAccuracy());

        moveCameraToPosition(map, update.getPosition(), zoom);
    }

    private void setStatusText(MapUpdate update)
    {
        float accuracy = update.getAccuracy();
        float accuracyInKiloMeters = accuracy / 1000;
        boolean isGreaterThenKilometer = accuracyInKiloMeters < 1.00000f;

        statusView.setText(String.format("Dokładność: %.0f [%s]",
                isGreaterThenKilometer ? accuracy : accuracyInKiloMeters,
                isGreaterThenKilometer ? "m" : "km"));
    }

    private void moveCameraToPosition(GoogleMap googleMap, LatLng position, int zoom)
    {
        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(position)
                .zoom(zoom)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void animateMarker(final Marker marker,
                               final Circle circle,
                               final LatLng toPosition,
                               final float accuracy)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection projection = map.getProjection();
        Point startPoint = projection.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = projection.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        circle.setRadius(accuracy);

        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                LatLng pos = new LatLng(lat, lng);
                marker.setPosition(pos);
                circle.setCenter(pos);

                if (t < 1.0)
                {
                    // Post again 10ms later.
                    handler.postDelayed(this, 10);
                }
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();
        saveState();
    }

    private void saveState()
    {
        backup = new Bundle();
        backup.putString("locateBtnState", locateButton.getText().toString());
        backup.putString("status", statusView.getText().toString());
        if (deviceMarker != null)
        {
            backup.putString("pos", json.toJson(deviceMarker.getPosition()));
            backup.putDouble("acc", accuracyMarker.getRadius());
        }
    }

    private void restoreView()
    {
        if (backup == null) return;

        locateButton.setText(backup.getString("locateBtnState"));
        statusView.setText(backup.getString("status"));
        if (backup.getString("pos") != null)
        {
            LatLng pos = json.fromJson(backup.getString("pos"), LatLng.class);
            double acc = backup.getDouble("acc");
            setupMap(pos, acc, calculateZoom(acc));
        }
    }

    private int calculateZoom(double accuracy)
    {
        int zoom = 0;
        if (accuracy < 1128.497220)
        {
            zoom = 20;
        }
        else if (accuracy < 2256.994440)
        {
            zoom = 19;
        }
        else if (accuracy < 4513.988880)
        {
            zoom = 18;
        }
        else if (accuracy < 9027.977761)
        {
            zoom = 17;
        }
        else if (accuracy < 18055.955520)
        {
            zoom = 16;
        }
        else if (accuracy < 36111.911040)
        {
            zoom = 15;
        }
        else if (accuracy < 72223.822090)
        {
            zoom = 14;
        }
        else if (accuracy < 144447.644200)
        {
            zoom = 13;
        }
        else if (accuracy < 288895.288400)
        {
            zoom = 12;
        }
        else if (accuracy < 577790.576700)
        {
            zoom = 11;
        }
        else if (accuracy < 1155581.153000)
        {
            zoom = 10;
        }
        else if (accuracy < 2311162.307000)
        {
            zoom = 9;
        }
        else if (accuracy < 4622324.614000)
        {
            zoom = 8;
        }
        else if (accuracy < 9244649.227000)
        {
            zoom = 7;
        }
        else if (accuracy < 18489298.450000)
        {
            zoom = 6;
        }
        else if (accuracy < 36978596.910000)
        {
            zoom = 5;
        }
        else if (accuracy < 73957193.820000)
        {
            zoom = 4;
        }
        else if (accuracy < 147914387.600000)
        {
            zoom = 3;
        }
        else if (accuracy < 295828775.300000)
        {
            zoom = 2;
        }
        else if (accuracy < 591657550.500000) zoom = 1;


        // W ramach tej aplikacji zoom większy niż 18 jest zbyt duży.
        return zoom > 18 ? 18 : zoom;
    }
}
