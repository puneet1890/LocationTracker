package com.example.locationtracker;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class LocationRepository
{
    public static final String TAG = "LocationRepository";
    private final LiveData<List<Place>> placesList;

    // private List<Place> placesList;
    private final LocationDao locationDao;

    public LocationRepository(Application application)
    {
        LocationDatabase locationDatabase = LocationDatabase.getInstance(application);
        locationDao = locationDatabase.getlocationDao();

        placesList = locationDao.getAllPlaces();
        Log.d(TAG,"Location Repository created");
    }

    public void deleteAll()
    {
        new DeleteAllAsyncTask(locationDao).execute();
        Log.d(TAG,"Place details deleted from table");
    }

    public void insert(Place place)
    {
        new InsertAsyncTask(locationDao).execute(place);
        Log.d(TAG,"Place details inserted into table");
    }

    public void update(Place place)
    {
        new UpdateAsyncTask(locationDao).execute(place);
        Log.d(TAG,"Place details updated in the table");
    }

/*    public List<Place> getAllPlaces()
    {
        Log.d(TAG,"Retriving all places");
        return placesList;
    }
*/

    public LiveData<List<Place>> getAllPlaces()
    {
        Log.d(TAG,"Retriving all places");
        return placesList;
    }

    private static class InsertAsyncTask extends AsyncTask<Place,Void,Void>
    {
        LocationDao locationDao;

        public InsertAsyncTask(LocationDao locationDao)
        {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(Place... places)
        {
            locationDao.insert(places[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Place,Void,Void>
    {
        LocationDao locationDao;

        public UpdateAsyncTask(LocationDao locationDao)
        {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(Place... places)
        {
            locationDao.update(places[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>
    {
        LocationDao locationDao;

        public DeleteAllAsyncTask(LocationDao locationDao)
        {
            this.locationDao = locationDao;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            locationDao.deleteAll();
            return null;
        }
    }
}
