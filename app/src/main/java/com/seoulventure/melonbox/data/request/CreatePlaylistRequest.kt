package com.seoulventure.melonbox.data.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaylistRequest(
    val snippet: PlaylistSnippet,
    val status: PlaylistStatus
)

@Serializable
data class PlaylistSnippet(
    val title: String,
    val description: String,
    val tags: List<String>?,
    val defaultLanguage: String?
)

@Serializable
data class PlaylistStatus(
    val privacyStatus: String
)