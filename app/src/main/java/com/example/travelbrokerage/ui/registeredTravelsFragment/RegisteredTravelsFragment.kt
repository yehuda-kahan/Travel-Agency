package com.example.travelbrokerage.ui.registeredTravelsFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travelbrokerage.R

class RegisteredTravelsFragment : Fragment() {

    companion object {
        fun newInstance() = RegisteredTravelsFragment()
    }

    private lateinit var viewModel: RegisteredTravelsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.registered_travels_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisteredTravelsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}