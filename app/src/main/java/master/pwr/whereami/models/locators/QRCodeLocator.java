package master.pwr.whereami.models.locators;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import master.pwr.whereami.DetailActivity;
import master.pwr.whereami.QRCodeActivity;
import master.pwr.whereami.enums.LocationStrategyType;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class QRCodeLocator extends BaseLocator
{
    public QRCodeLocator(Activity activity)
    {
        super(activity);
    }

    @Override
    protected void setup()
    {
        locationProvider = locationManager.getProvider(LocationManager.PASSIVE_PROVIDER);
    }

    @Override
    public String getName()
    {
        return LocationStrategyType.QR_CODE.getDescription();
    }

    @Override
    public void dumpStats()
    {
    }

    @Override
    public void localize()
    {
        ((Activity)context).startActivityForResult(new Intent(context, QRCodeActivity.class), 50123);
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
