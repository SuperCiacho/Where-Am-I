package master.pwr.whereami.enums;

import android.util.Log;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public enum LocationStrategyType
{
    HIGH_ACCURACY(0, "Wysoka dokładność"),
    NETWORK(1, "Sieci bezprzewodowe"),
    GPS_ONLY(2, "Tylko GPS"),
    FUSED_LOCATION(3, "Fused Location Provider");

    private int value;
    private String desc;
    LocationStrategyType(int id, String description)
    {
        value = id;
        desc = description;
    }

    public static LocationStrategyType getByValue(int value)
    {
        LocationStrategyType lst = null;
        for (LocationStrategyType type : LocationStrategyType.values())
        {
            if (type.value == value)
            {
                lst = type;
                break;
            }
        }

        if (lst == null)
        {
            Log.d("Location Strategy Type", "Not found any LST for value:" + value);
        }

        return lst;
    }

    public String getDescription()
    {
        return desc;
    }

    public int getValue()
    {
        return value;
    }
}
