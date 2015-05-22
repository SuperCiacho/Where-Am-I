package master.pwr.whereami.activities;

import android.content.Intent;
import android.location.GpsStatus;
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

public class GpsActivity extends BaseActivity implements GpsStatus.Listener
{
    private LocationListener locationListener = new LocationListener()
    {
        @Override
        public void onLocationChanged(Location location)
        {
            GpsActivity.this.location = location;
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
        providerName = LocationManager.GPS_PROVIDER;
        locationProvider = locationManager.getProvider(providerName);
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
    protected void startLocation()
    {
        location = locationManager.getLastKnownLocation(providerName);

        locationManager.addGpsStatusListener(this);
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

    @Override
    protected boolean prepare()
    {
        boolean result = true;
        attemptCounter = 0;

        if (!locationManager.isProviderEnabled(providerName))
        {
            result = false;
            Toast.makeText(this, "Zmień tryb na \"Tylko GPS\".", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        ServiceHelper.getInstance().setWifiEnabled(false);


        return result;
    }

    public void onGpsStatusChanged(int event)
    {
        switch (event)
        {
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                updateMap(location == null ? MapUpdate.Default() : new MapUpdate(location));
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                break;
            case GpsStatus.GPS_EVENT_STARTED:
                Toast.makeText(this, "Rozpoczęto określanie pozycji", Toast.LENGTH_SHORT).show();
                updateMap(MapUpdate.Default());
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                Toast.makeText(this, "Zakończono określanie pozycji", Toast.LENGTH_SHORT).show();
                updateMap(new MapUpdate(location));
                break;
        }
    }
}
