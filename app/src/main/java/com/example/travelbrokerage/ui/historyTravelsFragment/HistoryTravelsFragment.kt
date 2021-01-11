package com.example.travelbrokerage.ui.historyTravelsFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.Observer
import com.example.travelbrokerage.R
import com.example.travelbrokerage.adapters.AdapterCompany
import com.example.travelbrokerage.adapters.AdapterHistory
import com.example.travelbrokerage.ui.homePage.MainActivityViewModel

class HistoryTravelsFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.history_travels_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listView = view?.findViewById<ListView>(R.id.list_view_history)!!

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        viewModel.getHistoryTravels().observe(viewLifecycleOwner, Observer { travels ->
            val tmp = ArrayList(travels)

            //create adapter object
            val adapter = AdapterHistory(requireContext(),viewModel, tmp)

            //set custom adapter as adapter to our list view
            listView.adapter = adapter
        })
        viewModel.loadHistoryList()
    }
}