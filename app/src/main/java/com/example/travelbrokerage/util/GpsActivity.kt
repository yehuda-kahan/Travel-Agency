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
    var currentLocation: Travel.UserLocation? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps2)

        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

        // Define a listener that responds to location updates
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                currentLocation!!.lat = location.latitude
                currentLocation!!.lon = location.longitude
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }
        getLocation()
    }

    fun calculateDistance(userLocation: Travel.UserLocation): Float {
        val latDistance = Math.toRadians(currentLocation!!.lat!! - userLocation.lat!!)
        val lngDistance = Math.toRadians(currentLocation!!.lon!! - userLocation.lon!!)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(currentLocation!!.lat!!)) *
                Math.cos(Math.toRadians(userLocation.lat!!)) *
                Math.sin(lngDistance / 2) *
                Math.sin(lngDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return (Math.round(AVERAGE_RADIUS_OF_EARTH * c)).toFloat()
    }// Android version is lesser than 6.0 or the permission is already granted.

    //     Check the SDK version and whether the permission is already granted or not.
    private fun getLocation() {

        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "RequestPermission", Toast.LENGTH_LONG).show()
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                locationListener
            )
        }
    }
}