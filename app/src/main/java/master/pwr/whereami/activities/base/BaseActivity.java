package master.pwr.whereami.activities.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import master.pwr.whereami.R;
import master.pwr.whereami.fragments.CustomMapFragment;
import master.pwr.whereami.fragments.StatsFragment;
import master.pwr.whereami.models.MapUpdate;
import master.pwr.whereami.models.Stats;
import master.pwr.whereami.tools.BatteryStatsReader;
import master.pwr.whereami.tools.StatDumper;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-19.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener
{
    protected static final float MIN_DISTANCE = 0.0f;
    protected static final int MAX_ATTEMPTS = 25;

    protected long executionTime;
    protected int attemptCounter;
    protected int interval;
    protected LocationManager locationManager;
    protected String providerName;
    protected Location location;
    protected boolean isWorking;
    protected View locateButton;
    private List<Stats> statsList;
    private float accuracy;
    private BatteryStatsReader batteryStatsReader;
    private int activityLayoutId;
    private CustomMapFragment mapFragment;
    private View.OnClickListener onMapButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            showMapFragment(null);
        }
    };
    private View.OnClickListener onStatButtonClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            showStats();
        }
    };

    public BaseActivity()
    {
        this(R.layout.activity_location_template, 10.0f);
    }

    public BaseActivity(float defaultAccuracy)
    {
        this(R.layout.activity_location_template, defaultAccuracy);
    }

    public BaseActivity(int activityLayoutId, float defaultAccuracy)
    {
        this.activityLayoutId = activityLayoutId;
        interval = 3;
        accuracy = defaultAccuracy;

        statsList = new ArrayList<>(4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(activityLayoutId);
        findViewById(R.id.button_1).setOnClickListener(onMapButtonClickListener);
        findViewById(R.id.button_2).setOnClickListener(onStatButtonClickListener);
        showMapFragment(null);
        setupSliders();

        batteryStatsReader = BatteryStatsReader.getInstance(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View v)
    {
        if (isWorking)
        {
            stopLocation();
            dumpStats();
            showStats();
        }
        else
        {
            if (prepare())
            {
                startLocation();
            }
        }

        if (v != null)
        {
            setViewText(v, isWorking);
            locateButton = v;
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(batteryStatsReader, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        unregisterReceiver(batteryStatsReader);
        if (statsList.size() > 0)
        {
            StatDumper.getInstance().dumpLog(statsList, getName(), "[Emergency Save]");
            statsList.clear();
        }
    }

    protected abstract boolean prepare();

    protected abstract void startLocation();

    protected abstract void stopLocation();

    private synchronized void isLocationSufficient(Location location)
    {
        if (++attemptCounter == MAX_ATTEMPTS || (location != null && (location.getAccuracy() - accuracy < 1.0f)))
        {
            if (isWorking) onClick(locateButton);
        }
    }

    private String getName()
    {
        return getTitle().toString();
    }

    private void setViewText(View view, boolean isWorking)
    {
        ((TextView) view).setText(isWorking ? R.string.abort_location : R.string.get_location);
    }

    public void setLocation(Location newLocation)
    {
        location = newLocation;
    }

    protected synchronized Stats retrieveStats()
    {
        Stats stats = new Stats();

        stats.setMethodName(getName());
        stats.setExecutionTime(executionTime == 0 ? 0 : System.currentTimeMillis() - executionTime);
        stats.setAttempt(attemptCounter);
        stats.setInterval(interval);
        if (location != null)
        {
            stats.setAccuracy(location.getAccuracy());
            stats.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }

        getBatteryStats(stats);
        return stats;
    }

    private void getBatteryStats(Stats batteryStats)
    {
        batteryStats.setBatteryLevel(batteryStatsReader.getLevel());
        batteryStats.setBatteryVoltage(batteryStatsReader.getVoltage());
    }

    protected void runStopwatch()
    {
        executionTime = isWorking ? System.currentTimeMillis() : 0;
    }

    private void dumpStats()
    {
        viewInputDialog();
    }

    public void collectStats()
    {
        isLocationSufficient(location);
        statsList.add(retrieveStats());
    }

    private void setupSliders()
    {
        final TextView intervalText = (TextView) findViewById(R.id.text);
        final TextView sufficientAccuracy = (TextView) findViewById(R.id.text_1);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                int id = seekBar.getId();
                if (id == R.id.interval)
                {
                    BaseActivity.this.interval = progress;
                    intervalText.setText(progress + "");
                }
                else if (id == R.id.accuracy)
                {
                    BaseActivity.this.accuracy = (float) progress;
                    sufficientAccuracy.setText(progress + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        };

        SeekBar slider = (SeekBar) findViewById(R.id.interval),
                accuracySlider = (SeekBar) findViewById(R.id.accuracy);
        slider.setOnSeekBarChangeListener(listener);
        accuracySlider.setOnSeekBarChangeListener(listener);

        slider.setProgress(interval);
        accuracySlider.setProgress((int) accuracy);

        intervalText.setText(interval + "");
        sufficientAccuracy.setText(accuracy + "");
    }

    private CustomMapFragment getMapFragment()
    {
        if (mapFragment == null)
        {
            mapFragment = (CustomMapFragment) getFragmentManager().findFragmentByTag(CustomMapFragment.TAG);
        }

        if (mapFragment == null)
        {
            mapFragment = CustomMapFragment.newInstance();
            mapFragment.setOnLocateButtonListener(this);
        }

        return mapFragment;
    }

    private void showMapFragment(Bundle args)
    {
        Fragment mf = getMapFragment();
        if (mf.isVisible()) return;

        if (args != null)
        {
            mf.setArguments(args);
        }

        getFragmentManager().beginTransaction()
                            .replace(R.id.inner_fragment_container, mf, CustomMapFragment.TAG)
                            .commit();
    }

    public void updateMap(MapUpdate update)
    {
        getMapFragment().updateMap(update);
    }

    protected void showStats()
    {
        if (statsList.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Brak statystyk", Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentManager fm = getFragmentManager();
        Fragment statsFragment = getFragmentManager().findFragmentByTag(StatsFragment.TAG);

        if (statsFragment == null)
        {
            statsFragment = StatsFragment.newInstance(statsList);
        }
        else
        {
            statsFragment.setArguments(StatsFragment.getArgs(statsList));
        }

        if (statsFragment.isVisible()) return;

        fm.beginTransaction()
          .replace(R.id.inner_fragment_container, statsFragment, StatsFragment.TAG)
          .commit();
    }

    private void viewInputDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.note_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editText);
        final Spinner inOut = (Spinner) promptsView.findViewById(R.id.spinner);
        final Spinner loc = (Spinner) promptsView.findViewById(R.id.spinner1);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Zapisz pomiar",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                String tag = String.format("[%s][%s][%s]",
                                        loc.getSelectedItem(),
                                        inOut.getSelectedItem(),
                                        userInput.getText().toString());
                                StatDumper.getInstance().dumpLog(statsList, getName(), tag);
                                statsList.clear();
                            }
                        })
                .setNegativeButton("Usuń pomiar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                        statsList.clear();
                    }
                })
                .create()
                .show();
    }

}
