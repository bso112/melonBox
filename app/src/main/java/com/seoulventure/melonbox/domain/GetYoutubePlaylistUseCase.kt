package com.seoulventure.melonbox.domain

import com.seoulventure.melonbox.data.MelonDataSource
import com.seoulventure.melonbox.data.YoutubeDataSource
import com.seoulventure.melonbox.data.response.YtSearchItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetYoutubePlaylistUseCase @Inject constructor(
    private val melonDataSource: MelonDataSource,
    private val ytDataSource: YoutubeDataSource
) {
    suspend operator fun invoke(melonPlaylistUrl: String): List<Song> {
        return coroutineScope {
            melonDataSource.getMelonSongList(melonPlaylistUrl)
                .data
                .map {
                    async { ytDataSource.search(it.songName) }
                }
                .awaitAll()
                .mapNotNull { it.items.firstOrNull()?.toDomain() }
        }

    }
}