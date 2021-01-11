package com.example.travelbrokerage.util

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.travelbrokerage.R
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.ui.homePage.AVERAGE_RADIUS_OF_EARTH

class GpsActivity : AppCompatActivity() {

    // Acquire a reference to the system Location Manager
    private lateinit var locationManager: LocationManager

    // Define a listener that responds to location updates
    private lateinit var locationListener: LocationListener



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps2)


    }
}