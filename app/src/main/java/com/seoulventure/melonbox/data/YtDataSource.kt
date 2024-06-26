package com.seoulventure.melonbox.data

import com.seoulventure.melonbox.data.request.CreatePlaylistRequest
import com.seoulventure.melonbox.data.request.InsertSongInPlaylistRequest
import com.seoulventure.melonbox.data.request.InsertSongInPlaylistSnippet
import com.seoulventure.melonbox.data.request.PlaylistSnippet
import com.seoulventure.melonbox.data.request.PlaylistStatus
import com.seoulventure.melonbox.data.response.CreatePlaylistResponse
import com.seoulventure.melonbox.data.response.YtResourceId
import com.seoulventure.melonbox.data.response.YtSearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class YtDataSource @Inject constructor(
    private val client: HttpClient
) {

    suspend fun search(keyword: String, maxResult: Int = 1): YtSearchResponse {
        val token = OAuthManager.accessToken ?: error("accessToken not available")
        return client.get("https://www.googleapis.com/youtube/v3/search") {
            parameter("part", "snippet")
            parameter("maxResults", maxResult)
            parameter("q", keyword)
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            parameter("scope", "https://www.googleapis.com/auth/youtube.readonly")
        }.body()
    }

    suspend fun createPlaylist(playlistTitle: String): CreatePlaylistResponse {
        val token = OAuthManager.accessToken ?: error("accessToken not available")
        return client.post("https://www.googleapis.com/youtube/v3/playlists") {
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            parameter("part", "snippet,status")
            setBody(
                CreatePlaylistRequest(
                    PlaylistSnippet(
                        title = playlistTitle,
                        description = "",
                        tags = emptyList(),
                        defaultLanguage = "en"
                    ), PlaylistStatus(privacyStatus = "private")
                )
            )
        }.body()
    }

    suspend fun insertSongInPlaylist(playlistId: String, videoId: String) {
        val token = OAuthManager.accessToken ?: error("accessToken not available")
        client.post("https://www.googleapis.com/youtube/v3/playlistItems") {
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.Authorization, "Bearer $token")
            }
            parameter("part", "snippet,status")
            setBody(
                InsertSongInPlaylistRequest(
                    InsertSongInPlaylistSnippet(
                        playlistId = playlistId,
                        resourceId = YtResourceId(kind = "youtube#video", videoId = videoId)
                    )
                )
            )
        }
    }
}