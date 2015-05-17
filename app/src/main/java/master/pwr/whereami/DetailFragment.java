package master.pwr.whereami;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import master.pwr.whereami.enums.LocationStrategyType;
import master.pwr.whereami.interfaces.LocationStrategy;
import master.pwr.whereami.tools.LocatorFactory;

/**
 * A fragment representing a single LocationStrategy detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link DetailActivity}
 * on handsets.
 */
public class DetailFragment extends Fragment
{
    private LocationStrategy mItem;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailFragment()
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_locationstartegy_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null)
        {
            ((TextView) rootView.findViewById(R.id.location_strategy_detail)).setText(mItem.getName());
            rootView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    mItem.localize();
                    // mItem.retrieveStats();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
