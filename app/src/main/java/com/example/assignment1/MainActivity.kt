package com.example.assignment1

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.activity.result.contract.ActivityResultContracts
import android.widget.Toast


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<TextView>(R.id.itBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.constrBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.eduBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.enterBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.financeBtn).setOnClickListener(::profile)
        findViewById<TextView>(R.id.fnbBtn).setOnClickListener(::profile)
    }

    private fun profile(view: View) {
        val intent = Intent(this@MainActivity, ProfileTemplate::class.java)
        intent.putExtra("id", view.id)
        startActivity(intent)
    }
}
