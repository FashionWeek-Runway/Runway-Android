package com.cmc12th.runway.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

@Composable
fun SystemBroadcastReceiver(
    systemAction: String,
    onSystemEvent: (intent: Intent?) -> Unit,
) {
    val context = LocalContext.current

    // 해당 Intent.시스템액션이 들어왔을 때 사용될 람다를 안전하게 보관
    val currentOnSystemEvent by rememberUpdatedState(onSystemEvent)

    // 컴포저블이 시작될 때 Receiver을 등록하고 꺼질 때 Receiver을 종료함
    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                currentOnSystemEvent(intent)
            }
        }
        context.registerReceiver(broadcast, intentFilter)

        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

fun parseSmsMessage(bundle: Bundle): Array<SmsMessage?> {
    val objs = bundle["pdus"] as Array<*>?
    val messages: Array<SmsMessage?> = arrayOfNulls(objs!!.size)
    for (i in objs.indices) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val format = bundle.getString("format")
            messages[i] = SmsMessage.createFromPdu(objs[i] as ByteArray, format)
        } else {
            messages[i] = SmsMessage.createFromPdu(objs[i] as ByteArray)
        }
    }
    return messages
}
