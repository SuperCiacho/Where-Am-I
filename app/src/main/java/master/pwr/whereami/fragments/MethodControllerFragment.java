package master.pwr.whereami.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.MapFragment;

import master.pwr.whereami.R;
import master.pwr.whereami.activities.MethodControllerActivity;
import master.pwr.whereami.activities.MainActivity;
import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.interfaces.LocationStrategy;
import master.pwr.whereami.interfaces.StatsUpdater;
import master.pwr.whereami.models.Stats;
import master.pwr.whereami.tools.LocatorFactory;

/**
 * A fragment representing a single LocationStrategy detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link MethodControllerActivity}
 * on handsets.
 */
public class MethodControllerFragment extends Fragment implements StatsUpdater
{
    private LocationStrategy mItem;
    private Stats stats;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MethodControllerFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            mItem = LocatorFactory.getLocationStrategy(
                    getActivity(),
                    LocationStrategyType.getByValue(
                            getArguments().getInt(ARG_ITEM_ID)));
            mItem.setStatsUpdater(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_method_controller, container, false);

        getFragmentManager().beginTransaction().replace(R.id.inner_fragment_container, new MapFragment()).commit();

        if (mItem != null)
        {
            ((TextView) rootView.findViewById(R.id.location_strategy_detail)).setText(mItem.getName());
            rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mItem.dumpStats(true);
                    mItem.prepare();
                    mItem.localize(MethodControllerFragment.this);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mItem.stop();
    }

    @Override
    public void updateStats(Stats stats)
    {
        this.stats = stats;
        mItem.showMap(getFragmentManager());
        mItem.dumpStats(false);
    }


}
