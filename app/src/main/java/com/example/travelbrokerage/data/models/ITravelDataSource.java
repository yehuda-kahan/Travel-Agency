package com.example.travelbrokerage.data.models;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public interface ITravelDataSource {

    void addTravel(Travel travel);

    void updateTravel(Travel travel);

    List<Travel> getAllTravels();

    MutableLiveData<Boolean> getIsSuccess();

    interface NotifyToTravelListListener {
        void onTravelsChanged();
    }
    void setNotifyToTravelListListener(NotifyToTravelListListener l);
  }
