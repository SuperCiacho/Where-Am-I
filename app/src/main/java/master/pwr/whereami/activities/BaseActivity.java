package master.pwr.whereami.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import master.pwr.whereami.R;
import master.pwr.whereami.fragments.CustomMapFragment;
import master.pwr.whereami.fragments.StatsFragment;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_template);

        findViewById(R.id.button_1).setOnClickListener(onMapButtonClickListener);
        findViewById(R.id.button_2).setOnClickListener(onStatButtonClickListener);
    }

    protected View.OnClickListener onMapButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment statsFragment = getFragmentManager().findFragmentByTag(StatsFragment.TAG);
            Fragment mapFragment = getFragmentManager().findFragmentByTag(CustomMapFragment.TAG);
            if(statsFragment != null)
            {
                ft.hide(statsFragment);
            }
            if(mapFragment == null)
            {
                mapFragment = CustomMapFragment.newInstance();
                ((CustomMapFragment)mapFragment).setOnLocateButtonListener(BaseActivity.this);
                ft.add(R.id.inner_fragment_container, mapFragment, CustomMapFragment.TAG);
            }
            else
            {
                ft.show(mapFragment);
            }

            ft.commit();
        }
    };

    protected View.OnClickListener onStatButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment statsFragment = getFragmentManager().findFragmentByTag(StatsFragment.TAG);
            Fragment mapFragment = getFragmentManager().findFragmentByTag(CustomMapFragment.TAG);
            if(mapFragment != null)
            {
                ft.hide(mapFragment);
            }
            if(statsFragment == null)
            {
                statsFragment = StatsFragment.newInstance(null);
                ft.add(R.id.inner_fragment_container, statsFragment, CustomMapFragment.TAG);
            }
            else
            {
                ft.show(mapFragment);
            }

            ft.commit();
        }
    };
}
