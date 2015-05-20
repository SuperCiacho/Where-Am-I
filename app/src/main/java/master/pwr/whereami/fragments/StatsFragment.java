package master.pwr.whereami.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.Serializable;
import java.util.List;

import master.pwr.whereami.adapters.StatsAdapter;
import master.pwr.whereami.models.Stats;

public class StatsFragment extends ListFragment
{
    private static final String STATS_ARG = StatsFragment.class.getName();
    public static final String TAG = "Stats";

    public static StatsFragment newInstance(List<Stats> stats)
    {
        StatsFragment sf = new StatsFragment();
        Bundle args = new Bundle();
        args.putSerializable(STATS_ARG, (Serializable) stats);
        sf.setArguments(args);
        return sf;
    }

    public StatsFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(getArguments() != null)
        {
            List<Stats> data = (List<Stats>) getArguments().getSerializable(STATS_ARG);
            setListAdapter(new StatsAdapter(data));
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

    }

}
