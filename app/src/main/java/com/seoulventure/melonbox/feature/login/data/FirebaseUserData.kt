package com.seoulventure.melonbox.feature.login.data

data class FirebaseUserData(
    val idToken: String,
    val googleAuthCode: String,
    val email: String,
    val displayName: String,
    val phoneNumber: String,
    val photoUrl: String,
    val isEmailVerified: Boolean,
    val isAnonymous: Boolean,
    val providerId: String,
    val uid: String
)