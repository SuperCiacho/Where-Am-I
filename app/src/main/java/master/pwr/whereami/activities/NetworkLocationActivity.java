package master.pwr.whereami.activities;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import master.pwr.whereami.activities.base.BaseActivity;
import master.pwr.whereami.models.LMLocationListener;
import master.pwr.whereami.tools.ServiceHelper;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public class NetworkLocationActivity extends BaseActivity
{
    private LocationListener locationListener;

    public NetworkLocationActivity()
    {
        super(15.0f);

        providerName = LocationManager.NETWORK_PROVIDER;
        locationListener = new LMLocationListener(this);
    }

    @Override
    protected boolean prepare()
    {
        boolean result = true;
        executionTime = 0;

        ServiceHelper.getInstance().setWifiEnabled(true);

        if (!locationManager.isProviderEnabled(providerName))
        {
            result = false;
            Toast.makeText(this, "Zmień tryb określania pozycji na \"Tryb oszczędny\".\nPamiętaj o włączeniu danych komórkowych!", Toast.LENGTH_LONG)
                 .show();
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
}