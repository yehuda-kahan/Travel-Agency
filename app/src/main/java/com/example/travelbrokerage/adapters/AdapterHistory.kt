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
import com.example.travelbrokerage.ui.homePage.MainActivityViewModel
import com.example.travelbrokerage.util.MyApplication
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AdapterHistory (
    private val context: Context,
    private val viewModel: MainActivityViewModel,
    private val companyList: ArrayList<Travel>,
) :
    BaseAdapter() {

    private val sharedPreferences =
        MyApplication.getAppContext().getSharedPreferences("USER", Context.MODE_PRIVATE)
    private val userMail = sharedPreferences.getString("Mail", "")

    override fun getCount(): Int = companyList.size

    override fun getItem(position: Int): Any = companyList[position]

    override fun getItemId(position: Int): Long = position.toLong()


    @SuppressLint("SimpleDateFormat")
    override fun getView(position: Int, _convertView: View?, parent: ViewGroup?): View {

        val viewHolder: ViewHolder
        var convertView = _convertView

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_company, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView!!.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
        }

        val currentItem = getItem(position) as Travel

        viewHolder.address.text = getPlace(currentItem.address!!)
        viewHolder.destination.text = getPlace(currentItem.travelLocations[0])
        viewHolder.name.text = currentItem.clientName
        viewHolder.numTravelers.text = currentItem.numOfTravelers.toString()

        val dateFormat = SimpleDateFormat("MM/dd/yyyy");
        val date = dateFormat.format(currentItem.travelDate!!.time)

        viewHolder.startDay.text = date
        val diff: Long = currentItem.arrivalDate!!.time - currentItem.arrivalDate!!.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        viewHolder.numDays.text = days.toString()

        val check = currentItem.company.get(userMail) == true
        viewHolder.checkBox.isChecked = check

        viewHolder.confirmBtn.setTag(R.integer.confirm_btn_view, convertView)
        viewHolder.confirmBtn.setTag(R.integer.confirm_btn_pos, position)
        viewHolder.confirmBtn.setOnClickListener(View.OnClickListener {
            if (currentItem.company.get(userMail!!) == null) {
                val mail = userMail.substringBefore('@')
                currentItem.company.put(mail, false)
                viewModel.updateTravel(currentItem)
            }
        })

        viewHolder.mailBtn.setOnClickListener {
            val to = currentItem.clientEmail
            val subject = "הזמנת נסיעה"
            val message = "שלום וברכה, ברצוננו להגיש הצעה מקסימה ומיוחדת במינה"

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

        var name: TextView = view.findViewById<View>(R.id.costumer_name) as TextView
        var address: TextView = view.findViewById<View>(R.id.address_company) as TextView
        var destination: TextView = view.findViewById<View>(R.id.destination_company) as TextView
        var numTravelers: TextView = view.findViewById<View>(R.id.num_of_travelers) as TextView
        var numDays: TextView = view.findViewById<View>(R.id.num_days) as TextView
        var startDay: TextView = view.findViewById<View>(R.id.start_day) as TextView
        var mailBtn: Button = view.findViewById(R.id.btn_mail) as Button
        var confirmBtn: Button = view.findViewById(R.id.btn_confirm_company) as Button
        var checkBox: CheckBox = view.findViewById(R.id.check_box_company) as CheckBox
    }

    private fun getPlace(location: Travel.UserLocation): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address>
        try {
            addresses = geocoder.getFromLocation(location.lat!!, location.lon!!, 1)
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
}