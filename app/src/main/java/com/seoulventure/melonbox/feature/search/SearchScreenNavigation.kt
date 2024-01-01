package com.seoulventure.melonbox.feature.search

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
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
    navigate("$SEARCH_ROUTE/$songId/${Uri.encode(keyword)}", navOptions)
}

fun NavGraphBuilder.searchScreen(appState: MelonBoxAppState) {
    composable("$SEARCH_ROUTE/{$ARG_SONG_ID}/{$ARG_KEYWORD}") {
        SearchScreen(
            appState = appState
        )
    }
}