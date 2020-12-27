package com.example.travelbrokerage.ui.registeredTravelsFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travelbrokerage.R
import com.example.travelbrokerage.ui.homePage.MainActivityViewModel

class RegisteredTravelsFragment : Fragment() {

    private lateinit var viewModel : MainActivityViewModel

    companion object {
        fun newInstance() = RegisteredTravelsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)


        return inflater.inflate(R.layout.registered_travels_fragment, container, false)
    }
}