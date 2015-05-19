package master.pwr.whereami.tools;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

import master.pwr.whereami.WhereAmI;

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
        if (!isWifiEnabled())
        {
            getWifiManager().setWifiEnabled(toggle);
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

    public void setMobileDataEnabled(boolean mobileData)
    {
        if(ServiceHelper.getInstance().getMobileDataEnabled())
        {
            Toast.makeText(
                    context,
                    String.format("%s dane mobilne.", mobileData ? "Włącz" : "Wyłącz"),
                    Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(Settings.ACTION_APN_SETTINGS));
        }
    }
}


