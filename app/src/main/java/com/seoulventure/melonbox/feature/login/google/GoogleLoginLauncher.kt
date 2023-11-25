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


/**
 * use R.string.default_web_client_id for [androidx.activity.result.ActivityResultLauncher.launch] input
 */
fun ComponentActivity.registerForGoogleLoginResult(
    onSuccess: (FirebaseUserData) -> Unit,
    onFailure: (Throwable) -> Unit,
): ActivityResultLauncher<String> {
    return registerForActivityResult(GoogleSignInContract(onFailure)) { account ->
        if (account == null) {
            onFailure(IllegalStateException("GoogleSignInAccount is null"))
            return@registerForActivityResult
        }
        GoogleLoginUtil.googleSignIn(
            activity = this,
            account = account,
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}

@Composable
fun RegisterForGoogleLoginResult(
    onSuccess: (authCode: String) -> Unit,
    onFailure: (Throwable) -> Unit,
): ManagedActivityResultLauncher<String, GoogleSignInAccount?> {
    val activity = LocalContext.current.findActivity()
    return rememberLauncherForActivityResult(GoogleSignInContract(onFailure)) { account ->
        if (account == null) {
            onFailure(IllegalStateException("GoogleSignInAccount is null"))
            return@rememberLauncherForActivityResult
        }
        onSuccess(account.serverAuthCode.orEmpty())
//        GoogleLoginUtil.googleSignIn(
//            activity = activity,
//            account = account,
//            onSuccess = onSuccess,
//            onFailure = onFailure
//        )
    }
}

fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("no activity")
}