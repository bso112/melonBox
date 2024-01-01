package com.seoulventure.melonbox.data.response

data class MelonPlaylistResponse(
    val data: List<MelonSongResponse>
)

data class MelonSongResponse(
    val songName: String,
    val artistName: String
)
