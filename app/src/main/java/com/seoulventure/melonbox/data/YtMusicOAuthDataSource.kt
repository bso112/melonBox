package com.seoulventure.melonbox.data

import android.net.Uri
import com.seoulventure.melonbox.domain.YtMusicOAuthData
import com.seoulventure.melonbox.domain.YtMusicOAuthResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton


@Serializable
data class YtMusicOAuthDataResponse(
    @SerialName("device_code")
    val deviceCode: String?,
    @SerialName("user_code")
    val userCode: String?,
    @SerialName("verification_url")
    val verificationUrl: String?,
    @SerialName("expires_in")
    val expiresIn: Long?,
    @SerialName("interval")
    val interval: Int?
) {
    fun toDomain(): YtMusicOAuthData? = kotlin.runCatching {
        YtMusicOAuthData(
            deviceCode = checkNotNull(deviceCode),
            userCode = checkNotNull(userCode),
            verificationUrl = checkNotNull(verificationUrl),
        )
    }.getOrNull()
}

@Serializable
data class YtMusicOAuthResultResponse(
    @SerialName("access_token")
    val accessToken: String?,
    @SerialName("expires_in")
    val expireIn: Long?,
    @SerialName("scope")
    val scope: String?,
    @SerialName("token_type")
    val tokenType: String?,
    @SerialName("refresh_token")
    val refreshToken: String?
) {
    fun toDomain(): YtMusicOAuthResult? = kotlin.runCatching {
        YtMusicOAuthResult(
            accessToken = checkNotNull(accessToken),
            expireIn = checkNotNull(expireIn),
            scope = checkNotNull(scope),
            tokenType = checkNotNull(tokenType),
            refreshToken = checkNotNull(refreshToken)
        )
    }.getOrNull()
}

@Singleton
class YtMusicOAuthDataSource @Inject constructor(
    private val client: HttpClient
) {

    suspend fun requestOAuth(clientId: String): YtMusicOAuthDataResponse {
        return client.post("https://oauth2.googleapis.com/device/code") {
            contentType(ContentType.Application.FormUrlEncoded)
            parameter("client_id", clientId)
            parameter("scope", "https://www.googleapis.com/auth/youtube.readonly")
        }.body()
    }

    suspend fun getOAuthResult(
        clientId: String,
        clientSecret: String,
        authorizationCode: String
    ): YtMusicOAuthResultResponse {
        return client.post("https://oauth2.googleapis.com/token") {
            contentType(ContentType.Application.FormUrlEncoded)
            parameter("client_id", clientId)
            parameter("client_secret", clientSecret)
            parameter("code", authorizationCode)
            parameter("redirect_uri", "https://www.naver.com/")
            parameter("grant_type", "authorization_code")
        }.body()
    }
}