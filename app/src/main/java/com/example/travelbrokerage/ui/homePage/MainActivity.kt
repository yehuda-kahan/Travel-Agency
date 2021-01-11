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
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.travelbrokerage.R
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.ui.companyTravelsFragment.CompanyTravelsFragment
import com.example.travelbrokerage.ui.historyTravelsFragment.HistoryTravelsFragment
import com.example.travelbrokerage.ui.registeredTravelsFragment.RegisteredTravelsFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

const val AVERAGE_RADIUS_OF_EARTH = 6371.0

class MainActivity : AppCompatActivity() {

    private lateinit var dl: DrawerLayout
    private lateinit var t: ActionBarDrawerToggle
    private lateinit var nv: NavigationView
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient


    // Static field and fun to get the current location
    companion object{
        private var currentLocation = Travel.UserLocation()

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        initializeLocation()

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
    private fun initializeLocation() {

        //     Check the SDK version and whether the permission is already granted or not.
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION), 44)
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnCompleteListener(OnCompleteListener {
           val location = it.getResult()
            if (location != null){
                currentLocation.lat = location.latitude
                currentLocation.lon = location.longitude
            }
        })
    }

    @SuppressLint("MissingPermission")
   override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 44) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(OnCompleteListener {
                    val location = it.getResult()
                    if (location != null){
                        currentLocation.lat = location.latitude
                        currentLocation.lon = location.longitude
                    }
                })
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