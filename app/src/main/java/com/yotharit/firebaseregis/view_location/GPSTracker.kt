package com.yotharit.firebaseregis.view_location

import android.os.Bundle
import android.content.Intent
import android.os.IBinder
import android.R.string.cancel
import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.DialogInterface
import androidx.core.content.ContextCompat.startActivity
import android.provider.Settings.ACTION_LOCALE_SETTINGS
import android.location.LocationManager
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import androidx.core.content.ContextCompat.getSystemService
import android.location.LocationListener
import android.provider.Settings
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog


class GPSTracker(private val mContext: Context) : Service(), LocationListener {

    // is GPS enable status
    internal var isGPSEnabled = false
    // check network enable status
    internal var isNetworkEnabled = false
    // is able to get location
    var isCanGetLocation = false
        internal set

    internal var location: Location? = null
    internal var latitude: Double = 0.toDouble()
    internal var longitude: Double = 0.toDouble()

    protected var locationManager: LocationManager? = null

    init {
        getLocation()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager?
            // gps status
            isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            // network status
            isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isNetworkEnabled && !isGPSEnabled) {
                // no network
            } else {
                this.isCanGetLocation = true
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BTW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )
                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                        if (location != null) {
                            latitude = location!!.getLatitude()
                            longitude = location!!.getLongitude()
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BTW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                            this
                        )
                        if (locationManager != null) {
                            location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.getLatitude()
                                longitude = location!!.getLongitude()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GPSTracker)
        }
    }

    fun getLatitude(): Double {
        if (location != null) latitude = location!!.getLatitude()
        return latitude
    }

    fun getLongitude(): Double {
        if (location != null) longitude = location!!.getLongitude()
        return longitude
    }

    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)

        alertDialog.setTitle("GPS has been set?")
        alertDialog.setMessage("GPS is not enable, Enable?")

        alertDialog.setPositiveButton("Settings", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            mContext.startActivity(intent)
        })

        alertDialog.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
    }

    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {

    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {

    }

    override fun onProviderEnabled(provider: String) {

    }

    override fun onProviderDisabled(provider: String) {

    }

    companion object {
        private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters
        private val MIN_TIME_BTW_UPDATES = (1000 * 60 * 1).toLong() // 1 min
    }
}