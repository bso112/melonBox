package com.seoulventure.melonbox.feature.preview.data

import android.net.Uri
import android.os.Parcelable
import com.seoulventure.melonbox.domain.Song
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class SongItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val artistName: String
) : Parcelable

fun Song.toUIModel() = SongItem(
    id = id,
    name = Uri.decode(name),
    artistName = Uri.decode(artistName)
)


