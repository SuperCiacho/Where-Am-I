package master.pwr.whereami.activities;

import android.content.Intent;
import android.location.GpsStatus;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import master.pwr.whereami.activities.base.BaseActivity;
import master.pwr.whereami.models.LMLocationListener;
import master.pwr.whereami.models.MapUpdate;
import master.pwr.whereami.tools.ServiceHelper;

public class GpsOnlyActivity extends BaseActivity implements GpsStatus.Listener
{
    private LocationListener locationListener;

    public GpsOnlyActivity()
    {
        super(25.0f);

        providerName = LocationManager.GPS_PROVIDER;
        locationListener = new LMLocationListener(this);
    }

    @Override
    protected void startLocation()
    {
        location = locationManager.getLastKnownLocation(providerName);

        locationManager.addGpsStatusListener(this);
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                TimeUnit.SECONDS.toMillis(interval),
                MIN_DISTANCE,
                locationListener
        );

        isWorking = true;

        collectStats();
        runStopwatch();
    }

    @Override
    protected void stopLocation()
    {
        locationManager.removeUpdates(locationListener);
        isWorking = false;
        collectStats();
        runStopwatch();
    }

    @Override
    protected boolean prepare()
    {
        boolean result = true;
        attemptCounter = 0;

        ServiceHelper.getInstance().setWifiEnabled(false);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            result = false;
            Toast.makeText(this, "Zmień tryb na \"Tylko GPS\" oraz wyłącz sieci komórkowe.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

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
