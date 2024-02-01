package com.seoulventure.melonbox.domain

import com.seoulventure.melonbox.data.YtDataSource
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
    suspend operator fun invoke(playListTitle: String, videoIdList: List<String>): Int {
        return coroutineScope {
            val playlistId = ytDataSource.createPlaylist(playListTitle).playlistId
            videoIdList.mapIndexedNotNull { index, videoId ->
                kotlin.runCatching {
                    launch {
                        ytDataSource.insertSongInPlaylist(
                            playlistId = playlistId,
                            videoId = videoId
                        )
                    }.join() // 곡 하나의 insert 요청-응답이 완전히 끝난 뒤 다음 요청을 해야한다
                    delay(500L) // 요청을 빠르게하면 Youtube 서버측에서 오류를 내려주기에 딜레이 추가
                }.getOrNull()
            }.count()
        }
    }
}