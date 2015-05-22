package master.pwr.whereami.activities;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.TimeUnit;

import master.pwr.whereami.activities.base.BaseActivity;
import master.pwr.whereami.models.MapUpdate;
import master.pwr.whereami.tools.ServiceHelper;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public class GsmActivity extends BaseActivity
{
    private LocationListener locationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            GsmActivity.this.location = location;
            position = new LatLng(location.getLatitude(), location.getLongitude());

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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        providerName = LocationManager.NETWORK_PROVIDER;
    }

    @Override
    public void onClick(View v)
    {
        if (isWorking)
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

        ServiceHelper.getInstance().setWifiEnabled(false);

        if (!ServiceHelper.getInstance().getMobileDataEnabled())
        {
            result = false;
            messageBuilder.append("Włącz dane mobilne.\n");
        }

        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            result = false;
            messageBuilder.append("Zmień tryb określania pozycji na \"Tryb oszczędny\".\n");
        }

        if (!result)
        {
            Toast.makeText(this, messageBuilder.toString(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        return result;
    }

    @Override
    protected void startLocation()
    {
        location = locationManager.getLastKnownLocation(providerName);
        locationManager.requestLocationUpdates(
                providerName,
                TimeUnit.SECONDS.toMillis(1),
                MIN_DISTANCE,
                locationListener
        );

        isWorking = true;

        dumpStats(true);
        measureTime(true);
    }

    @Override
    protected void stopLocation()
    {
        locationManager.removeUpdates(locationListener);
        dumpStats(false);
        measureTime(false);
        isWorking = false;
    }
}