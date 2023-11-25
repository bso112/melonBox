package com.seoulventure.melonbox

import android.util.Log

private const val TAG = "melonBox_log"

fun logD(msg: String) {
    Log.d(TAG, msg)
}

fun logE(msg: String) {
    Log.e(TAG, msg)
}

fun logI(msg: String) {
    Log.i(TAG, msg)
}

fun logW(msg: String) {
    Log.w(TAG, msg)
}