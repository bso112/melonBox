package com.seoulventure.melonbox

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberMelonBoxState(
    navController: NavHostController = rememberNavController(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
): MelonBoxAppState {
    return remember(
        navController,
        snackBarHostState,
    ) {
        MelonBoxAppState(
            navController = navController,
            snackBarHostState = snackBarHostState,
        )
    }
}

class MelonBoxAppState(
    val navController: NavHostController,
    val snackBarHostState: SnackbarHostState,
)