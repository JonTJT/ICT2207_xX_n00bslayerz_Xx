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
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class DataSender() {
    private val client = OkHttpClient()
    private var androidId: String = ""
    fun obtainAndroidID(contentResolver : ContentResolver) {
        val id = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        this.androidId = id
    }
    fun getAndroidID(): String {
        return this.androidId
    }

    private fun getPublicKey(): ByteArray? {

        try {
            val client = OkHttpClient()
            val request = Request.Builder().url("http://192.168.1.24/dashboard/publickey.php").build()
            val response = client.newCall(request).execute()

            // Get the public key
            val publicKey = response.body?.string()
            if (publicKey == null) {
                Log.d("Error:", "Unable to retrieve the public key.")
                return null
            }
            return Base64.decode(publicKey, Base64.DEFAULT)
        }
         catch (e: Exception) {
            Log.e("Error:", "Unable to get public key.", e)
             return null
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

        handleRequest((request))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendData(id: String, data: String) {
        val url = "http://192.168.1.24/dashboard/dbconfig.php"
        val publicKey = getPublicKey()

        if (publicKey != null) {
            try {
                // Encrypt the data
                val secretKeySpec = SecretKeySpec(publicKey, "AES")
                val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
                val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
                val encodedData = Base64.encodeToString(encryptedData, Base64.DEFAULT)

                val formBody = FormBody.Builder()
                    .add("id", id)
                    .add("data", encodedData)
                    .build()
                val request = Request.Builder().url(url).post(formBody).build()
                handleRequest((request))

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