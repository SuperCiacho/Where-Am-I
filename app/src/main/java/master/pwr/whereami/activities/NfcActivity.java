package master.pwr.whereami.activities;

import android.content.Intent;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import master.pwr.whereami.R;
import master.pwr.whereami.activities.base.BaseActivity;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public class NfcActivity extends BaseActivity implements NfcAdapter.ReaderCallback
{
    private NfcAdapter mNfcAdapter;
    private View locateButton;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        providerName = LocationManager.PASSIVE_PROVIDER;

        locationProvider = locationManager.getProvider(providerName);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter != null && !mNfcAdapter.isEnabled())
        {
            Toast.makeText(this, getString(R.string.nfc_off), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
    }

    @Override
    protected boolean prepare()
    {
        boolean result = true;
        if (mNfcAdapter == null)
        {
            // Stop here, we definitely need NFC
            Toast.makeText(this.getApplicationContext(), "To urządzenie nie posiada modułu NFC.", Toast.LENGTH_LONG).show();
            this.finish();
            result = false;
        }

        if (!mNfcAdapter.isEnabled())
        {
            result = false;
            Toast.makeText(this, getString(R.string.nfc_off), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }

        return result;

    }

    @Override
    public void onClick(View v)
    {
        if (isWorking)
        {
            stopLocation();
            locateButton = null;
            isWorking = false;
        }
        else
        {
            if (prepare())
            {

                startLocation();
                locateButton = v;
                isWorking = true;
            }
        }

        setViewText(v, isWorking);
    }

    @Override
    protected void startLocation()
    {
        Toast.makeText(this, getString(R.string.explanation), Toast.LENGTH_SHORT).show();
        mNfcAdapter.enableReaderMode(
                this,
                this,
                NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B
                        | NfcAdapter.FLAG_READER_NFC_F | NfcAdapter.FLAG_READER_NFC_V
                        | NfcAdapter.FLAG_READER_NFC_BARCODE,
                null);

        dumpStats(true);
        measureTime(true);
    }

    @Override
    protected void stopLocation()
    {
        mNfcAdapter.disableReaderMode(this);
        dumpStats(false);
        measureTime(false);
    }

    @Override
    public void onTagDiscovered(Tag tag)
    {
        MifareClassic mifare = MifareClassic.get(tag);
        Ndef ndef = Ndef.get(tag);
        if (ndef == null)
        {
            // NDEF is not supported by this Tag.
            return;
        }

        NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        NdefRecord[] records = ndefMessage.getRecords();
        for (NdefRecord ndefRecord : records)
        {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT))
            {
                try
                {
                    Bundle args = new Bundle();
                    args.putString("data", readText(ndefRecord));
                    showMapFragment(args);
                }
                catch (UnsupportedEncodingException e)
                {
                    Log.e("WAI", "Unsupported Encoding", e);
                }
            }
        }

        onClick(locateButton);
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException
    {
        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ?
                "UTF-8" :
                "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }
}