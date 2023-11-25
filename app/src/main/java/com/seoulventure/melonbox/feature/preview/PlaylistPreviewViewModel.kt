package com.seoulventure.melonbox.feature.preview

import androidx.lifecycle.ViewModel
import com.seoulventure.melonbox.feature.preview.data.Song
import com.seoulventure.melonbox.newList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PlaylistPreviewViewModel @Inject constructor() : ViewModel() {

    private val _songList = MutableStateFlow(fakeSongList.toImmutableList())
    val songList: StateFlow<ImmutableList<Song>> = _songList.asStateFlow()

    private val _selectedSong = MutableStateFlow<Song?>(null)
    val selectedSong = _selectedSong.asStateFlow()

    fun clearSelectedSong() {
        _selectedSong.update { null }
    }

    fun selectSong(song: Song) {
        _selectedSong.update { song }
    }

    fun replaceSong(targetSongId: String, replaceSong: Song) {
        val newList =
            _songList.value.newList { replaceAll { if (it.id == targetSongId) replaceSong else it } }
        _songList.update { newList }
        if (_selectedSong.value?.id == targetSongId) {
            _selectedSong.update { replaceSong }
        }
    }

    fun deleteSelectedSong() {
        _songList.update { it.newList { remove(_selectedSong.value) } }
        _selectedSong.update { null }
    }
}
