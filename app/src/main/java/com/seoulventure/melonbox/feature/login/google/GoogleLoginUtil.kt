package com.seoulventure.melonbox.feature.login.google

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.seoulventure.melonbox.feature.login.data.FirebaseUserData
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.lang.IllegalStateException
import java.util.concurrent.CountDownLatch


internal object GoogleLoginUtil {

    fun googleSignIn(
        activity: Activity,
        account: GoogleSignInAccount,
        onSuccess: (user: FirebaseUserData) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        /**
         * ID token is a JSON Web Token signed by Google that can be used to identify a user to a backend.
         */
        val googleIdToken = account.idToken.takeIf { it?.isNotBlank() == true } ?: kotlin.run {
            onFailure(IllegalStateException("GoogleAccount idToken is Null or Blank"))
            return
        }

        val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnSuccessListener(activity) { authResult ->
                val user = authResult.user ?: kotlin.run {
                    onFailure(IllegalStateException("Firebase user is null"))
                    return@addOnSuccessListener
                }
                user.getIdToken(true).addOnSuccessListener { result ->
                    FirebaseUserData(
                        email = user.email.orEmpty(),
                        googleAuthCode = account.serverAuthCode.orEmpty(),
                        idToken = result.token.orEmpty(),
                        displayName = user.displayName.orEmpty(),
                        phoneNumber = user.phoneNumber.orEmpty(),
                        photoUrl = user.photoUrl.toString(),
                        isEmailVerified = user.isEmailVerified,
                        isAnonymous = user.isAnonymous,
                        providerId = user.providerId,
                        uid = user.uid
                    ).also(onSuccess)
                }
            }.addOnFailureListener(onFailure)
    }

    fun signOut(activity: Activity) {
        Firebase.auth.signOut()
        val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val client = GoogleSignIn.getClient(activity, opt)
        client.signOut()
        client.revokeAccess()
    }

    fun isUserLogin(): Boolean {
        return (Firebase.auth.currentUser != null)
    }


    fun refreshIdToken() = callbackFlow {
        Firebase.auth.currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
            trySend(result.token.orEmpty())
        }?.addOnFailureListener {
            close(it)
        }
        awaitClose()
    }

    /**
     * 토큰을 리프레시할 동안 현재 스레드를 블록한다.
     * ANR이 발생할 수 있으니 주의해서 사용해야한다.
     */
    fun refreshIdTokenSync(onFailure: (Exception) -> Unit): String {
        val lock = CountDownLatch(1)
        var token = ""
        Firebase.auth.currentUser?.getIdToken(true)?.addOnSuccessListener { result ->
            token = result.token.orEmpty()
            lock.countDown()
        }?.addOnFailureListener {
            onFailure(it)
            lock.countDown()
        }
        lock.await()
        return token
    }

}