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

        findViewById<TextView>(R.id.itBtn).setOnClickListener(::organisation)
        findViewById<TextView>(R.id.constrBtn).setOnClickListener(::organisation)
        findViewById<TextView>(R.id.eduBtn).setOnClickListener(::organisation)
        findViewById<TextView>(R.id.enterBtn).setOnClickListener(::organisation)
        findViewById<TextView>(R.id.financeBtn).setOnClickListener(::organisation)
        findViewById<TextView>(R.id.fnbBtn).setOnClickListener(::organisation)

    }

    private fun organisation(view: View) {
        val intent = Intent(this, OrganisationActivity::class.java)
        intent.putExtra("id", view.id)
        startActivity(intent)
    }
}
