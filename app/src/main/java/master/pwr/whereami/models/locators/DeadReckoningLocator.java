package master.pwr.whereami.models.locators;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.nfc.NfcAdapter;

import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.tools.ServiceHelper;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class DeadReckoningLocator extends BaseLocator
{
    NfcAdapter nfc;

    public DeadReckoningLocator(Context context)
    {
        super(context);
        providerName = LocationManager.PASSIVE_PROVIDER;
    }

    @Override
    protected void setup()
    {
        locationProvider = locationManager.getProvider(providerName);
        ServiceHelper.getInstance().setWifiEnabled(false);
        ServiceHelper.getInstance().setMobileDataEnabled(false);
    }

    @Override
    public String getName()
    {
        return LocationStrategyType.DEAD_RECKONING.getDescription();
    }

    @Override
    public void localize(Fragment fragment)
    {
        super.localize(fragment);
    }
}
