package com.seoulventure.melonbox.feature.preview.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Song(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val artistName: String
) : Parcelable