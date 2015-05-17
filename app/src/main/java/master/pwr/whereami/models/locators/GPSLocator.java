package master.pwr.whereami.models.locators;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.util.Log;
import android.util.LogPrinter;

import master.pwr.whereami.enums.LocationStrategyType;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class GPSLocator extends BaseLocator
{
    public GPSLocator(Context context)
    {
        super(context);
    }

    @Override
    protected void setup()
    {
        locationProvider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
    }

    @Override
    public String getName()
    {
        return LocationStrategyType.GPS.getDescription();
    }

    @Override
    public void dumpStats()
    {
        long l = getBatteryStatsReader().getBatteryManager().getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
        getBatteryStatsReader().getBatteryInfo().dump(new LogPrinter(Log.VERBOSE, "Where Am I: "), "[Where Am I?] ");
    }

    @Override
    public void localize()
    {

    }

    @Override
    public void stop()
    {

    }

    @Override
    public void reset()
    {

    }

    @Override
    public void restart()
    {

    }
}
