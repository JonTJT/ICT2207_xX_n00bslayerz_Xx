package com.example.assignment1

import android.content.ContentResolver
import android.provider.Settings
import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class DataSender() {
    private val client = OkHttpClient()
    private var androidId: String = ""
    fun obtainIMEI(contentResolver : ContentResolver) {
        val id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        this.androidId = id
    }
    fun getIMEI(): String {
        println(this.androidId)
        return this.androidId
    }

    fun sendFile(filepath: String) {
        val url = "http://192.168.1.24/dashboard/receivefile.php"
        val file = File(filepath)
        val fileBody = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, fileBody)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        handleRequest((request))
    }

    fun sendData(id: String, data: String) {
        val url = "http://192.168.1.24/dashboard/dbconfig.php"
        val formBody = FormBody.Builder()
            .add("id", id)
            .add("data", data)
            .build()
        val request = Request.Builder().url(url).post(formBody).build()
        handleRequest((request))
    }

    private fun handleRequest(request: Request) {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the error here by printing it
                println("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle the response here
                println(response.body?.string())
            }
        })
    }
}