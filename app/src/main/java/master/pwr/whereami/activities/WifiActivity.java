package master.pwr.whereami.activities;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import master.pwr.whereami.activities.base.BaseActivity;
import master.pwr.whereami.models.MapUpdate;
import master.pwr.whereami.models.MslResult;
import master.pwr.whereami.models.MslSearchRecord;
import master.pwr.whereami.models.net.NetResult;
import master.pwr.whereami.tools.RESTClient;
import master.pwr.whereami.tools.ServiceHelper;
import master.pwr.whereami.tools.WifiScanner;

public class WifiActivity extends BaseActivity implements WifiScanner.Listener
{
    private static final String API_KEY = "69702f37-5b49-4fbb-856d-87cbd379911b";
    private static final String SERVER_ADDRESS = "https://location.services.mozilla.com/v1/search?key=";
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

        WifiScanner.addListener(this, this);
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
        WifiScanner.removeListener(this);
        locationManager.removeUpdates(locationListener);
        dumpStats(false);
        measureTime(false);
    }

    @Override
    public void onScanResult(final List<ScanResult> results)
    {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... params)
            {
                RESTClient rc = new RESTClient(WifiActivity.this, WifiActivity.SERVER_ADDRESS + WifiActivity.API_KEY);

                MslSearchRecord msr = new MslSearchRecord();
                List<Integer> distances = new ArrayList<>(results.size());
                List<NetResult<MslResult>> data = new ArrayList<>();

                for (ScanResult r : results)
                {
                    if (r.SSID.endsWith("_nomap")) continue;

                    msr.setKey(r.BSSID);
                    msr.setSignal(r.level);

                    try
                    {
                        data.add(rc.checkLocation(msr));
                        distances.add(ServiceHelper.getInstance().calculateDistance(r));
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                // TODO Dokończyć obliczenia

                return null;
            }
        }.execute();
    }
}
