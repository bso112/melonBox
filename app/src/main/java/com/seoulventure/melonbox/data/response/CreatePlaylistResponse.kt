package com.seoulventure.melonbox.data.response

import com.seoulventure.melonbox.data.request.PlaylistSnippet
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistResponse(
    @SerialName("id")
    val playlistId : String,
)