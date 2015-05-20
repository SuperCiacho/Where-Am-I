package master.pwr.whereami.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import master.pwr.whereami.R;
import master.pwr.whereami.activities.base.BaseActivity;

public class FusedLocationActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_template);
    }

    @Override
    protected boolean prepare()
    {
        return false;
    }

    @Override
    protected void startLocation()
    {

    }

    @Override
    protected void stopLocation()
    {

    }

    @Override
    public void onClick(View v)
    {

    }
}
