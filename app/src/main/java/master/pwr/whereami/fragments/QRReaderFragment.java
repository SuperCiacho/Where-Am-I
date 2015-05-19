package master.pwr.whereami.fragments;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;

import eu.livotov.zxscan.ScannerView;
import master.pwr.whereami.R;
import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.tools.LocatorFactory;

public class QRReaderFragment extends Fragment implements ScannerView.ScannerViewEventListener
{
    private ScannerView scanner;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QRReaderFragment.
     */
    public static QRReaderFragment newInstance()
    {
        return new QRReaderFragment();
    }

    public QRReaderFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_qrreader, container, false);

        scanner = (ScannerView) v.findViewById(R.id.scanner);
        scanner.setScannerViewEventListener(this);
        scanner.setHudVisible(true);
        scanner.startScanner();

        return v;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        if (scanner != null)
        {
            scanner.startScanner();
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        if (scanner != null) scanner.stopScanner();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (scanner != null) scanner.stopScanner();
    }

    @Override
    public void onScannerReady()
    {

    }

    @Override
    public void onScannerFailure(int i)
    {
        Toast.makeText(getActivity(), "Coś poszło nie tak.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCodeScanned(String data)
    {
        scanner.stopScanner();

        if (data == null || data.isEmpty()) return false;

        Intent i = new Intent();
        i.putExtra("data", data);

        LocatorFactory.getLocationStrategy(getActivity(), LocationStrategyType.QR_CODE).update(i);

        return true;
    }
}
