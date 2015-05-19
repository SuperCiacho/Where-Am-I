package master.pwr.whereami.models.locators;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
        providerName = LocationManager.NETWORK_PROVIDER;
        locationProvider = locationManager.getProvider(providerName);
        ServiceHelper.getInstance().setWifiEnabled(false);
        ServiceHelper.getInstance().setMobileDataEnabled(true);
    }

    @Override
    public String getName()
    {
        return LocationStrategyType.GSM.getDescription();
    }

    @Override
    public void localize(Fragment fragment)
    {
        super.localize(fragment);
    }
}
