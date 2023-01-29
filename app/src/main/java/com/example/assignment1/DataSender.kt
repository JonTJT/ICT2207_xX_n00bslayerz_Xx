package com.example.assignment1

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException


class DataSender() {
    private val client = OkHttpClient()

    fun getIMEI(contentResolver : ContentResolver): String {
        val androidId: String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Log.d("id",androidId)
        return androidId
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

    fun sendData(name: String, email: String) {
        val url = "http://192.168.1.24/dashboard/dbconfig.php"
        val formBody = FormBody.Builder()
            .add("name", name)
            .add("email", email)
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