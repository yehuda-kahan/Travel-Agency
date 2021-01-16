package com.example.travelbrokerage.data.repositories;


import android.util.Log;

import androidx.lifecycle.LiveData;
import com.example.travelbrokerage.data.models.IHistoryDataSource;
import com.example.travelbrokerage.data.models.ITravelDataSource;
import com.example.travelbrokerage.data.models.HistoryDataSource;
import com.example.travelbrokerage.data.models.Travel;
import com.example.travelbrokerage.data.models.TravelFirebaseDataSource;
import com.example.travelbrokerage.util.MyApplication;
import java.util.ArrayList;
import java.util.List;

public class TravelRepository implements ITravelRepository {

    private final ITravelDataSource travelDataSource;
    private final IHistoryDataSource historyDataSource;

    private NotifyToTravelListListener notifyToTravelListListenerRepo;

    private List<Travel> travelList = new ArrayList<>();

    public TravelRepository() {

        travelDataSource = TravelFirebaseDataSource.getInstance();
        historyDataSource = new HistoryDataSource(MyApplication.getAppContext());

        ITravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new ITravelDataSource.NotifyToTravelListListener() {
            @Override
            public void onTravelsChanged() {
                travelList = travelDataSource.getAllTravels();

                // Entering the updating data to the ROOM Database
                historyDataSource.clearTable();
                historyDataSource.addTravel(travelList);
                //Notifies viewModel of a change in the database
                if (notifyToTravelListListenerRepo != null) {
                    notifyToTravelListListenerRepo.onTravelsChanged();
                }
            }
        };

        travelDataSource.setNotifyToTravelListListener(notifyToTravelListListener);
    }

    @Override
    public void setNotifyToTravelListListener(NotifyToTravelListListener l) {
        notifyToTravelListListenerRepo = l;
    }

    @Override
    public void addTravel(Travel travel) {
        travelDataSource.addTravel(travel);
    }

    @Override
    public void updateTravel(Travel travel) {
        travelDataSource.updateTravel(travel);
    }

    @Override
    public LiveData<List<Travel>> getAllTravels() {
        //return travelList;
        return historyDataSource.getTravels();
    }

    @Override
    public LiveData<Boolean> getIsSuccess() {
        return travelDataSource.getIsSuccess();
    }

    @Override
    public LiveData<List<Travel>> loadData() {
        return historyDataSource.loadData();
    }
}
