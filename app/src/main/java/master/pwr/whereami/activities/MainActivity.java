package master.pwr.whereami.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import master.pwr.whereami.R;
import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.fragments.MainFragment;

public class MainActivity extends Activity implements MainFragment.Callbacks
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationstartegy_list);
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
            case HIGH_ACCURACY:
                cls = HighAccuracyActivity.class;
                break;
            case NETWORK:
                cls = NetworkLocationActivity.class;
                break;
            case GPS_ONLY:
                cls = GpsOnlyActivity.class;
                break;
            case FUSED_LOCATION:
                cls = FusedLocationActivity.class;
                break;
            default:
                throw new IllegalArgumentException("Provided location strategy type is not supported and never will be!");
        }

        Intent detailIntent = new Intent(this, cls);
        detailIntent.putExtra("id", id);
        startActivity(detailIntent);
    }
}
