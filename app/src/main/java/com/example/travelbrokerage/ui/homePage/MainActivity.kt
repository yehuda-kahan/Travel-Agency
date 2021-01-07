package com.example.travelbrokerage.ui.homePage


import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.*
import com.example.travelbrokerage.R
import com.example.travelbrokerage.ui.companyTravelsFragment.CompanyTravelsFragment
import com.example.travelbrokerage.ui.historyTravelsFragment.HistoryTravelsFragment
import com.example.travelbrokerage.ui.registeredTravelsFragment.RegisteredTravelsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var dl: DrawerLayout
    private lateinit var t: ActionBarDrawerToggle
    private lateinit var nv: NavigationView
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var registeredTravelsFragment: RegisteredTravelsFragment
    private lateinit var companyTravelsFragment: CompanyTravelsFragment
    private lateinit var historyTravelsFragment: HistoryTravelsFragment
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       /* registeredTravelsFragment = RegisteredTravelsFragment()
        companyTravelsFragment = CompanyTravelsFragment()
        historyTravelsFragment = HistoryTravelsFragment()*/

        dl = findViewById<DrawerLayout>(R.id.activity_main)
        t = object : ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close) {
            override fun onDrawerClosed(view: View) {
                supportActionBar!!.title = "Close"
                // calling onPrepareOptionsMenu() to show action bar icons
                supportInvalidateOptionsMenu()
            }

            override fun onDrawerOpened(drawerView: View) {
                supportActionBar!!.title = "Open"
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

}