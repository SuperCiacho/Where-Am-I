package master.pwr.whereami.activities;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import master.pwr.whereami.activities.base.BaseActivity;
import master.pwr.whereami.models.MapUpdate;
import master.pwr.whereami.tools.ServiceHelper;

public class WifiActivity extends BaseActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        providerName = LocationManager.NETWORK_PROVIDER;
    }

    @Override
    public void onClick(View v)
    {
        if(isWorking)
        {
            stopLocation();
        }
        else
        {
            if (prepare())
            {
                startLocation();
            }
        }

        setViewText(v, isWorking);
    }

    @Override
    protected boolean prepare()
    {
        boolean result = true;
        executionTime = 0;
        messageBuilder.setLength(0);

        ServiceHelper.getInstance().setWifiEnabled(true);

        if (ServiceHelper.getInstance().getMobileDataEnabled())
        {
            result = false;
            messageBuilder.append("Wyłącz dane mobilne.\n");
        }

        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            result = false;
            messageBuilder.append("Zmień tryb określania pozycji na \"Tryb oszczędny\".\n");
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        if (!result)
        {
            Toast.makeText(this, messageBuilder.toString(), Toast.LENGTH_LONG).show();
        }

        return result;
    }

    @Override
    protected void startLocation()
    {
        dumpStats(true);
        measureTime(true);

        location = locationManager.getLastKnownLocation(providerName);
        locationManager.requestLocationUpdates(
                providerName,
                TimeUnit.SECONDS.toMillis(1),
                MIN_DISTANCE,
                locationListener
        );

        isWorking = true;
    }

    @Override
    protected void stopLocation()
    {

    }

    private LocationListener locationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            WifiActivity.this.location = location;
            dumpStats(false);
            updateMap(new MapUpdate(location));

            isLocationSufficient(location);
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
    };
}
