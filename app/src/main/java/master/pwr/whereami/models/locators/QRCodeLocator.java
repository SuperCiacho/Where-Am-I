package master.pwr.whereami.models.locators;

import android.app.Fragment;
import android.content.Context;
import android.location.LocationManager;

import master.pwr.whereami.R;
import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.fragments.QRReaderFragment;


/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class QRCodeLocator extends BaseLocator
{
    public QRCodeLocator(Context context)
    {
        super(context);
    }

    @Override
    protected void setup()
    {
        providerName = LocationManager.PASSIVE_PROVIDER;
        locationProvider = locationManager.getProvider(providerName);
    }

    @Override
    public String getName()
    {
        return LocationStrategyType.QR_CODE.getDescription();
    }

    @Override
    public void localize(Fragment fragment)
    {
        super.localize(fragment);

        QRReaderFragment qrFragment = QRReaderFragment.newInstance();
        fragment.getFragmentManager()
                .beginTransaction()
                .replace(R.id.inner_fragment_container, qrFragment)
                .commit();
    }
}