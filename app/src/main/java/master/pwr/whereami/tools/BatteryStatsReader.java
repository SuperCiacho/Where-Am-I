package master.pwr.whereami.tools;

import android.app.ApplicationErrorReport;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class BatteryStatsReader extends BroadcastReceiver
{
    private int level;
    private int voltage;
    private ApplicationErrorReport.BatteryInfo batteryInfo;
    private BatteryManager batteryManager;

    public BatteryStatsReader()
    {
        batteryInfo = new ApplicationErrorReport.BatteryInfo();
        batteryManager = new BatteryManager();
    }

    public void fetchBatteryStats(Context context)
    {
        context.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        context.unregisterReceiver(this);

        int rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (rawLevel >= 0 && scale > 0) {
            level = (rawLevel * 100) / scale;
        }
        else
            level = rawLevel;

        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
    }

    public int getLevel()
    {
        int l = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
        return level;
    }

    public int getVoltage()
    {
        return voltage;
    }

    public ApplicationErrorReport.BatteryInfo getBatteryInfo()
    {
        return batteryInfo;
    }

    public BatteryManager getBatteryManager()
    {
        return batteryManager;
    }
}
