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

import master.pwr.whereami.R;
import master.pwr.whereami.models.MapUpdate;

public class CustomMapFragment extends Fragment
{
    public static final String TAG = CustomMapFragment.class.getName();
    private static final int DEFAULT_ZOOM = 5;
    private static final int MAX_ZOOM = 10;

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

        locateButton.setOnClickListener(callback);

        if (map == null)
        {
            MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    map = googleMap;
                    deviceMarker = map.addMarker(new MarkerOptions().position(DEFAULT_POSITION));
                    accuracyMarker = map.addCircle(new CircleOptions()
                                    .center(DEFAULT_POSITION)
                                    .radius(10000.0f)
                                    .strokeWidth(5.0f)
                                    .fillColor(0x302072FF)
                                    .strokeColor(0x2072FF)
                    );
                }
            });
        }

        return v;
    }

    public void setOnLocateButtonListener(View.OnClickListener listener)
    {
        callback = listener;
    }

    public void updateMap(MapUpdate update)
    {
        int zoom = (int) update.getAccuracy();
        if (zoom < 10000)
        {
            zoom = MAX_ZOOM;
        }
        else
        {
            zoom = DEFAULT_ZOOM;
        }

        setStatusText(update);
        animateMarker(deviceMarker, accuracyMarker, update.getPosition(), update.getAccuracy(), false);
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

    public void animateMarker(final Marker marker,
                              final Circle circle,
                              final LatLng toPosition,
                              final float accuracy,
                              final boolean hideMarker)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
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
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
                else
                {
                    if (hideMarker)
                    {
                        marker.setVisible(false);
                    }
                    else
                    {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

}
