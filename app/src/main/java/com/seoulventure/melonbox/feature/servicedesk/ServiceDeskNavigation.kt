package com.seoulventure.melonbox.feature.servicedesk

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.seoulventure.melonbox.MelonBoxAppState

private const val SERVICE_DESK_ROUTE = "SERVICE_DESK_ROUTE"

fun NavHostController.navigateServiceDesk(
    navOptions: NavOptions? = null
) {
    navigate(SERVICE_DESK_ROUTE, navOptions)
}

fun NavGraphBuilder.serviceDeskScreen(appState: MelonBoxAppState) {
    composable(SERVICE_DESK_ROUTE) {
        ServiceDeskScreen(appState = appState)
    }
}