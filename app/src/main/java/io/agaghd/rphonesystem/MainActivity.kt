package io.agaghd.rphonesystem

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import cn.jpush.android.api.JPushInterface
import io.agaghd.rphonesystem.flashlight.FlashLightIntentService
import io.agaghd.rphonesystem.flashlight.FlashLigntUtil
import io.agaghd.rphonesystem.flashlight.WifiIpHelper
import io.agaghd.rphonesystem.remote.Orders
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    var phoneIp = ""

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
        toggle_flash_light_remote_btn.setOnClickListener {
            sendOrderToServer(
                Orders.TOGGLE_ORDER,
                "a", "flash_light_" + target_ip_et.text
            )
        }
        touch_flash_light_remote_btn.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> sendOrderToServer(
                    Orders.TOGGLE_ORDER,
                    "b", "flash_light_" + target_ip_et.text
                )
                else -> Log.i("wtf", "nothing")
            }
            true
        }
        save_app_name_btn.setOnClickListener { saveAppName() }
    }

    @SuppressLint("SetTextI18n")
    fun init() {
        val targetIp = getTargetIp()
        val appName = getAppName()
        app_name_et.setText(appName)
        Thread(Runnable {
            phoneIp = WifiIpHelper.getWifiIp(this@MainActivity)
            runOnUiThread {
                ip_tv.text = "本地局域网IP：$phoneIp"
                target_ip_et.setText(if (TextUtils.isEmpty(targetIp)) phoneIp else targetIp)
                Handler().postDelayed({
                    JPushInterface.setAlias(this, 1, "flash_light")
                    val set = HashSet<String>()
                    set.add("flash_light_$phoneIp")
                    Log.i("jiguang", "flash_light_$phoneIp")
                    JPushInterface.setTags(this, 1, set)
                }, 3000)
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
            val sendBytes = Orders.TOGGLE_ORDER.toByteArray()
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

    fun getAppName(): String? {
        val sharedPreferences = getSharedPreferences("appName", Context.MODE_PRIVATE)
        return sharedPreferences.getString("appName", "")
    }

    fun saveAppName() {
        val sharedPreferences = getSharedPreferences("appName", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("appName", app_name_et.text.toString())
        editor.apply()
        sendOrderToServer(Orders.LAUNCH_APP, app_name_et.text.toString(), "flash_light_" + target_ip_et.text)
    }


    fun sendOrderToServer(order: String, param: String, vararg tags: String) {
        val url = BuildConfig.CLIENTIP
        val client = OkHttpClient()
        val json = JSONObject()
        json.put("order", order)
        val tag = JSONArray()
        for (t in tags) {
            tag.put(t)
        }
        tag.put("flash_light_" + target_ip_et.text)
        json.put("param", param)
        json.put("tag", tag)
        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())
        val request = Request.Builder().url(url).post(body).build()
        Thread(Runnable {
            try {
                val response = client.newCall(request).execute()
                Log.i("wtf", response.body.toString())
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }).start()
    }

    override fun onDestroy() {
        FlashLigntUtil.camera.release()
        super.onDestroy()
    }
}
