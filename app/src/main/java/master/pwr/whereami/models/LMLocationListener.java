package master.pwr.whereami.models;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import master.pwr.whereami.activities.base.BaseActivity;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-27.
 */
public class LMLocationListener implements LocationListener
{
    BaseActivity activity;

    public LMLocationListener(BaseActivity activity)
    {
        this.activity = activity;
    }

    public void onLocationChanged(Location location)
    {
        activity.setLocation(location);
        activity.collectStats();
        activity.updateMap(new MapUpdate(location));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider)
    {

    }
}
