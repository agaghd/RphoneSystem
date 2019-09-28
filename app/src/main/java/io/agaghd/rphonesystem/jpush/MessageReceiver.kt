package io.agaghd.rphonesystem.jpush

import android.content.Context
import android.util.Log
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.service.JPushMessageReceiver

class MessageReceiver : JPushMessageReceiver() {
    override fun onTagOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onTagOperatorResult(p0, p1)
        Log.i("jiguang", p1.toString())
    }
}