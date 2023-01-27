package com.example.assignment1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

// For HTTP sending data
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    fun sendData(name: String, email: String, queue: RequestQueue) {
        val url = "http://192.168.1.24/dashboard/dbconfig.php"
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response -> // response
                Log.d("Response", response!!)
            },
            Response.ErrorListener { error -> // error
                Log.d("Error.Response", error.toString())
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["name"] = name
                params["email"] = email
                return params
            }
        }
        queue.add(postRequest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create volley queue
        val queue = Volley.newRequestQueue(this)

        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.mengrong).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.wesley).setOnClickListener(::clickName)
        findViewById<TextView>(R.id.jon).setOnClickListener{
            sendData("tesdssdst1", "test2", queue)
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
