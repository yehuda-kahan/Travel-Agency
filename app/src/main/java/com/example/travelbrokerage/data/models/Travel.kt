package com.example.travelbrokerage.data.models

import android.annotation.SuppressLint
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.android.libraries.places.api.model.Place
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


// Represent travel details
// Can be stored in FireBase and/or in SqlLite through ROOM
@Entity(tableName = "travels")
class Travel {

    @NonNull
    @PrimaryKey
    var travelId: String? = null
    var clientName: String? = null
    var clientPhone: String? = null
    var clientEmail: String? = null
    var companyEmail: String? = null

    var numOfTravelers: Int? = null

    @TypeConverters(UserLocationConverter::class)
    var address: UserLocation? = null

    @TypeConverters(UserLocationConverter::class)
    var travelLocations: MutableList<UserLocation> = arrayListOf()

    @TypeConverters(RequestTypeConverter::class)
    var requestType: RequestType? = null

    @TypeConverters(DateConverter::class)
    var travelDate: Date? = null

    @TypeConverters(DateConverter::class)
    var arrivalDate: Date? = null

    @TypeConverters(CompanyConverter::class)
    var company: HashMap<String, Boolean> = HashMap()

    class DateConverter {

        @SuppressLint("SimpleDateFormat")
        private var format = SimpleDateFormat("dd-MM-yyyy")

        @TypeConverter
        @Throws(ParseException::class)
        fun fromTimestamp(date: String?): Date? {
            return if (date == null) null else format.parse(date)
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): String? {
            return if (date == null) null else format.format(date)
        }
    }

    enum class RequestType {
        SENT, ACCEPTED, RUN, CLOSE, PAYMENT;
    }

    class RequestTypeConverter {

        @TypeConverter
        fun getType(numeral: Int): RequestType {
            when (numeral) {
                1 -> return RequestType.SENT
                2 -> return RequestType.ACCEPTED
                3 -> return RequestType.RUN
                4 -> return RequestType.CLOSE
            }
            return RequestType.PAYMENT
        }

        @TypeConverter
        fun getTypeInt(requestType: RequestType): Int {
            when (requestType) {
                RequestType.SENT -> return 1
                RequestType.ACCEPTED -> return 2
                RequestType.RUN -> return 3
                RequestType.CLOSE -> return 4
            }
            return 5
        }
    }

    class CompanyConverter {
        @TypeConverter
        fun fromString(value: String?): HashMap<String, Boolean>? {
            if (value == null || value.isEmpty()) return null
            val mapString =
                value.split(",").toTypedArray() //split map into array of (string,boolean) strings
            val hashMap = HashMap<String, Boolean>()
            for (s1 in mapString)  //for all (string,boolean) in the map string
            {
                if (!s1.isEmpty()) { //is empty maybe will needed because the last char in the string is ","
                    val s2 = s1.split(":")
                        .toTypedArray() //split (string,boolean) to company string and boolean string.
                    val aBoolean = java.lang.Boolean.parseBoolean(s2[1])
                    hashMap[s2[0]] = aBoolean
                }
            }
            return hashMap
        }

        @TypeConverter
        fun asString(map: HashMap<String?, Boolean?>?): String? {
            if (map == null) return null
            val mapString = StringBuilder()
            for ((key, value) in map) mapString.append(
                key
            ).append(":").append(value).append(",")
            return mapString.toString()
        }
    }

    class UserLocationConverter {
        @TypeConverter
        fun fromString(value: String?): UserLocation? {
            if (value == null || value == "") return null
            val lat = value.split(" ").toTypedArray()[0].toDouble()
            val long = value.split(" ").toTypedArray()[1].toDouble()
            return UserLocation(lat, long)
        }

        @TypeConverter
        fun asString(warehouseUserLocation: UserLocation?): String {
            return if (warehouseUserLocation == null) "" else warehouseUserLocation.getLon()
                .toString() + " " + warehouseUserLocation.getLat()
        }
    }

    class UserLocationListConverter {

        @TypeConverter
        fun fromTravelLocations(TravelLocations: List<UserLocation>?): String? {
            if (TravelLocations == null) {
                return null
            }
            val gson = Gson()
            val type: Type = object : TypeToken<List<UserLocation>?>() {}.getType()
            return gson.toJson(TravelLocations, type)
        }

        @TypeConverter
        fun toTravelLocations(TravelLocationsString: String): List<UserLocation>? {
            if (TravelLocationsString == null) {
                return null
            }
            val gson = Gson()
            val type: Type = object : TypeToken<List<UserLocation?>?>() {}.getType()
            return gson.fromJson<List<UserLocation>>(TravelLocationsString, type)
        }
    }

    class UserLocation() {
        private var lat: Double? = null
        private var lon: Double? = null
        fun getLat(): Double? {
            return lat
        }

        fun getLon(): Double? {
            return lon
        }

        constructor(lat: Double?, lon: Double?) : this() {
            this.lat = lat
            this.lon = lon
        }

        constructor(place: Place) : this() {
            this.lat = place.latLng?.latitude
            this.lon = place.latLng?.longitude
        }
    }
}