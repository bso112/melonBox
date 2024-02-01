package com.seoulventure.melonbox.data.request

import com.seoulventure.melonbox.data.response.YtResourceId
import kotlinx.serialization.Serializable

@Serializable
data class InsertSongInPlaylistRequest(
    val snippet: InsertSongInPlaylistSnippet
)

@Serializable
data class InsertSongInPlaylistSnippet(
    val playlistId: String,
    val resourceId: YtResourceId
)
