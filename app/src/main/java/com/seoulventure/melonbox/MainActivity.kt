package com.seoulventure.melonbox

import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import com.seoulventure.melonbox.feature.complete.completeScreen
import com.seoulventure.melonbox.feature.login.LOGIN_ROUTE
import com.seoulventure.melonbox.feature.login.loginScreen
import com.seoulventure.melonbox.feature.main.mainScreen
import com.seoulventure.melonbox.feature.preview.playlistPreview
import com.seoulventure.melonbox.feature.search.searchScreen
import com.seoulventure.melonbox.ui.theme.MelonBoxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val imm: InputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        return super.dispatchTouchEvent(ev)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MelonBoxAppCompose()
        }
    }
}

@Composable
fun MelonBoxAppCompose(
    appState: MelonBoxAppState = rememberMelonBoxState(),
) {
    val context = LocalContext.current

    MelonBoxTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MelonBoxTheme.colors.background),
            snackbarHost = { SnackbarHost(appState.snackBarHostState) },
        ) { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = appState.navController,
                startDestination = LOGIN_ROUTE,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                loginScreen(appState = appState)
                mainScreen(appState = appState)
                playlistPreview(appState = appState)
                searchScreen(appState = appState)
                completeScreen(appState = appState)
            }
        }
    }
}
