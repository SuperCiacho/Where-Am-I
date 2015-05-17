package master.pwr.whereami.tools;

import android.app.Activity;

import java.util.EnumMap;

import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.models.locators.BaseLocator;
import master.pwr.whereami.models.locators.DeadReckoningLocator;
import master.pwr.whereami.models.locators.GPSLocator;
import master.pwr.whereami.models.locators.GSMLocator;
import master.pwr.whereami.models.locators.NFCLocator;
import master.pwr.whereami.models.locators.QRCodeLocator;
import master.pwr.whereami.models.locators.WiFiLocator;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public final class LocatorFactory
{
    private static EnumMap<LocationStrategyType, BaseLocator> locators = new EnumMap<>(LocationStrategyType.class);

    public static BaseLocator getLocationStrategy(Activity activity, LocationStrategyType locationStrategyType)
    {
        if(locationStrategyType == null || activity == null)
        {
            throw new IllegalArgumentException("Are you serious? Null? As method parameter? Not on my watch!");
        }

        BaseLocator ls = locators.get(locationStrategyType);
        if (ls == null)
        {
            switch (locationStrategyType)
            {
                case DEAD_RECKONING:
                    ls = new DeadReckoningLocator(activity);
                    break;
                case GPS:
                    ls = new GPSLocator(activity);
                    break;
                case GSM:
                    ls = new GSMLocator(activity);
                    break;
                case NFC:
                    ls = new NFCLocator(activity);
                    break;
                case QR_CODE:
                    ls = new QRCodeLocator(activity);
                    break;
                case WIFI:
                    ls = new WiFiLocator(activity);
                    break;
                default:
                    throw new IllegalArgumentException("Provided location strategy type is not supported and never will be!");
            }

            locators.put(locationStrategyType, ls);
        }

        return ls;
    }

    public static void resetAll()
    {
        for(LocationStrategyType type : locators.keySet())
        {
            reset(type);
        }
    }

    public static void reset(LocationStrategyType type)
    {
        locators.get(type).reset();
        locators.remove(type);
    }

}
