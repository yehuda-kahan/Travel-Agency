package com.example.travelbrokerage.data.repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.travelbrokerage.data.models.Travel;

import java.util.List;

public interface ITravelRepository {

    void addTravel(Travel travel);

    void updateTravel(Travel travel);

    MutableLiveData<List<Travel>> getAllTravels();

    MutableLiveData<Boolean> getIsSuccess();
}
