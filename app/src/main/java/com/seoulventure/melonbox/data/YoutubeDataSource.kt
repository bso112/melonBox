package com.seoulventure.melonbox.data

import com.seoulventure.melonbox.BuildConfig
import com.seoulventure.melonbox.data.response.YtSearchItem
import com.seoulventure.melonbox.data.response.YtSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YoutubeDataSource @Inject constructor(
    private val client: HttpClient
) {

    suspend fun search(keyword: String): YtSearchResponse {
        val token = OAuthManager.accessToken ?: error("accessToken not available")
        return client.get("https://www.googleapis.com/youtube/v3/search") {
            parameter("part", "snippet")
            parameter("maxResults", 25)
            parameter("q", keyword)
            parameter("key", BuildConfig.GOOGLE_API_KEY)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.Authorization, token)
            }
            parameter("scope", "https://www.googleapis.com/auth/youtube.readonly")
        }.body()
    }
}