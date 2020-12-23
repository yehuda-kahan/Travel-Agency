package com.example.travelbrokerage.data.models;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TravelDao {

    @Query("select * from travels")
    LiveData<List<Travel>> getAll(); // TODO why liveData

    @Query("select * from travels where travelId=:id")
    LiveData<Travel> get(String id); // TODO why liveData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Travel travel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Travel> travels);

    @Update
   void update(Travel travel);

    @Delete
    void delete(Travel... travels);

    @Query("delete from travels")
    void clear();

}
