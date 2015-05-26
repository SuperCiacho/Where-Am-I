package master.pwr.whereami.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-18.
 */
public class Stats implements Serializable
{
    @StatProperties(StringFormat = "Poziom baterii [%%]: %s ", Order = 6)
    private int batteryLevel;
    @StatProperties(StringFormat = "Napięcie baterii [mV]: %s ", Order = 7)
    private int batteryVoltage;
    @StatProperties(StringFormat = "Czas operacji [ms]: %s ", Order = 4)
    private long executionTime;
    @StatProperties(StringFormat = "Pozycja: %s", Order = 2)
    private LatLng position;
    @StatProperties(StringFormat = "Metoda: %s", Order = 0)
    private String methodName;
    @StatProperties(StringFormat = "Dokładność [m]: %s", Order = 1)
    private float accuracy;
    @StatProperties(StringFormat = "Próba: %s", Order = 3)
    private int attempt;
    @StatProperties(StringFormat = "Interwał [s]: %s", Order = 5)
    private float interval;

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

    public LatLng getPosition()
    {
        return position;
    }

    public void setPosition(LatLng position)
    {
        this.position = position;
    }

    public String getMethodName()
    {
        return methodName;
    }

    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public float getAccuracy()
    {
        return accuracy;
    }

    public void setAccuracy(float accuracy)
    {
        this.accuracy = accuracy;
    }

    public int getAttempt()
    {
        return attempt;
    }

    public void setAttempt(int attempt)
    {
        this.attempt = attempt;
    }

    public float getInterval()
    {
        return interval;
    }

    public void setInterval(float interval)
    {
        this.interval = interval;
    }
}
