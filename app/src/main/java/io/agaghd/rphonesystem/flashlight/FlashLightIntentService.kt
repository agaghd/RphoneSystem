package io.agaghd.rphonesystem.flashlight

import android.app.IntentService
import android.content.Intent
import android.util.Log
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class FlashLightIntentService : IntentService("FlashLightIntentService") {

    companion object {
        var isRunnining = false
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.i("wtf", "FlashLight Service Start")

        val addr = InetAddress.getByName(WifiIpHelper.getWifiIp(this@FlashLightIntentService))
        val service = DatagramSocket(9090, addr)
        val receiveBytes = ByteArray(1024)
        val receivePacket = DatagramPacket(receiveBytes, receiveBytes.size)
        while (isRunnining) {
            try {
                service.receive(receivePacket)
                val receiveMsg = String(receivePacket.data, 0, receivePacket.length)
                Log.i("wtf", "receiveMsg = $receiveMsg")
                if (FlashLigntUtil.TOGGLE_ORDER == receiveMsg) {
                    FlashLigntUtil.toggleTouchLight()
                }
            } catch (e: Exception) {
                Log.e("wtf", e.message)
            }
        }
        service.close()
        Log.i("wtf", "FlashLight Service Stop")
    }


}
