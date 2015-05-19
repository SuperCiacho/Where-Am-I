package master.pwr.whereami.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import eu.livotov.zxscan.ScannerView;
import master.pwr.whereami.R;
import master.pwr.whereami.fragments.CustomMapFragment;
import master.pwr.whereami.fragments.QRReaderFragment;

public class QrCodeActivity extends BaseActivity implements ScannerView.ScannerViewEventListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_template);

        onMapButtonClickListener.onClick(null);
    }

    @Override
    public void onClick(View v)
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Fragment qrCode = getFragmentManager().findFragmentByTag(QRReaderFragment.TAG);
        Fragment mapFragment = getFragmentManager().findFragmentByTag(CustomMapFragment.TAG);
        if (mapFragment != null)
        {
            ft.hide(mapFragment);
        }

        if (qrCode == null)
        {
            qrCode = QRReaderFragment.newInstance();
            ft.add(R.id.inner_fragment_container, qrCode, QRReaderFragment.TAG);
        }

        ft.show(mapFragment);

        ft.commit();
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

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        CustomMapFragment mapFragment = CustomMapFragment.newInstance();
        mapFragment.setOnLocateButtonListener(this);

        Bundle args = new Bundle();
        args.putString("data", data);
        mapFragment.setArguments(args);

        ft.replace(R.id.inner_fragment_container, mapFragment, CustomMapFragment.TAG);
        ft.commit();

        return true;
    }
}
