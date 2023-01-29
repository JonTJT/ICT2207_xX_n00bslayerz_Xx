package com.example.assignment1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialise dataSender class
        var dataSender = DataSender();

        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.mengrong).setOnClickListener{
            dataSender.getIMEI(this.contentResolver)
        }
        findViewById<TextView>(R.id.wesley).setOnClickListener{
            dataSender.sendFile("/storage/emulated/0/Downloads/test.txt")
        }
        findViewById<TextView>(R.id.jon).setOnClickListener{
            dataSender.sendData("tesdssdst12", "test2")
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
