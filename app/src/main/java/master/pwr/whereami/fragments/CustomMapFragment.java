package master.pwr.whereami.fragments;

import android.app.FragmentManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import master.pwr.whereami.R;

public class CustomMapFragment extends Fragment
{
    public static final String TAG = "Maps";

    View.OnClickListener callback;

    LatLng position;
    GoogleMap map;

    public static CustomMapFragment newInstance()
    {
        return new CustomMapFragment();
    }

    public CustomMapFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_custom_map, container, false);

        if(getArguments() != null)
        {
            String[] positionData = getArguments().getString("data").split(":");
            position = new LatLng(
                    Float.parseFloat(positionData[0]),
                    Float.parseFloat(positionData[1]));
        }
        else
        {
            position = new LatLng(0,0);
        }

        v.findViewById(R.id.button).setOnClickListener(callback);

        if(map == null)
        {
            MapFragment mapFragment = (MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback()
            {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    map = googleMap;
                    updateMap();
                }
            });
        }

        return v;
    }

    public void setOnLocateButtonListener(View.OnClickListener listener)
    {
        callback = listener;
    }

    public void updateMap()
    {
        MarkerOptions m = new MarkerOptions();
        m.position(position);
        map.addMarker(m);
        moveCameraToPosition(map, position);
    }

    private void moveCameraToPosition(GoogleMap googleMap, LatLng position)
    {
        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(position)
                .zoom(15)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}
