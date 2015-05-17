package master.pwr.whereami.enums;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public enum LocationStrategyType
{
    GPS(0, "GPS"),
    GSM(1, "GSM/3G/LTE"),
    NFC(3, "NFC"),
    QR_CODE(4,"QR Code"),
    WIFI(2, "Sieci WiFi"),
    DEAD_RECKONING(5, "Nawigacja zliczniowa");

    LocationStrategyType(int id, String description)
    {
        value = id;
        desc = description;
    }

    private int value;
    private String desc;

    public String getDescription()
    {
        return desc;
    }

    public int getValue()
    {
        return value;
    }

    public static LocationStrategyType getByValue(int value)
    {
        LocationStrategyType lst = null;
        for(LocationStrategyType type : LocationStrategyType.values())
        {
            if(type.value == value)
            {
                lst = type;
                break;
            }
        }

        return lst;
    }
}
