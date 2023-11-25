package com.seoulventure.melonbox.domain

data class YtMusicOAuthData(
    val deviceCode: String,
    val userCode: String,
    val verificationUrl: String,
)

data class YtMusicOAuthResult(
    val accessToken: String,
    val expireIn: Long,
    val scope: String,
    val tokenType: String,
    val refreshToken: String
)