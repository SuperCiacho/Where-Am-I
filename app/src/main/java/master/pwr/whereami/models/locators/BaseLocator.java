package master.pwr.whereami.models.locators;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.BatteryManager;
import android.util.Log;
import android.util.LogPrinter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import master.pwr.whereami.R;
import master.pwr.whereami.interfaces.LocationStrategy;
import master.pwr.whereami.interfaces.StatsUpdater;
import master.pwr.whereami.models.Stats;
import master.pwr.whereami.tools.BatteryStatsReader;
import master.pwr.whereami.tools.StatDumper;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public abstract class BaseLocator implements LocationStrategy
{
    protected StatsUpdater statsUpdater;

    protected long executionTime;

    protected Context context;
    protected BatteryStatsReader batteryStatsReader;
    protected LocationManager locationManager;
    protected LocationProvider locationProvider;
    protected String providerName;
    protected Location location;
    protected LatLng position;

    private GoogleMap map;

    public BaseLocator(Context context)
    {
        this.context = context;
        batteryStatsReader = new BatteryStatsReader();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        setup();
    }

    protected abstract void setup();

    @Override
    public void setStatsUpdater(StatsUpdater callback)
    {
        statsUpdater = callback;
    }

    @Override
    public void stop()
    {
        map = null;
    }

    @Override
    public void reset()
    {

    }

    @Override
    public void restart()
    {

    }

    @Override
    public void prepare()
    {

    }

    @Override
    public void localize(Fragment fragment)
    {
        measureTime(true);
    }

    public void showMap(FragmentManager fm)
    {
        MapFragment mapFragment = MapFragment.newInstance();
        fm.beginTransaction()
          .replace(R.id.inner_fragment_container, mapFragment, "map" )
          .commit();

        mapFragment.getMapAsync(new OnMapReadyCallback()
        {
            @Override
            public void onMapReady(GoogleMap googleMap)
            {
                map = googleMap;

                MarkerOptions m = new MarkerOptions();
                m.position(position);
                map.addMarker(m);
                moveCameraToPosition(position);
            }
        });
    }

    private void moveCameraToPosition(LatLng position)
    {
        CameraPosition cameraPosition = new CameraPosition
                .Builder()
                .target(position)
                .zoom(15)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void update(Intent data)
    {
        if(data == null) return;

        measureTime(false);
        String[] locationData = data.getStringExtra("data").split(":");
        float latitude =  Float.parseFloat(locationData[0]);
        float longitude = Float.parseFloat(locationData[1]);
        position = new LatLng(latitude, longitude);

        statsUpdater.updateStats(retrieveStats());
    }

    @Override
    public void dumpStats(boolean beforeLocation)
    {
        String prefix = beforeLocation ? "[BEFORE]" : "[AFTER]";
        StatDumper.getInstance().dumpLog(retrieveStats(), prefix);
    }

    protected Stats retrieveStats()
    {
        Stats stats = new Stats();
        stats.setPosition(position);
        stats.setProviderName(locationProvider.getName());
        stats.setMethodName(getName());
        stats.setExecutionTime(executionTime);
        getBatteryStats(stats);
        return stats;
    }

    private void getBatteryStats(Stats batteryStats)
    {
        batteryStatsReader.fetchBatteryStats(context);
        batteryStats.setBatteryLevel(batteryStatsReader.getLevel());
        batteryStats.setBatteryVoltage(batteryStatsReader.getVoltage());
    }

    public void measureTime(boolean start)
    {
        if (start)
        {
            executionTime = -System.currentTimeMillis();
        }
        else
        {
            executionTime += System.currentTimeMillis();
        }
    }

    public long getExecutionTime()
    {
        return executionTime;
    }
}
