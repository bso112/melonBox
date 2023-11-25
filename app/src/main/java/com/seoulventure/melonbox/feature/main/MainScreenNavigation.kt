package com.seoulventure.melonbox.feature.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.seoulventure.melonbox.MelonBoxAppState

const val MAIN_ROUTE = "main"

fun NavHostController.navigateMain(navOptions: NavOptions? = null) {
    navigate(MAIN_ROUTE, navOptions)
}

fun NavGraphBuilder.mainScreen(appState: MelonBoxAppState) {
    composable(MAIN_ROUTE) {
        MainScreen(appState = appState)
    }
}