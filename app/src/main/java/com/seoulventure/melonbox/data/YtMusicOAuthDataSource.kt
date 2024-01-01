package com.seoulventure.melonbox.data

import com.seoulventure.melonbox.data.response.YtMusicOAuthResultResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YtMusicOAuthDataSource @Inject constructor(
    private val client: HttpClient
) {
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
            parameter("grant_type", "authorization_code")
        }.body()
    }
}