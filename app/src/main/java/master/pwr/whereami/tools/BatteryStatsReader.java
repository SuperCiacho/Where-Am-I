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
    private float level;
    private int voltage;

    public void fetchBatteryStats(Context context)
    {
        Intent batteryIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        onReceive(context, batteryIntent);
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {
        int rawLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if (rawLevel >= 0 && scale > 0) {
            level = (rawLevel * 100) / (float) scale;
        }
        else
            level = rawLevel;

        voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
    }

    public float getLevel()
    {
        return level;
    }

    public int getVoltage()
    {
        return voltage;
    }
}
