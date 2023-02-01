@file:Suppress("DEPRECATION")

package com.example.assignment1.DataRetriever

import android.os.AsyncTask
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

class MessageSender : AsyncTask<String?, Void?, Void?>() {
    var s: Socket? = null
    var pw: PrintWriter? = null

    override fun doInBackground(vararg voids: String?): Void? {
        val message = voids[0]
        val ip = voids[1]
        val port = voids[2]!!.toInt()
        try {
            s = Socket(ip, port)
            pw = PrintWriter(s!!.getOutputStream())
            pw!!.write("""$message""".trimIndent())
            pw!!.flush()
            s!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}