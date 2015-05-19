package master.pwr.whereami.models.locators;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.tools.ServiceHelper;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class GPSLocator extends BaseLocator implements GpsStatus.Listener
{
    private static final float MIN_DISTANCE = 8.0f;

    public GPSLocator(Context context)
    {
        super(context);
    }

    @Override
    protected void setup()
    {
        providerName = LocationManager.GPS_PROVIDER;
        locationProvider = locationManager.getProvider(providerName);
    }

    @Override
    public String getName()
    {
        return LocationStrategyType.GPS.getDescription();
    }

    @Override
    public void prepare()
    {
        if (!locationManager.isProviderEnabled(providerName))
        {
            Toast.makeText(context, "Włącz moduł GPS", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        ServiceHelper.getInstance().setWifiEnabled(false);
        ServiceHelper.getInstance().setMobileDataEnabled(false);
    }

    @Override
    public void localize(Fragment fragment)
    {
        super.localize(fragment);


        getCurrentLocation();

    }

    private void getCurrentLocation()
    {
        location = locationManager.getLastKnownLocation(providerName);

        locationManager.addGpsStatusListener(this);
        locationManager.requestLocationUpdates(
                providerName,
                TimeUnit.SECONDS.toMillis(5),
                MIN_DISTANCE,
                new LocationListener()
                {
                    @Override
                    public void onLocationChanged(Location location)
                    {
                        GPSLocator.this.location = location;
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
        );
    }


    @Override
    public void onGpsStatusChanged(int event)
    {
        switch (event)
        {
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                break;
        }
    }
}
