package master.pwr.whereami.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-18.
 */
public class Stats implements Serializable
{
    private int batteryLevel;
    private int batteryVoltage;
    private long executionTime;
    private LatLng position;
    private String providerName;
    private String methodName;


    public int getBatteryLevel()
    {
        return batteryLevel;
    }

    public void setBatteryLevel(int batteryLevel)
    {
        this.batteryLevel = batteryLevel;
    }

    public int getBatteryVoltage()
    {
        return batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage)
    {
        this.batteryVoltage = batteryVoltage;
    }

    public long getExecutionTime()
    {
        return executionTime;
    }

    public void setExecutionTime(long executionTime)
    {
        this.executionTime = executionTime;
    }

    public void setPosition(LatLng position)
    {
        this.position = position;
    }

    public LatLng getPosition()
    {
        return position;
    }

    public void setProviderName(String providerName)
    {
        this.providerName = providerName;
    }

    public String getProviderName()
    {
        return providerName;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }
}
