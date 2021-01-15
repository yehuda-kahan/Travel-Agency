package com.example.travelbrokerage.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.get
import com.example.travelbrokerage.R
import com.example.travelbrokerage.data.models.Travel
import com.example.travelbrokerage.data.models.Travel.UserLocation
import com.example.travelbrokerage.ui.homePage.MainActivityViewModel
import com.example.travelbrokerage.util.MyApplication
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AdapterCompany(private val context: Context,
                     private val viewModel: MainActivityViewModel, private val companyList: ArrayList<Travel>) :
    BaseAdapter() {

    private val sharedPreferences =
        MyApplication.getAppContext().getSharedPreferences("USER", Context.MODE_PRIVATE)
    //user mail is the mail of the company
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

        //fill the textView address with the address of the travel
        viewHolder.address.text = getPlace(currentItem.address!!)
        //fill the textView destination with the destination of the travel
        viewHolder.destination.text = getPlace(currentItem.travelLocations[0])
        //fill the textView name with the client name of the travel
        viewHolder.name.text = currentItem.clientName
        //fill the textView numTravelers with the numTravelers of the travel
        viewHolder.numTravelers.text = currentItem.numOfTravelers.toString()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy");
        val date = dateFormat.format(currentItem.travelDate!!.time - (31556952000 * 1900) + 86400000)

        viewHolder.startDay.text = date
        val diff: Long = currentItem.arrivalDate!!.time - currentItem.travelDate!!.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        //fill the textView numDays with the numDays that we calculate now in the travel
        viewHolder.numDays.text = days.toString()
        //save in val mail the mail of the company without the finish of @gmail.com
        val mail = userMail!!.substringBefore('@')

        //check is a boolean type, that tell us if the client confirm the suggest of the company
        val check = currentItem.company.get(mail) == true
        viewHolder.checkBox.isChecked = check

        //if the company don't exist in the field company in the travel, we add him with false value
        viewHolder.confirmBtn.setOnClickListener(View.OnClickListener {
            if (currentItem.company.get(mail) == null) {
                currentItem.company.put(mail, false)
                viewModel.updateTravel(currentItem)
            }
        })

        //if the company push on this button, this sent a generic mail to the costumer
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
            startActivity(context, Intent.createChooser(intent, "Send Email using:"), null)
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

    private fun getPlace(location: UserLocation): String {
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
