package master.pwr.whereami.models.locators;

import android.content.Context;
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
    }

    @Override
    protected void setup()
    {
        locationProvider = locationManager.getProvider(LocationManager.PASSIVE_PROVIDER);
        ServiceHelper.getInstance().setWifiEnabled(false);
        ServiceHelper.getInstance().setMobileDataEnabled(false);

        nfc = NfcAdapter.getDefaultAdapter(context);
    }

    @Override
    public String getName()
    {
        return LocationStrategyType.DEAD_RECKONING.getDescription();
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
