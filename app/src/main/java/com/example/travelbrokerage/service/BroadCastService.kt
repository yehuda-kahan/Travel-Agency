package com.example.travelbrokerage.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.travelbrokerage.data.models.Travel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BroadCastService : Service() {

    private val dataBase = FirebaseDatabase.getInstance()
    private val numOfTravelsRef = dataBase.getReference("isTravelAdded")

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        numOfTravelsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val isTravelAdded = snapshot.value as Boolean
                if (isTravelAdded) {
                    numOfTravelsRef.setValue(false)
                    val i = Intent("com.example.travelbrokerage.CUSTOM_ACTION")
                    //i.putExtra("numOfTravels", numOfTravels)
                    sendBroadcast(i)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return START_STICKY
    }
}