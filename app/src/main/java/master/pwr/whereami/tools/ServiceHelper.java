package master.pwr.whereami.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

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

    private ConnectivityManager getConnectivityManager()
    {
        if (!services.containsKey(Context.CONNECTIVITY_SERVICE))
        {
            services.put(Context.CONNECTIVITY_SERVICE, context.getSystemService(Context.CONNECTIVITY_SERVICE));
        }

        return (ConnectivityManager) services.get(Context.CONNECTIVITY_SERVICE);
    }

    public void setMobileDataEnabled(boolean enabled)
    {
        Method dataMtd = null;

        try
        {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
            dataMtd.setAccessible(true);
        }
        catch (NoSuchMethodException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try
        {
            dataMtd.invoke(getConnectivityManager(), true);
        }
        catch (IllegalArgumentException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (InvocationTargetException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}


