package master.pwr.whereami.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eu.livotov.zxscan.ScannerView;
import master.pwr.whereami.R;

public class QRReaderFragment extends Fragment
{
    public static final String TAG = "QrCode";
    private ScannerView scanner;
    private ScannerView.ScannerViewEventListener callback;

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
        scanner.setScannerViewEventListener(callback);
        scanner.setHudVisible(true);

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (scanner != null)
        {
            scanner.startScanner();
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        if (!(activity instanceof ScannerView.ScannerViewEventListener))
        {
            throw new IllegalStateException("Activity must implement ScannerView's ScannerViewEventListener.");
        }
        callback = (ScannerView.ScannerViewEventListener) activity;
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
}
