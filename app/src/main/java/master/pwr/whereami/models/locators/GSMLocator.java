package master.pwr.whereami.models.locators;

import android.content.Context;
import android.location.LocationManager;

import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.tools.ServiceHelper;


/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class GSMLocator extends BaseLocator
{
    public GSMLocator(Context context)
    {
        super(context);
    }

    @Override
    protected void setup()
    {
        locationProvider = locationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        ServiceHelper.getInstance().setWifiEnabled(false);
        ServiceHelper.getInstance().setMobileDataEnabled(true);
    }

    @Override
    public String getName()
    {
        return LocationStrategyType.GSM.getDescription();
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
