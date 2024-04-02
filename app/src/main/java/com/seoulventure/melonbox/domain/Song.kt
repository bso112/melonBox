package com.seoulventure.melonbox.domain

import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import com.seoulventure.melonbox.data.response.YtSearchItem

data class Song(
    val id: String,
    val name: String,
    val artistName: String
)

fun YtSearchItem.toDomain() = kotlin.runCatching {
    Song(
        id = checkNotNull(id?.videoId),
        //특수문자 처리를 위해 html unescape
        name = Html.fromHtml(checkNotNull(snippet?.title), FROM_HTML_MODE_LEGACY).toString(),
        artistName = Html.fromHtml(checkNotNull(snippet?.description), FROM_HTML_MODE_LEGACY).toString()
    )
}.getOrNull()