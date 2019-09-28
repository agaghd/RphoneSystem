package io.agaghd.rphonesystem.flashlight

import android.content.Context
import android.net.wifi.WifiManager

object WifiIpHelper {

    fun getWifiIp(context: Context): String {
        val wifiManager:WifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var ip = ""
        if(!wifiManager.isWifiEnabled){
            return ip
        }
        val wi = wifiManager.connectionInfo ?: return ip
        val ipAdd = wi.ipAddress ?: return ip
        ip = intToIp(ipAdd)
        return ip
    }

    fun intToIp(i: Int): String {
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)
    }
}