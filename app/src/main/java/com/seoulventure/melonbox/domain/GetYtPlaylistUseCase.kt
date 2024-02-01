package com.seoulventure.melonbox.domain

import com.seoulventure.melonbox.data.MelonDataSource
import com.seoulventure.melonbox.data.YtDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetYtPlaylistUseCase @Inject constructor(
    private val melonDataSource: MelonDataSource,
    private val ytDataSource: YtDataSource
) {
    suspend operator fun invoke(melonPlaylistUrl: String): List<Song> {
        return withContext(Dispatchers.IO) {
            melonDataSource.getMelonSongList(melonPlaylistUrl)
                .data
                .map {
                    async { ytDataSource.search("${it.songName} ${it.artistName}") }
                }
                .awaitAll()
                .mapNotNull { it.items.firstOrNull()?.toDomain() }
        }

    }
}