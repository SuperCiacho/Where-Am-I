package master.pwr.whereami.models.locators;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.provider.Settings;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import master.pwr.whereami.R;
import master.pwr.whereami.enums.LocationStrategyType;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-16.
 */
public class NFCLocator extends BaseLocator implements NfcAdapter.ReaderCallback
{
    private NfcAdapter mNfcAdapter;

    public NFCLocator(Context context)
    {
        super(context);
    }

    @Override
    protected void setup()
    {
        providerName = LocationManager.PASSIVE_PROVIDER;
        locationProvider = locationManager.getProvider(providerName);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (mNfcAdapter != null && !mNfcAdapter.isEnabled())
        {
            Toast.makeText(context, "Moduł jest wyłączony. Włącz go.", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }

    }

    @Override
    public String getName()
    {
        return LocationStrategyType.NFC.getDescription();
    }

    @Override
    public void localize(Fragment fragment)
    {
        super.localize(fragment);
        context = fragment.getActivity();
        TextView infoTextView = (TextView) fragment.getView().findViewById(R.id.text);

        if (mNfcAdapter == null)
        {
            // Stop here, we definitely need NFC
            Toast.makeText(context, "To urządzenie nie posiada modułu NFC.", Toast.LENGTH_LONG).show();
            fragment.getActivity().finish();
            return;
        }

        if (!mNfcAdapter.isEnabled())
        {
            infoTextView.setText("NFC jest wyłączone.");
        }
        else
        {
            infoTextView.setText(R.string.explanation);
        }

        mNfcAdapter.enableReaderMode(
                fragment.getActivity(),
                this,
                NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                null);
    }
    
    @Override
    public void stop()
    {
        if(mNfcAdapter != null)
        {
            mNfcAdapter.disableReaderMode((Activity) context);
        }
    }

    @Override
    public void onTagDiscovered(Tag tag)
    {
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
                    Intent i = new Intent();
                    i.putExtra("data",readText(ndefRecord));
                    update(i);
                }
                catch (UnsupportedEncodingException e)
                {
                    Log.e("WAI", "Unsupported Encoding", e);
                }
            }
        }

        mNfcAdapter.disableReaderMode((Activity) context);
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException
    {
        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }
}
