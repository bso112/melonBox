package com.seoulventure.melonbox.feature.preview

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.seoulventure.melonbox.MelonBoxAppState


private const val PLAYLIST_PREVIEW_ROUTE = "playlist_preview"

fun NavHostController.navigatePlaylistPreview(navOptions: NavOptions? = null) {
    navigate(PLAYLIST_PREVIEW_ROUTE, navOptions)
}

fun NavGraphBuilder.playlistPreview(appState: MelonBoxAppState) {
    composable(PLAYLIST_PREVIEW_ROUTE) {
        PlaylistPreviewScreen(appState = appState)
    }
}