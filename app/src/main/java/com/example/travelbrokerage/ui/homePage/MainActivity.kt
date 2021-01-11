package com.example.travelbrokerage.ui.homePage


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.travelbrokerage.R
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.ui.companyTravelsFragment.CompanyTravelsFragment
import com.example.travelbrokerage.ui.historyTravelsFragment.HistoryTravelsFragment
import com.example.travelbrokerage.ui.registeredTravelsFragment.RegisteredTravelsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

const val AVERAGE_RADIUS_OF_EARTH = 6371.0

class MainActivity : AppCompatActivity() {

    private lateinit var dl: DrawerLayout
    private lateinit var t: ActionBarDrawerToggle
    private lateinit var nv: NavigationView
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener


    // Static field and fun to get the current location
    companion object{
        var currentLocation: Travel.UserLocation? = null

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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get the current location
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


        dl = findViewById<DrawerLayout>(R.id.activity_main)
            t = object : ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close) {
                override fun onDrawerClosed(view: View) {
                    // calling onPrepareOptionsMenu() to show action bar icons
                    supportInvalidateOptionsMenu()
                }

                override fun onDrawerOpened(drawerView: View) {
                // calling onPrepareOptionsMenu() to hide action bar icons
                supportInvalidateOptionsMenu()
            }
        }
        dl.addDrawerListener(t)
        t.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nv = findViewById(R.id.nv)
        nv.setNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener,
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                val id = item.itemId
                dl.closeDrawer(nv)
                return when (id) {
                    R.id.RegisteredTravelsFragment -> {
                        //replaceFragment(registeredTravelsFragment)
                        supportFragmentManager.commit {
                            replace<RegisteredTravelsFragment>(R.id.frameLayout)
                            setReorderingAllowed(true)
                        }
                        true
                    }
                    R.id.CompanyTravelsFragment -> {
                        //replaceFragment(companyTravelsFragment)
                        supportFragmentManager.commit {
                            replace<CompanyTravelsFragment>(R.id.frameLayout)
                            setReorderingAllowed(true)
                        }
                        true
                    }
                    R.id.HistoryTravelsFragment -> {
                        //replaceFragment(historyTravelsFragment)
                        supportFragmentManager.commit {
                            replace<HistoryTravelsFragment>(R.id.frameLayout)
                            setReorderingAllowed(true)
                        }
                        true
                    }
                    else -> true
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (t.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    private fun replaceFragment(fragment: Fragment) {
        val backStateName: String = fragment.javaClass.name
        val manager = supportFragmentManager
        val fragmentPopped = manager.popBackStackImmediate(backStateName, 0)
        if (!fragmentPopped) { //fragment not in back stack, create it.
            val ft = manager.beginTransaction()
            ft.replace(R.id.frameLayout, fragment)
            ft.addToBackStack(backStateName)
            ft.commit()
        }
    }

    //     Check the SDK version and whether the permission is already granted or not.
    private fun getLocation() {

        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), 5)
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

    @SuppressLint("MissingPermission")
   override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0f,
                    locationListener
                )
            } else {
                Toast.makeText(
                    this,
                    "Until you grant the permission, we canot display the location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}