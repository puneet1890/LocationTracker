package com.example.locationtracker;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Place.class},version = 1)
public abstract class LocationDatabase extends RoomDatabase
{
    public static final String TAG = "Location_Database";

    public static LocationDatabase instance;

    public abstract LocationDao getlocationDao();

    public static synchronized LocationDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context, LocationDatabase.class, "places_table")
                    .fallbackToDestructiveMigration()
                    .build();

            Log.d(TAG,"Places_Database created");
        }
        return instance;
    }

}
