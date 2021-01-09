package com.example.travelbrokerage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import com.example.travelbrokerage.R
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.data.models.Travel.UserLocation
import com.example.travelbrokerage.ui.homePage.MainActivityViewModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AdapterCostumer(
    private val context: Context,
    private val viewModel: MainActivityViewModel,
    private val costumerList: ArrayList<Travel>,
) :
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
        initializeRequestTypeSpinner(viewHolder.status, currentItem.requestType!!)

        viewHolder.confirmBtn.setTag(R.integer.confirm_btn_view, convertView)
        viewHolder.confirmBtn.setTag(R.integer.confirm_btn_pos, position)
        viewHolder.confirmBtn.setOnClickListener(View.OnClickListener {
            val tempview = viewHolder.confirmBtn.getTag(R.integer.confirm_btn_view) as View
            val spinnerRequestType = tempview.findViewById<Spinner>(R.id.status)
            val spinnerCompany = tempview.findViewById<Spinner>(R.id.companies)

            currentItem.requestType = Travel.RequestType.values()[spinnerRequestType.selectedItemPosition + 1]  //as Travel.RequestType
                if (spinnerRequestType.selectedItemPosition + 1 == Travel.RequestType.ACCEPTED.ordinal){
                    currentItem.company.put(spinnerCompany.selectedItem.toString(), true)
                    currentItem.companyEmail = spinnerCompany.selectedItem.toString()
                }
            viewModel.updateTravel(currentItem)
        })

        return convertView
    }

    //ViewHolder inner class
    private class ViewHolder(view: View) {

        var address: TextView = view.findViewById<View>(R.id.address) as TextView
        var destination: TextView = view.findViewById<View>(R.id.destination) as TextView
        var date: TextView = view.findViewById<View>(R.id.date) as TextView
        var companies: Spinner = view.findViewById<View>(R.id.companies) as Spinner
        var status: Spinner = view.findViewById<View>(R.id.status) as Spinner
        var confirmBtn: Button = view.findViewById(R.id.btn_confirm) as Button
    }

    private fun getPlace(location: UserLocation): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = geocoder.getFromLocation(location.getLat()!!, location.getLon()!!, 1)
            if (addresses.isNotEmpty()) {
                val cityName = addresses[0].getAddressLine(0)
                //val stateName = addresses[0].getAddressLine(1)
                //val countryName = addresses[0].getAddressLine(2)
                return "$cityName"
            }
            return "no place"

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return "IOException ..."
    }

    // Initial the spinner values
    private fun initializeCompaniesSpinner(spinner: Spinner, companies: HashMap<String, Boolean>) {
        val items: MutableList<String> = arrayListOf()
        for (company in companies) {
            items.add(company.key)
        }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, items)
        spinner.adapter = adapter
    }

    private fun initializeRequestTypeSpinner(spinner: Spinner, requestType: Travel.RequestType) {
        val requests = mutableListOf<String>(
            "התקבלה נסיעה",
            "התחלת נסיעה",
            "סיום נסיעה"
        )
        // initialize an array adapter for spinner
        val adapter: ArrayAdapter<String> = object :
            ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, requests) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView

                // set item text bold and sans serif font
                view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)

                if ((requestType == Travel.RequestType.ACCEPTED && position == 0) ||
                    (requestType == Travel.RequestType.RUN && (position==1 || position == 0 ))) {
                    // set the spinner disabled item text color
                    view.setTextColor(Color.LTGRAY)
                }

                // set selected item style
                if (position == spinner.selectedItemPosition) {
                    view.background = ColorDrawable(Color.parseColor("#F5F5F5"))
                }

                return view
            }

            override fun isEnabled(position: Int): Boolean {
                if (requestType == Travel.RequestType.ACCEPTED && position == 0)
                    return false
                if (requestType == Travel.RequestType.RUN && (position == 1 || position == 0))
                    return false
                return true
            }
        }
        // finally, data bind spinner with adapter
        spinner.adapter = adapter
    }
}
