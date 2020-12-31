package com.example.travelbrokerage.data.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.travelbrokerage.data.models.IHistoryDataSource;
import com.example.travelbrokerage.data.models.ITravelDataSource;
import com.example.travelbrokerage.data.models.HistoryDataSource;
import com.example.travelbrokerage.data.models.Travel;
import com.example.travelbrokerage.data.models.TravelFirebaseDataSource;
import com.example.travelbrokerage.util.MyApplication;

import java.util.List;

public class TravelRepository implements ITravelRepository {

    private final ITravelDataSource  travelDataSource;
    private final IHistoryDataSource historyDataSource;

    private NotifyToTravelListListener notifyToTravelListListenerRepo;

    private MutableLiveData<List<Travel>> allTravels = new MutableLiveData<>() ;

    //private final MutableLiveData<List<Travel>> mutableLiveData = new MutableLiveData<>();

    /*private static TravelRepository instance;
    public static TravelRepository getInstance(Application application) {
        if (instance == null)
            instance = new TravelRepository(application);
        return instance;
    }*/

    public TravelRepository() {

        travelDataSource = TravelFirebaseDataSource.getInstance();
        historyDataSource = new HistoryDataSource(MyApplication.getAppContext());

        ITravelDataSource.NotifyToTravelListListener notifyToTravelListListener = new ITravelDataSource.NotifyToTravelListListener() {
            @Override
            public void onTravelsChanged() {
                List<Travel> travelList = travelDataSource.getAllTravels();
                //mutableLiveData.setValue(travelList);
                allTravels.setValue(travelList);
               /* historyDataSource.clearTable();
                historyDataSource.addTravel(travelList);*/
                //Notifies viewModel of a change in the database
                if (notifyToTravelListListenerRepo != null){
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
    public LiveData<List<Travel>> getAllTravels()  {
         return allTravels;
    }

    @Override
    public LiveData<Boolean> getIsSuccess() {
        return travelDataSource.getIsSuccess();
    }
}
