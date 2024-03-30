package com.seoulventure.melonbox.data

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.internal.ConfigFetchHandler
import com.seoulventure.melonbox.BuildConfig
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.logE
import javax.inject.Inject


class RemoteConfigDataSource @Inject constructor() {
    private val remoteConfig: FirebaseRemoteConfig by lazy {
        FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(if (BuildConfig.DEBUG) 0 else ConfigFetchHandler.DEFAULT_MINIMUM_FETCH_INTERVAL_IN_SECONDS)
                .build()
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_default)
            fetchAndActivate()
                .addOnFailureListener { logE(it) }
        }

    }


    fun getTutorialUrl(): String {
        return remoteConfig.getString("tutorial_url")
    }
}