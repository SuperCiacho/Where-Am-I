package master.pwr.whereami.models;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public class MapUpdate
{
    private static final LatLng DEFAULT_POSITION = new LatLng(51.5009489f, 18.006975f);
    private static final float DEFAULT_ACCURACY = 10000.0f;
    private final LatLng position;
    private final float accuracy;

    public MapUpdate(LatLng position)
    {
        this(position, DEFAULT_ACCURACY);
    }

    public MapUpdate(LatLng position, float accuracy)
    {
        this.position = position;
        this.accuracy = accuracy;
    }

    public MapUpdate(Location newLocation)
    {
        this(newLocation == null ?
                        DEFAULT_POSITION :
                        new LatLng(newLocation.getLatitude(), newLocation.getLongitude()),
                newLocation == null ?
                        DEFAULT_ACCURACY :
                        newLocation.getAccuracy());
    }

    public static MapUpdate Default()
    {
        return new MapUpdate(DEFAULT_POSITION);
    }

    public LatLng getPosition()
    {
        return position;
    }

    public float getAccuracy()
    {
        return accuracy;
    }
}
