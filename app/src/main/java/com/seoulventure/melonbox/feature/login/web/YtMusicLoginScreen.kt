package com.seoulventure.melonbox.feature.login.web

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seoulventure.melonbox.BuildConfig
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.logD


inline fun <reified T : Any> Any.ifIs(onSuccess: (T) -> Unit) {
    if (this is T) {
        onSuccess(this)
    }
}

@Composable
fun YtMusicLoginScreen(
    appState: MelonBoxAppState,
    viewModel: YtMusicLoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val oauthResponse by viewModel.oAuthResponse.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.requestAuth()
    }

    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                //check if user authentication is done
                if (oauthResponse !is YtMusicOAuthState.Success) {
                    oauthResponse.ifIs<YtMusicOAuthState.Progressing> {
                        logD("getOAuthResult")
                        viewModel.getOAuthResult(
                            clientId = BuildConfig.GOOGLE_OAUTH_CLIENT_ID,
                            clientSecret = BuildConfig.GOOGLE_OAUTH_CLIENT_SECRET,
                            deviceCode = it.data.deviceCode
                        )
                    }
                }
            }

            else -> {}
        }
    }


    when (val oauth = oauthResponse) {
        is YtMusicOAuthState.Loading -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(text = "로딩중...", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        }

        is YtMusicOAuthState.Progressing -> {
            val logInUrl = oauth.data.verificationUrl
            val userCode = oauth.data.userCode

            LaunchedEffect(oauth.data.userCode) {
                val snackbarResult = appState.snackBarHostState.showSnackbar(
                    userCode,
                    actionLabel = context.getString(R.string.action_copy),
                )
                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    clipboardManager.setText(AnnotatedString(userCode))
                    val intent = CustomTabsIntent.Builder()
                        .build()
                    intent.launchUrl(context, Uri.parse(logInUrl))
                }
            }
        }

        is YtMusicOAuthState.Error -> {
            oauth.e.printStackTrace()
            LaunchedEffect(Unit) {
                val snackBarResult = appState.snackBarHostState.showSnackbar(
                    context.getString(R.string.error_fail_oauth),
                    context.getString(R.string.label_fail_oauth)
                )

                if (snackBarResult == SnackbarResult.ActionPerformed) {
                    viewModel.requestAuth()
                }
            }
        }

        is YtMusicOAuthState.Success -> {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
                Text(text = "성공!!", fontSize = 30.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

