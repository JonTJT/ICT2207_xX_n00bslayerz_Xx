package com.example.assignment1

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ContactTemplate : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id = intent.getIntExtra("id",0)
        setContentView(R.layout.activity_contact)

        findViewById<Button>(R.id.contact_us).setOnClickListener(::onclick_contactus)
    }

    private fun onclick_contactus(view: View) {
        if (checkSMSPerms()) {
            writeSms("hello")
            toast("Success")
        } else {
            requestSMSPerms()
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
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS ), 101)
    }

    private fun writeSms(message: String) {
        val smsIntent : Intent = Uri.parse("smsto:" + 91234567).let {number -> Intent(Intent.ACTION_VIEW, number)}
        smsIntent.putExtra("sms_body", message)
        startActivity(smsIntent)
    }

//    private fun requestPerms() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
//            //Android is 11(R) or above
//            try {
//                val intent = Intent()
//                intent.action = Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
//                val uri = Uri.fromParts("package", this.packageName, null)
//                intent.data = uri
//                storageActivityResultLauncher.launch(intent)
//            }
//            catch (e: Exception){
//                val intent = Intent()
//                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//                storageActivityResultLauncher.launch(intent)
//            }
//        }
//        else{
//            //Android is below 11(R)
//            ActivityCompat.requestPermissions(this,
//                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE),
//                100           )
//        }
//    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}