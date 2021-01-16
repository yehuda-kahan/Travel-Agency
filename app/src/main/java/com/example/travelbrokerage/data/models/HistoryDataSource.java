package com.example.travelbrokerage.data.models;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class HistoryDataSource implements IHistoryDataSource {

    private final TravelDao travelDao;
    private LiveData<List<Travel>> allTravels;

    public HistoryDataSource(Context context) {
        RoomDataSource database = RoomDataSource.getInstance(context);
        travelDao = database.getTravelDao();
        //allTravels = travelDao.getAll(); // TODO why do this
    }

    public LiveData<List<Travel>> getTravels() {
        return travelDao.getAll();
    }

    public LiveData<Travel> getTravel(String id) {
        return travelDao.get(id);
    }

    public void addTravel(Travel p) {
        travelDao.insert(p);
    }

    public void updateTravel(Travel p) {
        travelDao.update(p);
    }

    public void deleteTravel(Travel p) {
        travelDao.delete(p);
    }

    public void clearTable() {
        travelDao.clear();
        //new DeleteAllTravelsAsyncTask(travelDao).execute();
    }

    @Override
    public LiveData<List<Travel>> loadData() {
        return travelDao.getAll();
    }

    public void addTravel(List<Travel> travelList) {
        travelDao.insert(travelList);
        //new InsertTravelAsyncTask(travelDao).execute(travelList);
    }

   /* public static class InsertTravelAsyncTask extends AsyncTask<List<Travel>, Void, Void> {

        private final TravelDao travelDao;

        public InsertTravelAsyncTask(TravelDao travelDao) {
            this.travelDao = travelDao;
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Travel>... travels) {
            travelDao.insert(travels[0]);
            return null;
        }
    }*/

   /* public static class DeleteAllTravelsAsyncTask extends AsyncTask<Void, Void, Void> {
        private TravelDao travelDao;

        public DeleteAllTravelsAsyncTask(TravelDao travelDao) {
            this.travelDao = travelDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            travelDao.clear();
            return null;
        }
    }*/
}

