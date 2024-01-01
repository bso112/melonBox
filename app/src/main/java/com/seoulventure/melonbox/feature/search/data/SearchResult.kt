package com.seoulventure.melonbox.feature.search.data

import com.seoulventure.melonbox.data.response.YtSearchItem
import com.seoulventure.melonbox.domain.Song

data class SongSearchUIModel(
    val songName: String,
    val artistName: String,
)

fun Song.toSearchUIModel() = SongSearchUIModel(
    songName = name,
    artistName = artistName
)