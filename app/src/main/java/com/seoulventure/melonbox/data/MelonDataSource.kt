package com.seoulventure.melonbox.data

import com.seoulventure.melonbox.data.response.MelonPlaylistResponse
import com.seoulventure.melonbox.data.response.MelonSongResponse
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MelonDataSource @Inject constructor() {
    suspend fun getMelonSongList(playlistUrl: String): MelonPlaylistResponse {
        return coroutineScope {
            val redirectQuery = Jsoup.connect(playlistUrl).apply { get() }.response().url().query
            val plylstSeq =
                redirectQuery.split("&").find { it.contains("plylstSeq") }?.split("=")?.getOrNull(1)
                    ?: return@coroutineScope MelonPlaylistResponse(data = emptyList())

            val doc = Jsoup
                .connect("https://www.melon.com/mymusic/playlist/mymusicplaylistview_listSong.htm?plylstSeq=$plylstSeq")
                .get()

            val songName = doc.select(".ellipsis .fc_gray").mapNotNull { it.text() }
            val artistName = doc.select("#artistName").mapNotNull { it.text() }

            MelonPlaylistResponse(
                songName.zip(artistName) { song, artist ->
                    MelonSongResponse(songName = song, artistName = artist)
                })
        }
    }
}