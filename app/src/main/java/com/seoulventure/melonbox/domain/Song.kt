package com.seoulventure.melonbox.domain

import com.seoulventure.melonbox.data.response.YtSearchItem

data class Song(
    val id: String,
    val name: String,
    val artistName: String
)

fun YtSearchItem.toDomain() = kotlin.runCatching {
    Song(
        id = checkNotNull(id?.videoId),
        name = checkNotNull(snippet?.title),
        artistName = checkNotNull(snippet?.description)
    )
}.getOrNull()