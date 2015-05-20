package master.pwr.whereami.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import eu.livotov.zxscan.ScannerView;
import master.pwr.whereami.R;
import master.pwr.whereami.activities.base.BaseActivity;
import master.pwr.whereami.fragments.QRReaderFragment;

public class QrCodeActivity extends BaseActivity implements ScannerView.ScannerViewEventListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        providerName = LocationManager.PASSIVE_PROVIDER;
    }

    @Override
    protected boolean prepare()
    {
        PackageManager packageManager = getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    @Override
    protected void startLocation()
    {
        FragmentManager fm = getFragmentManager();
        Fragment qrCode = getFragmentManager().findFragmentByTag(QRReaderFragment.TAG);
        if (qrCode == null)
        {
            qrCode = QRReaderFragment.newInstance();
        }

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.inner_fragment_container, qrCode, QRReaderFragment.TAG);
        ft.commit();

        dumpStats(true);
        measureTime(true);
    }

    private void stopLocation(String data)
    {
        Bundle args = new Bundle();
        args.putString("data", data);
        showMapFragment(args);

        stopLocation();
    }

    @Override
    protected void stopLocation()
    {
        measureTime(false);
        dumpStats(false);
    }

    @Override
    public void onClick(View v)
    {
        startLocation();
    }

    @Override
    public void onScannerReady()
    {

    }

    @Override
    public void onScannerFailure(int i)
    {
        Toast.makeText(this, "Coś poszło nie tak.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCodeScanned(String data)
    {
        if (data == null || data.isEmpty()) return false;

        stopLocation(data);

        return true;
    }
}
