package com.example.travelbrokerage.ui.companyTravelsFragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travelbrokerage.R
import com.example.travelbrokerage.ui.homePage.MainActivityViewModel

class CompanyTravelsFragment : Fragment() {

    companion object {
        fun newInstance() = CompanyTravelsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.company_travels_fragment, container, false)
    }
}