package com.example.locationtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener
{
    Button requestLocation, removeLocation;
    MyBackgroundService mService = null;
    boolean mBound = false;

    LocationViewModel locationViewModel;

    ListView placesListView;
    ArrayAdapter<Place> adapter;
    //LiveData<List<Place>> placeList;
    List<Place> placesList;

    private ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MyBackgroundService.LocalBinder binder = (MyBackgroundService.LocalBinder)service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesListView = findViewById(R.id.placesListView);
        placesList = new ArrayList<>();
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        locationViewModel.deleteAll();
/*
        placesList = new ArrayList<>();

        placesList.addAll(locationViewModel.getAllPlaces()) ;

        adapter = new ArrayAdapter<Place>(this, android.R.layout.simple_list_item_1, placesList);
        placesListView.setAdapter(adapter);
*/

        Dexter.withActivity(this).withPermissions(Arrays.asList(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)).withListener(new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport)
            {
                requestLocation = findViewById(R.id.btn_request_location_updates);
                removeLocation = findViewById(R.id.btn_remove_location_updates);

                requestLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        mService.requestLocationUpdates();
                    }
                });

                removeLocation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mService.removeLocationUpdates();
                    }
                });

                setButtonState(Common.requestingLocationUpdates(MainActivity.this));
                bindService(new Intent(MainActivity.this,MyBackgroundService.class), mServiceConnection, Context.BIND_AUTO_CREATE);

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();

        adapter = new ArrayAdapter<Place>(this, android.R.layout.simple_list_item_1, placesList);
        placesListView.setAdapter(adapter);

        locationViewModel.getAllPlaces().observe(this, new Observer<List<Place>>()
        {
            @Override
            public void onChanged(List<Place> places)
            {
                //placesList = places;
                placesList.addAll(places);
                adapter.notifyDataSetChanged();

                //placesListView.setAdapter(adapter);
            }
        });

        updateLocationsToListView();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop()
    {
        if(mBound)
        {
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
    {
        if(key.equals(Common.KEY_REQUESTING_LOCATION_UPDATES))
        {
            setButtonState(sharedPreferences.getBoolean(Common.KEY_REQUESTING_LOCATION_UPDATES, false));
        }
    }

    private void setButtonState(boolean isRequestEnable)
    {
        if(isRequestEnable)
        {
            requestLocation.setEnabled(false);
            removeLocation.setEnabled(true);
        }
        else
        {
            requestLocation.setEnabled(true);
            removeLocation.setEnabled(false);
        }
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onListenLocation(SendLocationToActivity event)
    {
        if(event != null)
        {
            final String data = new StringBuilder()
                        .append(event.getLocation().getLatitude())
                        .append(",")
                        .append(event.getLocation().getLongitude())
                        .toString();

            Toast.makeText(getApplicationContext(),data,Toast.LENGTH_LONG).show();

            String latitude = String.valueOf(event.getLocation().getLatitude());
            String longitude = String.valueOf(event.getLocation().getLongitude());

            locationViewModel.insert(new Place(latitude,longitude));
        }
    }

    public void updateLocationsToListView()
    {
        // Update the listView every 30mins
        final Handler handler = new Handler();
        handler.postDelayed( new Runnable()
        {
            @Override
            public void run()
            {
                adapter.notifyDataSetChanged();
                handler.postDelayed( this,  60 * 1000 );
            }
        }, 60 * 1000 );

    }
}
