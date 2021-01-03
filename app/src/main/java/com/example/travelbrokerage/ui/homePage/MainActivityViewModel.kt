package com.example.travelbrokerage.ui.homePage

import com.example.travelbrokerage.ui.homePage.MainActivity
import android.app.Application
import androidx.lifecycle.*
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.data.repositories.ITravelRepository
import com.example.travelbrokerage.data.repositories.TravelRepository


// Represent the View Model of AddTravelActivity
class MainActivityViewModel : ViewModel() {

    private var travelsList : List<Travel> = ArrayList<Travel>()
    private var costumerList : MutableLiveData<List<Travel>> = MutableLiveData()
    private var companyList : MutableLiveData<List<Travel>> = MutableLiveData()
    private var historyList : MutableLiveData<List<Travel>> = MutableLiveData()

    private var  travelRepo : ITravelRepository = TravelRepository()

    init {
        travelRepo.setNotifyToTravelListListener(object : ITravelRepository.NotifyToTravelListListener {
            override fun onTravelsChanged() {
                travelsList = travelRepo.allTravels
                costumerList.value = travelsList
            }
        })
    }

    //
    fun getCostumerTravels() : LiveData<List<Travel>> = costumerList

    fun loadCostumerList(){
        costumerList.value = travelsList
    }

    // Add travel obj to the DataBase
    fun addTravel(travel: Travel) {
        travelRepo.addTravel(travel)
    }

    // Get the boolean value Which indicates whether the value
    // was successfully inserted into the database
    fun getIsSuccess(): LiveData<Boolean> {
        return travelRepo.isSuccess
    }

    fun getUserList(userMail: String): List<Travel> {
        val tempList = ArrayList<Travel>()
        for (travel in travelsList){
            if (travel.clientEmail == userMail){
                tempList.add(travel)
            }
        }
        return tempList
    }
}