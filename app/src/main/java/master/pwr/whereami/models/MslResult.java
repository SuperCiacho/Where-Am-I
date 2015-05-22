package master.pwr.whereami.models;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-20.
 */
public class MslResult
{
    private String status;
    private float lat;
    private float lon;
    private float accuracy;

    public MslResult()
    {
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public float getLat()
    {
        return lat;
    }

    public void setLat(float lat)
    {
        this.lat = lat;
    }

    public float getLon()
    {
        return lon;
    }

    public void setLon(float lon)
    {
        this.lon = lon;
    }

    public float getAccuracy()
    {
        return accuracy;
    }

    public void setAccuracy(float accuracy)
    {
        this.accuracy = accuracy;
    }
}
