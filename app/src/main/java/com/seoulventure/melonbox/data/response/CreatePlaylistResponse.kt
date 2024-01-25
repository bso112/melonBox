package com.seoulventure.melonbox.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistResponse(
    @SerialName("id")
    val playlistId : String,
)