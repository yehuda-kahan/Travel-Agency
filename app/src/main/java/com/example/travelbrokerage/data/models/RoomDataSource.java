package com.example.travelbrokerage.data.models;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Travel.class}, version = 1, exportSchema = false)
@TypeConverters({Travel.UserLocationListConverter.class, Travel.RequestTypeConverter.class, Travel.DateConverter.class, Travel.UserLocationConverter.class, Travel.CompanyConverter.class})
public abstract class RoomDataSource extends RoomDatabase {

    public static final String DATABASE_NAME = "travelsDB";
    private static RoomDataSource database;

    public static RoomDataSource getInstance(Context context) {
        if (database == null)
            database = Room.databaseBuilder(context, RoomDataSource.class, DATABASE_NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        return database;
    }

    public abstract TravelDao getTravelDao();
}
