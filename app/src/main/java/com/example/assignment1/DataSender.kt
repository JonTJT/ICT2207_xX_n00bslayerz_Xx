package com.example.assignment1

import android.util.Log

// For HTTP sending data
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import java.util.*

class DataSender() {
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
}