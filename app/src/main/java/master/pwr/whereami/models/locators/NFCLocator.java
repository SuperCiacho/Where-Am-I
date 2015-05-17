package master.pwr.whereami.models.locators;

import android.content.Context;
import android.location.LocationManager;

import master.pwr.whereami.enums.LocationStrategyType;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class NFCLocator extends BaseLocator
{
    public NFCLocator(Context context)
    {
        super(context);
    }

    @Override
    protected void setup()
    {
        locationProvider = locationManager.getProvider(LocationManager.PASSIVE_PROVIDER);
    }

    @Override
    public String getName()
    {
        return LocationStrategyType.NFC.getDescription();
    }

    @Override
    public void dumpStats()
    {

    }

    @Override
    public void localize()
    {

    }

    @Override
    public void stop()
    {

    }

    @Override
    public void reset()
    {

    }

    @Override
    public void restart()
    {

    }
}
