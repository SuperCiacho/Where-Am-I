package master.pwr.whereami.tools;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.util.Hashtable;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class ServiceHelper
{
    private static ServiceHelper instance;

    private Context context;
    private Hashtable<String, Object> services;

    private ServiceHelper()
    {
        services = new Hashtable<>();
    }

    public static ServiceHelper getInstance()
    {
        if (instance == null)
        {
            instance = new ServiceHelper();
        }
        return instance;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }

    /**
     * Wifi Manager
     */
    private WifiManager getWifiManager()
    {
        if (!services.containsKey(Context.WIFI_SERVICE))
        {
            services.put(Context.WIFI_SERVICE, context.getSystemService(Context.WIFI_SERVICE));
        }

        return (WifiManager) services.get(Context.WIFI_SERVICE);
    }

    public boolean isWifiEnabled()
    {
        return getWifiManager().isWifiEnabled();
    }

    public void setWifiEnabled(boolean toggle)
    {
        if (isWifiEnabled() != toggle)
        {
            getWifiManager().setWifiEnabled(toggle);
            Toast.makeText(
                    context,
                    String.format("W%słączono moduł WiFi.", toggle ? "" : "y"),
                    Toast.LENGTH_SHORT).show();
        }
    }

    public int calculateDistance(ScanResult scanResult)
    {
        return WifiManager.calculateSignalLevel(scanResult.level, 1);
    }


    /**
     * Mobile Data (Connectivity Manager)
     */

    private TelephonyManager getTelephonyManager()
    {
        if (!services.containsKey(Context.TELEPHONY_SERVICE))
        {
            services.put(Context.TELEPHONY_SERVICE, context.getSystemService(Context.TELEPHONY_SERVICE));
        }

        return (TelephonyManager) services.get(Context.TELEPHONY_SERVICE);
    }

    public boolean getMobileDataEnabled()
    {
        int state = getTelephonyManager().getDataState();
        return state == TelephonyManager.DATA_CONNECTED || state == TelephonyManager.DATA_CONNECTING;
    }

    public void setMobileDataEnabled(boolean mobileData)
    {
        Toast.makeText(
                context,
                String.format("%s dane mobilne.", mobileData ? "Włącz" : "Wyłącz"),
                Toast.LENGTH_SHORT).show();

        /*
        Intent mobileDataIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        mobileDataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mobileDataIntent);
        */
    }

    /**
     * Sensor manager
     */
    private SensorManager getSensorManager()
    {
        if (!services.containsKey(Context.SENSOR_SERVICE))
        {
            services.put(Context.SENSOR_SERVICE, context.getSystemService(Context.SENSOR_SERVICE));
        }

        return (SensorManager) services.get(Context.SENSOR_SERVICE);
    }

    public boolean isSensorAvailable(int sensorType)
    {
        return getSensorManager().getDefaultSensor(sensorType) != null;
    }

    public Sensor getSensor(int sensorType)
    {
        return getSensorManager().getDefaultSensor(sensorType);
    }

}


