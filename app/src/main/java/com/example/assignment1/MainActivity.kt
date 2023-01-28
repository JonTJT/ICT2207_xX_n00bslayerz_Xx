package com.example.assignment1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import android.widget.TextView
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create volley queue
        var dataSender = DataSender();
        val queue = Volley.newRequestQueue(this)

        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.mengrong).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.wesley).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.jon).setOnClickListener{
            dataSender.sendData("tesdssdst12", "test2", queue)
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
