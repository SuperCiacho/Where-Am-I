package master.pwr.whereami.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public class MapUpdate
{
    private LatLng position;
    private float accuracy;

    public MapUpdate(LatLng position)
    {
        this(position, 0.0f);
    }

    public MapUpdate(LatLng position, float accuracy)
    {
        this.position = position;
        this.accuracy = accuracy;
    }

    public MapUpdate(Location newLocation)
    {
        this.position = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());
        this.accuracy = newLocation.getAccuracy();
    }

    public LatLng getPosition()
    {
        return position;
    }

    public float getAccuracy()
    {
        return accuracy;
    }

    public static MapUpdate Default()
    {
        LatLng pos = new LatLng(51.5009489f, 18.006975f);
        return new MapUpdate(pos, -1.0f);
    }
}
