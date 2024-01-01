package com.seoulventure.melonbox.feature.search

import android.os.Parcelable
import androidx.navigation.NavController
import com.seoulventure.melonbox.feature.preview.data.SongItem

class SearchScreenResult(
    private val navController: NavController
) {
    data class Argument(
        val targetSongId: String,
        val replaceSongItem: SongItem
    )

    fun setResult(argument: Argument) {
        navController.previousBackStackEntry?.savedStateHandle?.apply {
            set(KEY_TARGET_SONG, argument.targetSongId)
            set(KEY_REPLACE_SONG, argument.replaceSongItem)
        }
    }

    fun getResult(): Argument? {
        return navController.currentBackStackEntry?.savedStateHandle?.run {
            val targetSongId = get<String>(KEY_TARGET_SONG)
            val replaceSongItem = get<Parcelable>(KEY_REPLACE_SONG) as? SongItem
            if (targetSongId == null || replaceSongItem == null) return null
            Argument(
                targetSongId = targetSongId,
                replaceSongItem = replaceSongItem
            )
        }
    }

    companion object {
        private const val KEY_TARGET_SONG = "KEY_TARGET_SONG"
        private const val KEY_REPLACE_SONG = "KEY_REPLACE_SONG"
    }
}
