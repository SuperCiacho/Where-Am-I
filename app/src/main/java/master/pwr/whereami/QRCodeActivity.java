package master.pwr.whereami;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import eu.livotov.zxscan.ScannerView;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class QRCodeActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        final ScannerView scanner = (ScannerView) findViewById(R.id.scanner);
        scanner.setScannerViewEventListener(new ScannerView.ScannerViewEventListener()
        {
            @Override
            public void onScannerReady()
            {

            }

            @Override
            public void onScannerFailure(int i)
            {

            }

            public boolean onCodeScanned(final String data)
            {
                scanner.stopScanner();
                Toast.makeText(QRCodeActivity.this, "Data scanned: " + data, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        scanner.startScanner();
    }
}
