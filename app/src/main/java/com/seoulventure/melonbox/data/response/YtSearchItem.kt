package com.seoulventure.melonbox.data.response

import kotlinx.serialization.Serializable

@Serializable
data class YtSearchResponse(
    val items: List<YtSearchItem>
)

@Serializable
data class YtSearchItem(
    val id: YtId? = null,
    val snippet: YtSnippet? = null,
)

@Serializable
data class YtId(
    val kind: String? = null,
    val videoId: String? = null,
    val channelId: String? = null,
    val playlistId: String? = null
)

@Serializable
data class YtSnippet(
    val publishedAt: String? = null,
    val channelId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val thumbnails: YtThumbnails? = null,
)

@Serializable
data class YtThumbnails(
    val default: YtThumbnail? = null,
)

@Serializable
data class YtThumbnail(
    val url: String? = null,
    val width: Int? = null,
    val height: Int? = null,
)