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
import com.example.travelbrokerage.ui.companyTravelsFragment.BlankFragment
import com.example.travelbrokerage.ui.homePage.MainActivityViewModel
import java.util.*

class RegisteredTravelsFragment : Fragment() {

    companion object {
        fun newInstance() = RegisteredTravelsFragment()
    }

    private lateinit var viewModel : MainActivityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.registered_travels_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val listView = view?.findViewById<ListView>(R.id.list_view_costumer)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        viewModel.getTravels().observe(viewLifecycleOwner, Observer { travels ->
            val tmp = ArrayList(travels)

            //create adapter object
            val adapter = AdapterCostumer(requireContext(), tmp)

            //set custom adapter as adapter to our list view
            listView?.adapter = adapter
        })
    }
}