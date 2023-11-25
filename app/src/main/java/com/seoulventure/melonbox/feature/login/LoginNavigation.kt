package com.seoulventure.melonbox.feature.login

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.seoulventure.melonbox.MelonBoxAppState


const val LOGIN_ROUTE = "login"

fun NavHostController.navigateLogin(navOptions: NavOptions? = null) {
    navigate(LOGIN_ROUTE, navOptions)
}

fun NavGraphBuilder.loginScreen(appState: MelonBoxAppState) {
    composable(LOGIN_ROUTE) {
        LoginScreen(appState = appState)
    }
}