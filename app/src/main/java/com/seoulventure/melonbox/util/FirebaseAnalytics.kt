package com.seoulventure.melonbox.util

import androidx.core.os.bundleOf
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object FirebaseAnalytics {
    private val analytics get() = Firebase.analytics

    fun logEvent(event: FirebaseEvent) {
        analytics.logEvent(event.name, null)
    }

    fun logError(throwable: Throwable) {
        analytics.logEvent(FirebaseEvent.Error.name, bundleOf("error" to throwable))
    }
}


enum class FirebaseEvent {
    Login,
    ClickTutorial,
    ClickPutMelon,
    ClickSong,
    ClickCreatePlaylist,
    ClickServiceDesk,
    Error,
}
