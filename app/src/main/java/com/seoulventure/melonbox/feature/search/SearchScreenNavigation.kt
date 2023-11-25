package com.seoulventure.melonbox.feature.search

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.navigation.*
import androidx.navigation.compose.composable
import com.seoulventure.melonbox.MelonBoxAppState


private const val SEARCH_ROUTE = "search"
const val ARG_SONG_ID = "ARG_SONG_ID"
const val ARG_KEYWORD = "ARG_KEYWORD"


fun NavHostController.navigateSearch(
    songId: String,
    keyword: String,
    navOptions: NavOptions? = null
) {
    navigate("$SEARCH_ROUTE/$songId/$keyword", navOptions)
}

fun NavGraphBuilder.searchScreen(appState: MelonBoxAppState) {
    composable("$SEARCH_ROUTE/{$ARG_SONG_ID}/{$ARG_KEYWORD}") {
        SearchScreen(
            appState = appState
        )
    }
}