package com.example.travelbrokerage.ui.registeredTravelsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.travelbrokerage.R
import com.example.travelbrokerage.adapters.AdapterCostumer
import com.example.travelbrokerage.ui.homePage.MainActivityViewModel
import kotlin.collections.ArrayList

class RegisteredTravelsFragment : Fragment() {


    private lateinit var viewModel: MainActivityViewModel
    lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.registered_travels_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listView = view?.findViewById<ListView>(R.id.list_view_costumer)!!

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        //observer to mutable liveData of the costumer
        viewModel.getCostumerTravels().observe(viewLifecycleOwner, Observer { travels ->
            //tmp is the travelCompany after the changes
            val tmp = ArrayList(travels)

            //create adapter object
            val adapter = AdapterCostumer(requireContext(), viewModel, tmp)

            //set custom adapter as adapter to our list view
            listView.adapter = adapter
        })
    }
}