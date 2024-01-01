package com.seoulventure.melonbox.feature.preview

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.seoulventure.melonbox.MelonBoxAppState


private const val PLAYLIST_PREVIEW_ROUTE = "playlist_preview"
const val ARG_MELON_PLAYLIST_URL = "ARG_MELON_PLAYLIST_URL"

fun NavHostController.navigatePlaylistPreview(
    melonPlaylistUrl: String,
    navOptions: NavOptions? = null
) {
    navigate("$PLAYLIST_PREVIEW_ROUTE/$melonPlaylistUrl", navOptions)
}

fun NavGraphBuilder.playlistPreview(appState: MelonBoxAppState) {
    composable("$PLAYLIST_PREVIEW_ROUTE/{$ARG_MELON_PLAYLIST_URL}") {
        PlaylistPreviewScreen(appState = appState)
    }
}