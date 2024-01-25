package com.seoulventure.melonbox.domain

import com.seoulventure.melonbox.data.YtDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreatePlaylistUseCase @Inject constructor(
    private val ytDataSource: YtDataSource
){
    suspend operator fun invoke(playListTitle : String){
        ytDataSource.createPlaylist(playListTitle)
    }
}