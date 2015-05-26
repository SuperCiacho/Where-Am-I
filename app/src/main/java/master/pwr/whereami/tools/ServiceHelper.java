package master.pwr.whereami.tools;

import android.content.Context;
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
    private final Hashtable<String, Object> services;
    private Context context;

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
}


