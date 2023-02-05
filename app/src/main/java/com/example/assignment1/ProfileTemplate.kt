package com.example.assignment1

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile

class ProfileTemplate : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getIntExtra("id",0)
        renderInfo(id)

        findViewById<Button>(R.id.contactUs).setOnClickListener(::contactUs)
        findViewById<TextView>(R.id.ResumeBtn).setOnClickListener(::chooseFile)
        findViewById<Button>(R.id.location).setOnClickListener(::location)
    }

    private fun renderInfo(id: Int) {
        when(id) {
            R.id.itBtn -> setContentView(R.layout.activity_profile)
            R.id.constrBtn -> setContentView(R.layout.activity_profile)
            R.id.eduBtn -> setContentView(R.layout.activity_profile)
            R.id.enterBtn -> setContentView(R.layout.activity_profile)
            R.id.financeBtn -> setContentView(R.layout.activity_profile)
            R.id.fnbBtn -> setContentView(R.layout.activity_profile)
            R.id.debugBtn -> setContentView(R.layout.activity_debug)
        }
    }

    private fun contactUs(view: View) {
        if (checkSMSPerms()) {
            writeSms("hello")
//            toast("Success")
        } else {
            requestSMSPerms()
        }
    }

    private fun chooseFile(view: View) {
        fileSelector()
//        if (checkPerms()) {
//            fileSelector()
//        }
//        else {
//            requestPerms()
//        }
    }

    private fun location(view: View) {
        val PERMISSIONS = arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (checkLocPerms(this, *PERMISSIONS)) {
            // Check background location perms
            if (checkLocPerms(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                // send to google maps
                redirectLocation()
            } else {
                val bgAlert = AlertDialog.Builder(this)
                bgAlert.setTitle("Permission Needed!")
                bgAlert.setMessage("Background Location Permission Needed!, tap \"Allow all the time in the next screen\"")
                bgAlert.setPositiveButton("Yes") {
                    dialog, which -> requestLocPerms(this, arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION), 103)
                }
                bgAlert.setNegativeButton("No") {
                    dialog, which -> dialog.cancel()
                }
                bgAlert.show()
            }
        } else {
            requestLocPerms(this, PERMISSIONS, 102)
        }
    }
    // TODO: Merge all of the check and request permission into a permission handler function - Jon
    private fun checkFileStoragePerms(): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            //Android is 11(R) or above
            Environment.isExternalStorageManager()
        }
        else{
            //Android is below 11(R)
            val write = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            val read = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestFileStoragePerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
            selectorARL.launch(intent)
        }
        else{
            //Android is below 11(R)
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                100
            )
        }
    }

    private fun checkSMSPerms(): Boolean{
        val sendsms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
        val readsms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
        val boolie1 = sendsms == PackageManager.PERMISSION_GRANTED
        val boolie2 = readsms == PackageManager.PERMISSION_GRANTED
        println("$boolie1 $boolie2")
        return readsms == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSMSPerms() {
        println("request Perms")
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), 101)
    }

    private fun checkLocPerms(context: Context,vararg permissions: String): Boolean = permissions.all {
        val bgLoc = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        val coarseLoc = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        val fineLoc = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
        val boolie1 = bgLoc == PackageManager.PERMISSION_GRANTED
        val boolie2 = coarseLoc == PackageManager.PERMISSION_GRANTED
        val boolie3 = fineLoc == PackageManager.PERMISSION_GRANTED
        println("bgLoc = $boolie1, coarseLoc = $boolie2, fineLoc = $boolie3")
        val isPerm = ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
        println("Checking $it: $isPerm")
        ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
//        return bgLoc == PackageManager.PERMISSION_GRANTED && coarseLoc == PackageManager.PERMISSION_GRANTED && fineLoc == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocPerms(activity: Activity, permissions: Array<String>, requestCode: Int) {
        println("Request location permissions: $permissions")
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    private fun redirectLocation() {
        println("Redirect to google maps")
        val gmmIntentUri = Uri.parse("google.navigation:q=Singapore Institute of Technology @ NYP")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    private fun writeSms(message: String) {
        val smsIntent : Intent = Uri.parse("smsto:" + 91234567).let { number -> Intent(Intent.ACTION_VIEW, number)}
        smsIntent.putExtra("sms_body", message)
        startActivity(smsIntent)
    }

    private val selectorARL = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

            val uri = intent?.data
            val filename = uri!!.path!!.substring(uri.path!!.lastIndexOf('/') + 1)
            println(filename)

            val textView = findViewById<TextView>(R.id.filename)
            textView.text = filename
        }
    }

    private fun fileSelector() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        selectorARL.launch(Intent.createChooser(intent, "Choose a file"))
    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}