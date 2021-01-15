package com.example.travelbrokerage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.travelbrokerage.R
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.ui.homePage.MainActivity
import com.example.travelbrokerage.ui.homePage.MainActivityViewModel
import com.example.travelbrokerage.util.MyApplication
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AdapterHistory (
    private val context: Context,
    private val viewModel: MainActivityViewModel,
    private val historyList: ArrayList<Travel>,
) :
    BaseAdapter() {

    override fun getCount(): Int = historyList.size

    override fun getItem(position: Int): Any = historyList[position]

    override fun getItemId(position: Int): Long = position.toLong()


    @SuppressLint("SimpleDateFormat")
    override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {

        val viewHolder: ViewHolder
        var convertView = _convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_history, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView!!.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val currentItem = getItem(position) as Travel
        //fill the textView companyName with the companyName of the travel
        viewHolder.companyName.text = currentItem.companyEmail.toString()
        //calculate the sum of kilometer of the travel
        viewHolder.kilometers.text = MainActivity.calculateDistance(currentItem.address!!, currentItem.travelLocations[0]).toString()

        //change status to PAYMENT
        viewHolder.statBtn.setOnClickListener {
            currentItem.requestType = Travel.RequestType.PAYMENT
            viewModel.updateTravel(currentItem)
        }

        //send mail to the company remember to pay
        viewHolder.callBtn.setOnClickListener {
            val to = currentItem.companyEmail + "@gmail.com"
            val subject = "תשלום נסיעה"
            val message = "שלום וברכה, ברצוני להזכיר לך שעליך לשלם את סכום תיווך הנסיעה"

            val intent = Intent(Intent.ACTION_SEND)
            val addressees = arrayOf(to)
            intent.putExtra(Intent.EXTRA_EMAIL, addressees)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type = "message/rfc822"
            ContextCompat.startActivity(
                context,
                Intent.createChooser(intent, "Send Email using:"),
                null
            )
        }
        return convertView
    }

    //ViewHolder inner class
    private class ViewHolder(view: View) {

        var companyName: TextView = view.findViewById<View>(R.id.company_name) as TextView
        var kilometers: TextView = view.findViewById<View>(R.id.num_kilometer) as TextView
        var statBtn: Button = view.findViewById(R.id.btn_change_status) as Button
        var callBtn: Button = view.findViewById(R.id.btn_call_company) as Button
    }
}