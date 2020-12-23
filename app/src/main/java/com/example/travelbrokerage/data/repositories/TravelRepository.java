package com.example.travelbrokerage.data.repositories;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.travelbrokerage.data.models.IHistoryDataSource;
import com.example.travelbrokerage.data.models.ITravelDataSource;
import com.example.travelbrokerage.data.models.HistoryDataSource;
import com.example.travelbrokerage.data.models.Travel;
import com.example.travelbrokerage.data.models.TravelFirebaseDataSource;

import java.util.List;

public class TravelRepository implements ITravelRepository {

    private final ITravelDataSource  travelDataSource;
    private final IHistoryDataSource historyDataSource;

    private final MutableLiveData<List<Travel>> mutableLiveData = new MutableLiveData<>();

    /*private static TravelRepository instance;
    public static TravelRepository getInstance(Application application) {
        if (instance == null)
            instance = new TravelRepository(application);
        return instance;
    }*/

    private TravelRepository(Application application) {
        travelDataSource = TravelFirebaseDataSource.getInstance();
        historyDataSource = new HistoryDataSource(application.getApplicationContext());

        ITravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new ITravelDataSource.NotifyToTravelListListener() {
            @Override
            public void onTravelsChanged() {
                List<Travel> travelList = travelDataSource.getAllTravels();
                mutableLiveData.setValue(travelList);

                historyDataSource.clearTable();
                historyDataSource.addTravel(travelList);
            }
        };

        travelDataSource.setNotifyToTravelListListener(notifyToTravelListListener);
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
    public MutableLiveData<List<Travel>> getAllTravels() {
        return mutableLiveData;
    }

    @Override
    public MutableLiveData<Boolean> getIsSuccess() {
        return travelDataSource.getIsSuccess();
    }
}
