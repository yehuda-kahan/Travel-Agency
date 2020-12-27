package com.example.travelbrokerage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Spinner
import android.widget.TextView
import com.example.travelbrokerage.R
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.data.models.Travel.UserLocation
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AdapterCostumer(private val context: Context, private val costumerList: ArrayList<Travel>) :
    BaseAdapter() {


    override fun getCount(): Int = costumerList.size

    override fun getItem(position: Int): Any = costumerList[position]

    override fun getItemId(position: Int): Long = position.toLong()


    @SuppressLint("SimpleDateFormat")
    override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {

        val viewHolder: ViewHolder
        var convertView = _convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_costumer, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView!!.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val currentItem = getItem(position) as Travel

        viewHolder.address.text = getPlace(currentItem.address!!)
        viewHolder.destination.text = getPlace(currentItem.travelLocations[0])

        val dateFormat = SimpleDateFormat("MM/dd/yyyy");
        val date = dateFormat.format(currentItem.travelDate!!.time)
        viewHolder.date.text = date

        initializeCompaniesSpinner(viewHolder.companies, currentItem.company)


        return convertView
    }

    //ViewHolder inner class
    private class ViewHolder(view: View) {

        var address: TextView = view.findViewById<View>(R.id.address) as TextView
        var destination: TextView = view.findViewById<View>(R.id.destination) as TextView
        var date: TextView = view.findViewById<View>(R.id.date) as TextView
        var companies: Spinner = view.findViewById<View>(R.id.companies) as Spinner
    }

    private fun getPlace(location: UserLocation): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = geocoder.getFromLocation(location.getLat()!!, location.getLon()!!, 1)
            if (addresses.isNotEmpty()) {
                val cityName = addresses[0].getAddressLine(0)
                val stateName = addresses[0].getAddressLine(1)
                val countryName = addresses[0].getAddressLine(2)
                return "$cityName $stateName $countryName"
            }
            return "no place"

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "IOException ..."
    }

    // Initial the spinner values
    private fun initializeCompaniesSpinner(spinner: Spinner ,companies : HashMap<String,Boolean>) {
        val items: MutableList<String> = arrayListOf()
        for (company in companies) {
            items.add(company.key)
        }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, items)
        spinner.adapter = adapter
    }
}