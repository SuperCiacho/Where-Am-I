package master.pwr.whereami.models.locators;

import android.content.Context;
import android.location.LocationManager;
import android.location.LocationProvider;

import master.pwr.whereami.interfaces.LocationStrategy;
import master.pwr.whereami.tools.BatteryStatsReader;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public abstract class BaseLocator implements LocationStrategy
{
    Context context;
    BatteryStatsReader batteryStatsReader;
    LocationManager locationManager;
    LocationProvider locationProvider;

    public BaseLocator(Context context)
    {
        this.context = context;
        batteryStatsReader = new BatteryStatsReader();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        setup();
    }

    protected abstract void setup();

    public BatteryStatsReader getBatteryStatsReader()
    {
        return batteryStatsReader;
    }

    @Override
    public Object retrieveStats()
    {
        getBatteryStats();
        return null;
    }

    private void getBatteryStats()
    {
        batteryStatsReader.fetchBatteryStats(context);

    }
}
