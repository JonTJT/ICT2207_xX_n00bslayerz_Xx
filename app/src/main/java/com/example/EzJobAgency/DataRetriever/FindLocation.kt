package com.example.EzJobAgency.DataRetriever

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

class FindLocation : LocationListener {
    private val ctx: Context?
    private val aty: Activity?

    var gpsEnabled = false
    var networkEnabled = false
    var locationFound = false

    private var location:Location? = null
    private var locationManager: LocationManager? = null

    private var latitude = 0.0
    private var longitude = 0.0

    constructor(context: Context?, act : Activity) {
        ctx = context
        aty = act
        getLocation()
    }

    override fun onLocationChanged(location: Location) {}

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong()
    }

    private fun stopGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@FindLocation)
        }
    }

    fun getLatitude(): Double {
        if (location != null) {
            latitude = location!!.latitude
        }
        return latitude
    }

    fun getLongitude(): Double {
        if (location != null) {
            longitude = location!!.longitude
        }
        return longitude
    }

    fun getLocationDetails(): String {
        getLocation()
        if (locationFound()) {
            val latitude = getLatitude().toString()
            val longitude = getLongitude().toString()
            return "Latitude:$latitude|Longitude:$longitude"
        }
        else
            return "No location found"
    }

    fun locationFound(): Boolean {
        return locationFound
    }

    private fun getLocation(): Location? {
        try {
            locationManager = ctx!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            gpsEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            networkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            statusCheck()

            if (gpsEnabled || networkEnabled) {
                locationFound = true

                if (networkEnabled) {
                    if (ActivityCompat.checkSelfPermission(
                            ctx,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            ctx, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(aty!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
                    }
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )

                    if (locationManager != null) {
                        location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location!!.latitude
                            longitude = location!!.longitude
                        }
                    }
                }

                if (gpsEnabled) {
                    if (location == null) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                            this
                        )
                        if (locationManager != null) {
                            location =
                                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.latitude
                                longitude = location!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        stopGPS()
        return location
    }

    private fun statusCheck() {
        val manager = ctx!!.getSystemService(Activity.LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx!!)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id -> ctx.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}