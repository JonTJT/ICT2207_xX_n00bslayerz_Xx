package com.example.assignment1

import android.content.Context
import android.net.Uri
import java.util.*


class smstest(var context: Context) {
    fun readSMSBox(box: String): String {

        println("Debug test 2")

        val SMSURI = Uri.parse("content://sms/$box")
        val cur = context.contentResolver.query(SMSURI, null, null, null, null)
        var sms = ""
        try {
            if (cur!!.moveToFirst()) {
                for (i in 0 until cur.count) {
                    val iterator = i.toString()
                    val number = cur.getString(cur.getColumnIndexOrThrow("address"))
                    var date = cur.getString(cur.getColumnIndexOrThrow("date"))
                    val person = cur.getString(cur.getColumnIndexOrThrow("person"))
                    val epoch = date.toLong()
                    val fDate = Date(epoch * 1000)
                    date = fDate.toString()
                    val body = cur.getString(cur.getColumnIndexOrThrow("body"))
                    val fi =
                        "#$iterator\nNumber : $number\nPerson : $person\nDate : $fDate\nBody : $body\n"
                    sms += """
                        $fi
                        
                        """.trimIndent()
                    //sms += "[" + number + ":" + date + "]" + body + "\n";
                    cur.moveToNext()
                }
                sms += "\n"
            }
        } catch (npe: NullPointerException) {
            return ""
        }
        return sms
    }
}