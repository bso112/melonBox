package com.seoulventure.melonbox.feature.complete

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.seoulventure.melonbox.MelonBoxAppState


private const val COMPLETE_ROUTE = "complete"

fun NavHostController.navigateComplete(builder: (NavOptionsBuilder.() -> Unit)?) {
    navigate(COMPLETE_ROUTE) {
        builder?.invoke(this)
    }

}

fun NavGraphBuilder.completeScreen(appState: MelonBoxAppState) {
    composable(COMPLETE_ROUTE) {
        CompleteScreen(appState = appState)
    }
}