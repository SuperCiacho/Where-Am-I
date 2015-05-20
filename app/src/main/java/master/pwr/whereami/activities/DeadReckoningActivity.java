package master.pwr.whereami.activities;

import android.content.Intent;
import android.hardware.Sensor;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import master.pwr.whereami.activities.base.BaseActivity;
import master.pwr.whereami.tools.ServiceHelper;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public class DeadReckoningActivity extends BaseActivity
{
    List<Integer> sensorNames;
    SparseArray<Sensor> sensors;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        providerName = LocationManager.PASSIVE_PROVIDER;

        sensorNames = new ArrayList<>(6);
        sensorNames.add(Sensor.TYPE_ACCELEROMETER);
        sensorNames.add(Sensor.TYPE_GYROSCOPE);
        sensorNames.add(Sensor.TYPE_PRESSURE);
        sensorNames.add(Sensor.TYPE_MAGNETIC_FIELD);
        sensorNames.add(Sensor.TYPE_LINEAR_ACCELERATION);
        sensorNames.add(Sensor.TYPE_ROTATION_VECTOR);
        sensorNames.add(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
    }

    @Override
    public void onClick(View v)
    {
        if (isWorking)
        {
            stopLocation();
        }
        else
        {
            if (prepare())
            {
                startLocation();
            }
        }

        setViewText(v, isWorking);
    }

    @Override
    protected boolean prepare()
    {
        if (!locationManager.isProviderEnabled(providerName))
        {
            messageBuilder.append("Zmień tryb określania pozycji na \"Tryb oszczędny\".\n");
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            return false;
        }

        ServiceHelper sh = ServiceHelper.getInstance();
        for (int sensorId : sensorNames)
        {
            if (sh.iSensorAvailable(sensorId))
            {
                sensors.append(sensorId, sh.getSensor(sensorId));
            }
        }

        return sensors.size() > 0;
    }

    @Override
    protected void startLocation()
    {

    }

    @Override
    protected void stopLocation()
    {

    }
}
