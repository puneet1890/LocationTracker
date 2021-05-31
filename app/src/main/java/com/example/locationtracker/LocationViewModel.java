package com.example.locationtracker;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class LocationViewModel extends AndroidViewModel
{
    public LocationRepository locationRepository;
    public LiveData<List<Place>> placesList;
   //public List<Place> placesList;

    public static final String TAG = "Places_ViewModel";

    public LocationViewModel(@NonNull Application application)
    {
        super(application);

        locationRepository = new LocationRepository(application);
        placesList = locationRepository.getAllPlaces();
        // placesList = locationRepository.getAllPlaces();

        Log.d(TAG,"User_ViewModel object created");
    }

    public void insert(Place place)
    {
        locationRepository.insert(place);
        Log.d(TAG,"Places_ViewModel inserting to Repository");
    }

    public void update(Place place)
    {
        locationRepository.update(place);
        Log.d(TAG,"Place_ViewModel updating to Repository");
    }

    public void deleteAll()
    {
        locationRepository.deleteAll();
        Log.d(TAG,"Place_ViewModel updating to Repository");
    }

    public LiveData<List<Place>> getAllPlaces()
    {
        Log.d(TAG,"Place_ViewModel retrieving all users from Repository");
        return placesList;
    }


/*    public List<Place> getAllPlaces()
    {
        Log.d(TAG,"Place_ViewModel retrieving all users from Repository");
        return placesList;
    }

 */
}
