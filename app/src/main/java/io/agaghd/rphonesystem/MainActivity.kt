package io.agaghd.rphonesystem

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import io.agaghd.rphonesystem.flashlight.FlashLigntUtil
import io.agaghd.rphonesystem.flashlight.WifiIpHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initListeners()
        init()
    }

    fun initListeners() {
        test_light_btn.setOnClickListener { FlashLigntUtil.toggleTouchLight() }
        open_flash_light_service_btn.setOnClickListener {
            FlashLightIntentService.isRunnining = !FlashLightIntentService.isRunnining
            if (FlashLightIntentService.isRunnining) {
                startService(Intent(this, FlashLightIntentService::class.java))
            }
            open_flash_light_service_btn.text = if (FlashLightIntentService.isRunnining)
                getString(R.string.stop_flash_light_service)
            else getString(R.string.open_flash_light_service)
        }
        toggle_flash_light_btn.setOnClickListener { toggleFlashLignt() }
        save_target_ip_btn.setOnClickListener { saveTargetIp() }
        touch_flash_light_btn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> toggleFlashLignt()
                else -> Log.i("wtf", "nothing")
            }
            true

        }
    }

    @SuppressLint("SetTextI18n")
    fun init() {
        val targetIp = getTargetIp()
        Thread(Runnable {
            val addr = WifiIpHelper.getWifiIp(this@MainActivity)
            runOnUiThread {
                ip_tv.text = "本地局域网IP：$addr"
                target_ip_et.setText(if (TextUtils.isEmpty(targetIp)) addr else targetIp)
            }
        }).start()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions = arrayOf(android.Manifest.permission.CAMERA)
            requestPermissions(permissions, 0)
        }
    }

    fun toggleFlashLignt() {
        Thread(Runnable {
            val client = DatagramSocket()
            val sendBytes = FlashLigntUtil.TOGGLE_ORDER.toByteArray()
            val address = InetAddress.getByName(target_ip_et.text.toString())
            val sendPacket = DatagramPacket(sendBytes, sendBytes.size, address, 9090)
            client.send(sendPacket)
            Thread.sleep(1000)
        }).start()

    }

    fun getTargetIp(): String? {
        val sharedPreferences = getSharedPreferences("targetIp", Context.MODE_PRIVATE)
        return sharedPreferences.getString("targetIp", "")
    }

    fun saveTargetIp() {
        val sharedPreferences = getSharedPreferences("targetIp", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("targetIp", target_ip_et.text.toString())
        editor.apply()
    }

    override fun onDestroy() {
        FlashLigntUtil.camera.release()
        super.onDestroy()
    }
}
