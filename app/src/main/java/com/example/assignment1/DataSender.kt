package com.example.assignment1

import android.content.ContentResolver
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DataSender : AppCompatActivity(){
    private val client = OkHttpClient()
    private var publicKey: ByteArray? = null
    private var androidId: String = ""

    init {
        try{
            getPublicKey()
        }
        catch (e: Exception) {
            Log.e("Error:", "Error with retrieving public key from server.", e)
        }
    }
    fun obtainAndroidID(contentResolver : ContentResolver) {
        this.androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
    }
    fun getAndroidID(): String {
        return this.androidId
    }
    private fun getPublicKey() {
        // Declare a function to perform the HTTP request
        fun performHttpRequest(callback: (String?, Exception?) -> Unit) {
            val client = OkHttpClient.Builder().connectTimeout(1, TimeUnit.SECONDS).build()
            val request = Request.Builder().url("http://192.168.1.24/dashboard/publickey.php").build()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val publicKey = response.body?.string()
                    callback(publicKey, null)
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback(null, e)
                }
            })
        }

        // Call the function from a coroutine
        lifecycleScope.launch {
            try {
                performHttpRequest { publicKey, error ->
                    if (publicKey != null) {
                        this@DataSender.publicKey = Base64.decode(publicKey, Base64.DEFAULT)
                    } else {
                        Log.e("Error:", "Unable to retrieve public key from server.", error)
                    }
                }
            } catch (e: Exception) {
                Log.e("Error:", "Unable to retrieve public key from server.", e)
            }
        }
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

        // Call the function from a coroutine
        lifecycleScope.launch {
            try {
                handleRequest(request)
            } catch (e: Exception) {
                Log.e("Error:", "Failed to send file.", e)
            }
        }
    }

    fun sendData(data: String) {
        val url = "http://192.168.1.24/dashboard/dbconfig.php"

        if (this.publicKey != null) {
            try {
                // Encrypt the data
                val secretKeySpec = SecretKeySpec(this.publicKey, "AES")
                val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
                val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
                val encodedData = Base64.encodeToString(encryptedData, Base64.DEFAULT)

                val formBody = FormBody.Builder()
                    .add("id", this.androidId)
                    .add("data", encodedData)
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
            } catch (e: Exception) {
                Log.e("Error:", "Failed to encrypt the message.", e)
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
                println(response.body?.string())
            }
        })
    }
}