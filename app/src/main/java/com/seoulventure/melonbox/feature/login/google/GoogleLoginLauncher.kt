package com.seoulventure.melonbox.feature.login.google

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.seoulventure.melonbox.feature.login.data.FirebaseUserData



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

