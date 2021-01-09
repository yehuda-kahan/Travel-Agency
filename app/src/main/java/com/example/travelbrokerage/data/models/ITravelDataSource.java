package com.example.travelbrokerage.data.models;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public interface ITravelDataSource {

    void addTravel(Travel travel);

    void updateTravel(Travel travel);

    List<Travel> getAllTravels();

    MutableLiveData<Boolean> getIsSuccess();

    List<Travel> loadData();

    interface NotifyToTravelListListener {
        void onTravelsChanged();
    }
    void setNotifyToTravelListListener(NotifyToTravelListListener l);
  }
