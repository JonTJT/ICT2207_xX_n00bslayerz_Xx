package com.example.assignment1

import android.content.Intent
import android.icu.text.IDNA.Info
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.mengrong).setOnClickListener(::click_name)
        findViewById<TextView>(R.id.wesley).setOnClickListener(::click_name)
        findViewById<TextView>(R.id.jon).setOnClickListener(::click_name)
        findViewById<TextView>(R.id.keefe).setOnClickListener(::click_name)
        findViewById<TextView>(R.id.minyao).setOnClickListener(::click_name)
        findViewById<TextView>(R.id.lynette).setOnClickListener(::click_name)
    }

    fun click_name(view: View) {
        val intent = Intent(this@MainActivity, InfoActivity::class.java)
        intent.putExtra("id", view.id)
        startActivity(intent)
    }
}
