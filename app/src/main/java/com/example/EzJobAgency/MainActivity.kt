package com.example.EzJobAgency

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView


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
