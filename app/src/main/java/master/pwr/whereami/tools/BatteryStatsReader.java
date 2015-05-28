package master.pwr.whereami.tools;

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
    private static BatteryStatsReader instance;
    private BatteryManager batteryManager;
    private float level;
    private int voltage;
    private long current;
    private String health;
    private String plugType;
    private String status;

    private BatteryStatsReader(Context context)
    {
        batteryManager = new BatteryManager();
        Intent batteryIntent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        onReceive(context, batteryIntent);
    }

    public static BatteryStatsReader getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new BatteryStatsReader(context);
        }

        return instance;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean isPresent = intent.getBooleanExtra("present", false);

        if (isPresent)
        {
            int rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            if (rawLevel >= 0 && scale > 0)
            {
                level = (rawLevel * 100) / (float) scale;
            }
            else
            {
                level = rawLevel;
            }

            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);

            current = batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
            health = getHealthString(intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1));
            status = getStatusString(intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1));
            plugType = getPlugTypeString(intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1));
        }
    }

    public float getLevel()
    {
        return level;
    }

    public int getVoltage()
    {
        return voltage;
    }

    public long getCurrent()
    {
        return current;
    }

    private String getPlugTypeString(int plugged)
    {
        String plugType = "Unknown";

        switch (plugged)
        {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = "USB";
                break;
        }
        return plugType;
    }

    private String getHealthString(int health)
    {
        String healthString = "Unknown";
        switch (health)
        {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good Condition";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Over Heat";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Failure";
                break;
        }
        return healthString;
    }

    private String getStatusString(int status)
    {
        String statusString = "Unknown";

        switch (status)
        {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = "Not Charging";
                break;
        }
        return statusString;
    }
}
