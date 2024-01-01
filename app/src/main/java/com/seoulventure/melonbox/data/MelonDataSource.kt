package com.seoulventure.melonbox.data

import com.seoulventure.melonbox.data.response.MelonPlaylistResponse
import com.seoulventure.melonbox.data.response.MelonSongResponse
import io.ktor.client.HttpClient
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MelonDataSource @Inject constructor(
    private val httpClient: HttpClient
) {
    suspend fun getMelonSongList(playlistUrl: String): MelonPlaylistResponse {
        return fakeMelonPlaylistResponse
        //return httpClient.get(playlistUrl).body()
    }
}

private val fakeMelonPlaylistResponse = MelonPlaylistResponse(
    data = listOf(
        MelonSongResponse(songName = "좋니", artistName = "윤종신"),
        MelonSongResponse(songName = "좋은날", artistName = "아이유"),
        MelonSongResponse(songName = "네 생각", artistName = "존박"),
    )
)