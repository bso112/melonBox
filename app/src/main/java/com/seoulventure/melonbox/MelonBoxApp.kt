package com.seoulventure.melonbox

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MelonBoxApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}