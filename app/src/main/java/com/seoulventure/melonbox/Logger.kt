package com.seoulventure.melonbox

import android.util.Log
import com.seoulventure.melonbox.util.runOnDebug

private const val TAG = "melonBox_log"

fun logD(msg: String) {
    runOnDebug {
        Log.d(TAG, msg)
    }
}

fun logE(msg: String) {
    runOnDebug {
        Log.e(TAG, msg)
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

