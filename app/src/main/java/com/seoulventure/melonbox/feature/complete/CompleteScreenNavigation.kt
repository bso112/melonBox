package com.seoulventure.melonbox.feature.complete

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.seoulventure.melonbox.MelonBoxAppState


private const val COMPLETE_ROUTE = "complete"
const val ARG_INSERTED_SONG_COUNT = "ARG_INSERTED_SONG_COUNT"

fun NavHostController.navigateComplete(
    insertedSongCount: Int,
    builder: (NavOptionsBuilder.() -> Unit)?
) {
    navigate("$COMPLETE_ROUTE/$insertedSongCount") {
        builder?.invoke(this)
    }

}

fun NavGraphBuilder.completeScreen(appState: MelonBoxAppState) {
    composable(
        "$COMPLETE_ROUTE/{$ARG_INSERTED_SONG_COUNT}", arguments = listOf(navArgument(
            ARG_INSERTED_SONG_COUNT
        ) { type = NavType.IntType })
    ) {
        CompleteScreen(
            appState = appState,
            insertedSongCount = it.arguments?.getInt(ARG_INSERTED_SONG_COUNT) ?: 0
        )
    }
}