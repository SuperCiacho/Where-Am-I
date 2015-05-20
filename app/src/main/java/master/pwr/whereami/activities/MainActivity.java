package master.pwr.whereami.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.fragments.MainFragment;
import master.pwr.whereami.R;

public class MainActivity extends Activity implements MainFragment.Callbacks
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationstartegy_list);

        if (findViewById(R.id.fragment_container) != null)
        {
            ((MainFragment) getFragmentManager()
                    .findFragmentById(R.id.locationstartegy_list))
                    .setActivateOnItemClick(true);
        }
    }

    /**
     * Callback method from {@link MainFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int id)
    {
        Class cls;
        switch ( LocationStrategyType.getByValue(id))
        {
            case GPS:
                cls = GpsActivity.class;
                break;
            case GSM:
                cls = GsmActivity.class;
                break;
            case NFC:
                cls = NfcActivity.class;
                break;
            case QR_CODE:
                cls = QrCodeActivity.class;
                break;
            case WIFI:
                cls = WifiActivity.class;
                break;
            case DEAD_RECKONING:
                cls = DeadReckoningActivity.class;
                break;
            default:
                throw new IllegalArgumentException("Provided location strategy type is not supported and never will be!");
        }

        Intent detailIntent = new Intent(this, cls);
        detailIntent.putExtra("id", id);
        startActivity(detailIntent);
    }
}
