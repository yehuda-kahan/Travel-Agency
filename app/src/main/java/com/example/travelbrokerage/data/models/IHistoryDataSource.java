package com.example.travelbrokerage.data.models;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface IHistoryDataSource {
    void addTravel(Travel p);

    void addTravel(List<Travel> travelList);

    void updateTravel(Travel p);

    void deleteTravel(Travel p);

    void clearTable();

    LiveData<List<Travel>> getTravels();

    LiveData<Travel> getTravel(String id);

    LiveData<List<Travel>> loadData();
}
