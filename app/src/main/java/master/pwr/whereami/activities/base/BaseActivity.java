package master.pwr.whereami.activities.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import master.pwr.whereami.R;
import master.pwr.whereami.fragments.CustomMapFragment;
import master.pwr.whereami.fragments.StatsFragment;
import master.pwr.whereami.models.MapUpdate;
import master.pwr.whereami.models.Stats;
import master.pwr.whereami.tools.BatteryStatsReader;
import master.pwr.whereami.tools.StatDumper;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener
{
    protected static final float MIN_DISTANCE = 0.0f;
    protected static final int MAX_ATTEMPTS = 100;

    protected StringBuilder messageBuilder;
    protected List<Stats> statsList;

    protected long executionTime;
    protected int attemptCounter;

    protected BatteryStatsReader batteryStatsReader;
    protected LocationManager locationManager;
    protected LocationProvider locationProvider;
    protected String providerName;
    protected Location location;
    protected LatLng position;

    protected boolean isWorking;

    private CustomMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_template);

        statsList = new ArrayList<>(4);
        messageBuilder = new StringBuilder();

        findViewById(R.id.button_1).setOnClickListener(onMapButtonClickListener);
        findViewById(R.id.button_2).setOnClickListener(onStatButtonClickListener);

        showMapFragment(null);

        batteryStatsReader = new BatteryStatsReader();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    protected abstract boolean prepare();

    protected abstract void startLocation();

    protected abstract void stopLocation();

    protected void isLocationSufficient(Location location)
    {
        if (++attemptCounter == MAX_ATTEMPTS || location.getAccuracy() < 11)
        {
            stopLocation();
        }
    }

    protected String getName()
    {
        return getTitle().toString();
    }

    protected void setViewText(View view, boolean isWorking)
    {
        ((TextView) view).setText(isWorking ? R.string.abort_location : R.string.get_location);
    }

    protected Stats retrieveStats()
    {
        Stats stats = new Stats();
        stats.setPosition(position);
        stats.setMethodName(getName());
        stats.setExecutionTime(executionTime);
        getBatteryStats(stats);
        return stats;
    }

    private void getBatteryStats(Stats batteryStats)
    {
        batteryStatsReader.fetchBatteryStats(this);
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

    public void dumpStats(boolean beforeLocation)
    {
        executionTime = 0;
        Stats s = retrieveStats();
        statsList.add(s);
        String prefix = beforeLocation ? "[BEFORE]" : "[AFTER]";
        StatDumper.getInstance().dumpLog(retrieveStats(), prefix);
    }

    private CustomMapFragment getMapFragment()
    {
        if (mapFragment == null)
        {
            mapFragment = (CustomMapFragment) getFragmentManager().findFragmentByTag(CustomMapFragment.TAG);
        }

        return mapFragment;
    }

    protected void showMapFragment(Bundle args)
    {
        FragmentManager fm = getFragmentManager();

        Fragment mapFragment = getFragmentManager().findFragmentByTag(CustomMapFragment.TAG);
        if (mapFragment == null)
        {
            mapFragment = CustomMapFragment.newInstance();
            ((CustomMapFragment) mapFragment).setOnLocateButtonListener(this);
        }

        if (mapFragment.isVisible()) return;

        if (args != null)
        {
            mapFragment.setArguments(args);
        }

        fm.beginTransaction()
          .replace(R.id.inner_fragment_container, mapFragment, CustomMapFragment.TAG)
          .commit();
    }

    protected void updateMap(MapUpdate update)
    {
        getMapFragment().updateMap(update);
    }

    private View.OnClickListener onMapButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            showMapFragment(null);
        }
    };

    private View.OnClickListener onStatButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            if (statsList.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Brak statystyk", Toast.LENGTH_SHORT).show();
                return;
            }

            FragmentManager fm = getFragmentManager();
            Fragment statsFragment = getFragmentManager().findFragmentByTag(StatsFragment.TAG);

            if (statsFragment == null)
            {
                statsFragment = StatsFragment.newInstance(statsList);
            }

            if (statsFragment.isVisible()) return;

            fm.beginTransaction()
              .replace(R.id.inner_fragment_container, statsFragment, CustomMapFragment.TAG)
              .commit();
        }
    };
}
