package com.seoulventure.melonbox.feature.login.google

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope

class GoogleSignInContract(
    private val onFailure: (Throwable) -> Unit,
) : ActivityResultContract<String, GoogleSignInAccount?>() {

    override fun createIntent(context: Context, input: String): Intent {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(input)
            .requestScopes(Scope("https://www.googleapis.com/auth/youtube"))
            .requestEmail()
            .build()
            .let {
                GoogleSignIn.getClient(context, it)
            }.signInIntent
    }

    @Throws
    override fun parseResult(resultCode: Int, intent: Intent?): GoogleSignInAccount? {
        return kotlin.runCatching {
            GoogleSignIn.getSignedInAccountFromIntent(intent)
                .getResult(ApiException::class.java)
        }.onFailure(onFailure).getOrNull()
    }
}