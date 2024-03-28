package com.seoulventure.melonbox.domain

import com.seoulventure.melonbox.data.YtDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreatePlaylistUseCase @Inject constructor(
    private val ytDataSource: YtDataSource
) {
    /**
     * @return flow value indicate index of progressing video.
     */
    suspend operator fun invoke(playListTitle: String, videoIdList: List<String>): Flow<Int> =
        flow {
            val playlistId = ytDataSource.createPlaylist(playListTitle).playlistId
            videoIdList.mapIndexedNotNull { index, videoId ->
                emit(index)
                kotlin.runCatching {
                    ytDataSource.insertSongInPlaylist(
                        playlistId = playlistId,
                        videoId = videoId
                    )
                    delay(1000L) // 요청을 빠르게하면 Youtube 서버측에서 오류를 내려주기에 딜레이 추가
                }.getOrNull()
            }
        }
}