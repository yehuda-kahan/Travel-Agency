package com.example.travelbrokerage.ui.homePage

import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.*
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.data.models.Travel.RequestType
import com.example.travelbrokerage.data.repositories.ITravelRepository
import com.example.travelbrokerage.data.repositories.TravelRepository
import com.example.travelbrokerage.util.MyApplication

//max distance in kilometer between costumer and company
const val MAX_DISTANCE = 20

// Represent the View Model of AddTravelActivity
class MainActivityViewModel : ViewModel() {

    //all the travel list
    private var travelsList: List<Travel> = ArrayList<Travel>()
    //all the travel of the costumer after filter
    private var costumerList: MutableLiveData<List<Travel>> = MutableLiveData()
    //all the travel of the company after filter
    private var companyList: MutableLiveData<List<Travel>> = MutableLiveData()
    //all the travel of the history after filter
    private var historyList: MutableLiveData<List<Travel>> = MutableLiveData()

    private val sharedPreferences =
        MyApplication.getAppContext().getSharedPreferences("USER", MODE_PRIVATE)
    private val userMail = sharedPreferences.getString("Mail", "")

    private var travelRepo: ITravelRepository = TravelRepository()

    init {
        travelRepo.setNotifyToTravelListListener {
            travelsList = travelRepo.allTravels
            costumerList.value = filterCostumerTravels(userMail!!)
            companyList.value = filterCompanyTravels()
            historyList.value = filterHistoryTravels()
        }
    }
    
    fun getCostumerTravels(): LiveData<List<Travel>> = costumerList

    fun getCompanyTravels(): LiveData<List<Travel>> = companyList

    fun getHistoryTravels(): LiveData<List<Travel>> = historyList

    fun loadHistoryList() {
        travelsList = travelRepo.loadData()
        historyList.value = filterHistoryTravels()
    }

    fun loadCompanyList() {
        travelsList = travelRepo.loadData()
        companyList.value = filterCompanyTravels()
    }

    fun loadCostumerList() {
        travelsList = travelRepo.loadData()
        costumerList.value = filterCostumerTravels(userMail!!)
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

    private fun filterCostumerTravels(userMail: String): List<Travel> {
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

    //filter the travel to the company
    private fun filterCompanyTravels(): List<Travel> {
        val tempList = ArrayList<Travel>()
        val companyMail = userMail!!.substringBefore('@')

        for (travel in travelsList) {
            //the status of requestType is SENT
            if (travel.requestType == RequestType.SENT) {
                //calculate the distanse
                val dis = MainActivity.calculateDistance(MainActivity.currentLocation,travel.address!!)
                //if distance appropriate
                if (dis < MAX_DISTANCE)
                    tempList.add(travel)
                //the status of requestType is ACCEPT or RUN or CLOSE
            } else if (travel.requestType != RequestType.SENT && travel.requestType != RequestType.PAYMENT) {
                //Brings only the approved travels to this company
                if (travel.companyEmail == companyMail){
                    tempList.add(travel)
                }
            }
        }
        return tempList
    }

    //filter the travel to the history, that he will can see only who with CLOSE status
    private fun filterHistoryTravels(): List<Travel>{
        val tempList = ArrayList<Travel>()
        for (travel in travelsList)
            if (travel.requestType == RequestType.CLOSE)
                tempList.add(travel)
        return tempList
    }

    //update travel
    fun updateTravel(currentItem: Travel) {
        travelRepo.updateTravel(currentItem)
    }
}