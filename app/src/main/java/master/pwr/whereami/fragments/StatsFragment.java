package master.pwr.whereami.fragments;

import android.app.ListFragment;
import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

import master.pwr.whereami.adapters.StatsAdapter;
import master.pwr.whereami.models.Stats;

public class StatsFragment extends ListFragment
{
    public static final String TAG = "Stats";
    private static final String STATS_ARG = StatsFragment.class.getName();

    StatsAdapter adapter;

    public StatsFragment()
    {
    }

    public static StatsFragment newInstance(List<Stats> stats)
    {
        StatsFragment sf = new StatsFragment();
        sf.setArguments(getArgs(stats));
        return sf;
    }

    public static Bundle getArgs(List<Stats> stats)
    {
        Bundle args = new Bundle();
        args.putSerializable(STATS_ARG, (Serializable) stats);
        return args;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (adapter == null && getArguments() != null)
        {
            adapter = new StatsAdapter(getActivity());
        }

        Bundle args = getArguments();
        if (args != null)
        {
            adapter.clear();
            adapter.addRange((List<Stats>) args.getSerializable(STATS_ARG));
        }

        setListAdapter(adapter);
    }


}
