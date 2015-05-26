package master.pwr.whereami.activities;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        providerName = LocationManager.NETWORK_PROVIDER;
        location = locationManager.getLastKnownLocation(providerName);
        locationListener = new LMLocationListener(this);
    }

    @Override
    protected boolean prepare()
    {
        boolean result = true;
        executionTime = 0;
        messageBuilder.setLength(0);

        ServiceHelper.getInstance().setWifiEnabled(true);

        if (!ServiceHelper.getInstance().getMobileDataEnabled())
        {
            result = false;
            messageBuilder.append("Włącz dane mobilne.\n");
        }

        if (!locationManager.isProviderEnabled(providerName))
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