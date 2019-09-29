package io.agaghd.rphonesystem.jpush

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import cn.jpush.android.api.CmdMessage
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import io.agaghd.rphonesystem.flashlight.FlashLigntUtil
import io.agaghd.rphonesystem.remote.Orders
import org.json.JSONObject

class MessageReceiver : JPushMessageReceiver() {
    override fun onTagOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onTagOperatorResult(p0, p1)
    }

    override fun onCheckTagOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onCheckTagOperatorResult(p0, p1)
    }

    override fun onNotifyMessageDismiss(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageDismiss(p0, p1)
    }

    /**
     * 处理返回内容
     */
    override fun getNotification(p0: Context?, p1: NotificationMessage?): Notification {
        val content = if (p1 != null) p1.notificationContent else "{}"
        val json = JSONObject(content)
        val order = json.optString("order")
        when (order) {
            Orders.TOGGLE_ORDER -> FlashLigntUtil.toggleTouchLight()
            Orders.LAUNCH_APP -> {
                val packageName = json.optString("param")
                if(!TextUtils.isEmpty(packageName)){
                    p0?.startActivity(p0.packageManager.getLaunchIntentForPackage(packageName))
                }
            }
            else -> Log.i("wtf", content)
        }
        return super.getNotification(p0, p1)
    }

    override fun onNotifyMessageOpened(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageOpened(p0, p1)
    }

    override fun onNotifyMessageArrived(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageArrived(p0, p1)
    }

    override fun onRegister(p0: Context?, p1: String?) {
        super.onRegister(p0, p1)
    }

    override fun onCommandResult(p0: Context?, p1: CmdMessage?) {
        super.onCommandResult(p0, p1)
    }

    override fun onMessage(p0: Context?, p1: CustomMessage?) {
        super.onMessage(p0, p1)
    }

    override fun onConnected(p0: Context?, p1: Boolean) {
        super.onConnected(p0, p1)
    }

    override fun onMobileNumberOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onMobileNumberOperatorResult(p0, p1)
    }

    override fun onAliasOperatorResult(p0: Context?, p1: JPushMessage?) {
        super.onAliasOperatorResult(p0, p1)
    }

    override fun onMultiActionClicked(p0: Context?, p1: Intent?) {
        super.onMultiActionClicked(p0, p1)
    }
}