package master.pwr.whereami.activities;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;

import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.fragments.MethodControllerFragment;
import master.pwr.whereami.fragments.MainFragment;
import master.pwr.whereami.R;


/**
 * An activity representing a list of LocationStartegies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link MethodControllerActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link MainFragment} and the item details
 * (if present) is a {@link MethodControllerFragment}.
 * <p/>
 * This activity also implements the required
 * {@link MainFragment.Callbacks} interface
 * to listen for item selections.
 */
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
        Class cls = MethodControllerActivity.class;
        switch ( LocationStrategyType.getByValue(id))
        {
            case GPS:
                cls = GpsActivity.class;
                break;
            case GSM:
                cls = QrCodeActivity.class;
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
        }

        Intent detailIntent = new Intent(this, cls);
        detailIntent.putExtra(MethodControllerFragment.ARG_ITEM_ID, id);
        startActivity(detailIntent);
    }
}
