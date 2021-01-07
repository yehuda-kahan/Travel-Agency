package com.example.travelbrokerage.ui.homePage

import com.example.travelbrokerage.ui.homePage.MainActivity
import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.*
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.data.models.Travel.RequestType
import com.example.travelbrokerage.data.repositories.ITravelRepository
import com.example.travelbrokerage.data.repositories.TravelRepository
import com.example.travelbrokerage.util.MyApplication
import com.google.firebase.auth.FirebaseAuth


// Represent the View Model of AddTravelActivity
class MainActivityViewModel : ViewModel() {

    private var travelsList: List<Travel> = ArrayList<Travel>()
    private var costumerList: MutableLiveData<List<Travel>> = MutableLiveData()
    private var companyList: MutableLiveData<List<Travel>> = MutableLiveData()
    private var historyList: MutableLiveData<List<Travel>> = MutableLiveData()
    private val sharedPreferences = MyApplication.getAppContext().getSharedPreferences("USER", MODE_PRIVATE)
    private val userMail = sharedPreferences.getString("Mail", "")

    private var travelRepo: ITravelRepository = TravelRepository()

    init {
        travelRepo.setNotifyToTravelListListener {
            travelsList = travelRepo.allTravels
            costumerList.value = getUserList(userMail!!)
            //companyList.value = getCompanyList(userMail!!)
            //historyList.value = getHistoryList(userMail!!)
        }
    }

    //
    fun getCostumerTravels(): LiveData<List<Travel>> = costumerList

    fun loadCostumerList() {
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

    private fun getUserList(userMail: String): List<Travel> {
        val tempList = ArrayList<Travel>()
        for (travel in travelsList) {
            if (travel.clientEmail == userMail && (travel.requestType != RequestType.CLOSE &&
                        travel.requestType != RequestType.PAYMENT)
            ) {
                tempList.add(travel)
            }
        }
        return tempList
    }

    fun updateTravel(currentItem: Travel) {
        travelRepo.updateTravel(currentItem)
    }
}