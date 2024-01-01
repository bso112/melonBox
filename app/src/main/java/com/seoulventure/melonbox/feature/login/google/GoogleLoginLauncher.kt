package com.seoulventure.melonbox.feature.login.google

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


@Composable
fun registerForGoogleLoginResult(
    onSuccess: (authCode: String) -> Unit,
    onFailure: (Throwable) -> Unit,
): ManagedActivityResultLauncher<String, GoogleSignInAccount?> {
    return rememberLauncherForActivityResult(GoogleSignInContract(onFailure)) { account ->
        if (account == null) {
            onFailure(IllegalStateException("GoogleSignInAccount is null"))
            return@rememberLauncherForActivityResult
        }
        onSuccess(account.serverAuthCode.orEmpty())
    }
}

