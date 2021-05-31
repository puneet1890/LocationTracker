package com.example.locationtracker;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "places_table")
public class Place
{
    @PrimaryKey(autoGenerate = true)
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @ColumnInfo(name = "latitude")
    private final String latitude;

    @ColumnInfo(name = "longitude")
    private final String longitude;

    public Place(String latitude, String longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;
        Place place = (Place) o;
        return Objects.equals(getLatitude(), place.getLatitude()) &&
                Objects.equals(getLongitude(), place.getLongitude());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(getLatitude(), getLongitude());
    }

    @Override
    @NonNull
    public String toString()
    {
        return "Place{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
