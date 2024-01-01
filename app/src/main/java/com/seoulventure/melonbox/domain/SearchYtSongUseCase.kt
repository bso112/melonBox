package com.seoulventure.melonbox.domain

import com.seoulventure.melonbox.data.YtDataSource
import com.seoulventure.melonbox.data.response.YtSearchItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchYtSongUseCase @Inject constructor(
    private val ytDataSource: YtDataSource
) {
    suspend operator fun invoke(keyword: String, maxResult: Int): List<Song> {
        return ytDataSource.search(keyword, maxResult).items.mapNotNull(YtSearchItem::toDomain)
    }
}