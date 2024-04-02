package com.seoulventure.melonbox

import android.util.Log
import com.seoulventure.melonbox.util.FirebaseAnalytics
import com.seoulventure.melonbox.util.runOnDebug

private const val TAG = "melonBox_log"

fun logD(msg: String) {
    runOnDebug {
        Log.d(TAG, msg)
    }
}

fun logE(msg: String, throwable: Throwable? = null) {
    runOnDebug {
        Log.e(TAG, msg)
        throwable?.printStackTrace()
    }
    FirebaseAnalytics.logError(throwable ?: return)
}

fun logE(throwable: Throwable) {
    runOnDebug {
        Log.e(TAG, throwable.message.toString())
        throwable.printStackTrace()
    }
}

fun logI(msg: String) {
    runOnDebug {
        Log.i(TAG, msg)
    }
}

fun logW(msg: String) {
    runOnDebug {
        Log.w(TAG, msg)
    }
}

