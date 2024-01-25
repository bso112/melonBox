package com.seoulventure.melonbox.domain

import com.seoulventure.melonbox.data.YtDataSource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreatePlaylistUseCase @Inject constructor(
    private val ytDataSource: YtDataSource
) {
    /**
     * @return count: insert에 성공한 음악의 총 갯수
     */
    suspend operator fun invoke(playListTitle: String, videoIdList: List<String>) : Int {
        return coroutineScope {
            val playlistId = ytDataSource.createPlaylist(playListTitle).playlistId
            videoIdList.mapIndexedNotNull { index, videoId ->
                kotlin.runCatching {
                    launch {
                        ytDataSource.insertSongInPlaylist(
                            playlistId = playlistId,
                            position = index,
                            videoId = videoId
                        )
                    }.join() //insert가 index base이기 때문에 이전의 insert가 끝나고 다음 insert를 실행해야함.
                }.getOrNull()
            }.count()
        }
    }
}