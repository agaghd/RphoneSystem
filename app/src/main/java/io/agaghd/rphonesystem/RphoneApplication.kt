package io.agaghd.rphonesystem

import android.app.Application
import android.os.Handler
import cn.jpush.android.api.JPushInterface

class RphoneApplication : Application() {

    companion object {
        var appContext: RphoneApplication? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)

    }
}