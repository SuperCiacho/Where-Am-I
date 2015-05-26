package master.pwr.whereami.activities;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import master.pwr.whereami.R;
import master.pwr.whereami.activities.base.BaseActivity;
import master.pwr.whereami.models.MapUpdate;

public class FusedLocationActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks,
                                                                   GoogleApiClient.OnConnectionFailedListener,
                                                                   LocationListener
{
    private FusedLocationProviderApi fusedLocationProviderApi;
    private GoogleApiClient googleApiClient;

    private int priority;

    private LocationRequest locationRequest;

    public FusedLocationActivity()
    {
        super(R.layout.activity_fused_location);

        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setupSpinner();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    private void setupSpinner()
    {
        List<String> data = new ArrayList<>(4);
        data.add("Balans");
        data.add("Wysoka dokładność");
        data.add("Niski pobór energii");
        data.add("Bez dodatkowego poboru energii");
        Spinner prioritySelector = (Spinner) findViewById(R.id.spinner);
        prioritySelector.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, data));

        prioritySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                switch (position)
                {
                    default:
                    case 0:
                        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
                        break;
                    case 1:
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
                        break;
                    case 2:
                        priority = LocationRequest.PRIORITY_LOW_POWER;
                        break;
                    case 3:
                        priority = LocationRequest.PRIORITY_NO_POWER;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
    }

    @Override
    protected boolean prepare()
    {
        attemptCounter = 0;

        if (googleApiClient.isConnecting())
        {
            Toast.makeText(this, "Google Api Client łączy się z siecią...", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!googleApiClient.isConnected())
        {
            Toast.makeText(this, "Nawiązywanie połączenia", Toast.LENGTH_SHORT).show();
            googleApiClient.connect();
            return false;
        }

        if (locationRequest == null)
        {
            locationRequest = new LocationRequest();
            locationRequest.setNumUpdates(MAX_ATTEMPTS);
            locationRequest.setSmallestDisplacement(0.0f);
        }

        locationRequest.setFastestInterval(1);
        locationRequest.setInterval(TimeUnit.SECONDS.toMillis(interval));
        locationRequest.setPriority(priority);

        return true;
    }

    @Override
    protected void startLocation()
    {
        isWorking = true;
        fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        collectStats();
        runStopwatch();

    }

    @Override
    protected void stopLocation()
    {
        fusedLocationProviderApi.removeLocationUpdates(googleApiClient, this);
        isWorking = false;
        collectStats();
        runStopwatch();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Toast.makeText(this, "Google Api Client nawiązał połączenie.", Toast.LENGTH_SHORT).show();
        onClick(locateButton);
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    @Override
    public void onLocationChanged(final Location location)
    {
        this.location = location;

        collectStats();

        updateMap(new MapUpdate(location));
    }
}
