package com.example.travelbrokerage.data.repositories;

import androidx.lifecycle.LiveData;
import com.example.travelbrokerage.data.models.ITravelDataSource;
import com.example.travelbrokerage.data.models.Travel;

import java.util.List;

public interface ITravelRepository {

    void addTravel(Travel travel);

    void updateTravel(Travel travel);

    LiveData<List<Travel>> getAllTravels();

    LiveData<Boolean> getIsSuccess();

    interface NotifyToTravelListListener {
        void onTravelsChanged();
    }
    void setNotifyToTravelListListener(NotifyToTravelListListener l);
}
