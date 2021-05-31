package com.example.locationtracker;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LocationDao
{
    @Insert
    void insert(Place place);

    @Update
    void update(Place place);

    @Query("SELECT * FROM places_table")
    LiveData<List<Place>> getAllPlaces();

    @Query("DELETE FROM places_table")
    void deleteAll();
}