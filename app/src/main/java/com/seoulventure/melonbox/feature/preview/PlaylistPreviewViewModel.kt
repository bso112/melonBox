package com.seoulventure.melonbox.feature.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seoulventure.melonbox.domain.GetYoutubePlaylistUseCase
import com.seoulventure.melonbox.domain.Song
import com.seoulventure.melonbox.feature.preview.data.SongItem
import com.seoulventure.melonbox.feature.preview.data.toUIModel
import com.seoulventure.melonbox.ifIs
import com.seoulventure.melonbox.newList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistPreviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getYoutubePlaylistUseCase: GetYoutubePlaylistUseCase
) : ViewModel() {

    private val _selectedSongItem = MutableStateFlow<SongItem?>(null)
    val selectedSong = _selectedSongItem.asStateFlow()

    private val _playlistState = MutableStateFlow<PlayListState>(PlayListState.Init)
    val playlistState: StateFlow<PlayListState> = _playlistState.asStateFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow(ARG_MELON_PLAYLIST_URL, "")
                .filter { it.isNotBlank() }
                .onStart { _playlistState.update { PlayListState.Loading } }
                .map { getYoutubePlaylistUseCase(it).map(Song::toUIModel).toImmutableList() }
                .catch { error -> _playlistState.update { PlayListState.Error(error) } }
                .collect { list ->
                    _playlistState.update { PlayListState.Success(list) }
                }
        }

    }

    fun clearSelectedSong() {
        _selectedSongItem.update { null }
    }

    fun selectSong(songItem: SongItem) {
        _selectedSongItem.update { songItem }
    }

    fun replaceSong(targetSongId: String, replaceSongItem: SongItem) {
        playlistState.value.ifIs<PlayListState.Success> { state ->
            val newList =
                state.data.newList { replaceAll { if (it.id == targetSongId) replaceSongItem else it } }
            _playlistState.update { state.copy(data = newList) }
            if (_selectedSongItem.value?.id == targetSongId) {
                _selectedSongItem.update { replaceSongItem }
            }
        }
    }


    fun deleteSelectedSong() {
        playlistState.value.ifIs<PlayListState.Success> { state ->
            val newList = state.data.newList { remove(_selectedSongItem.value) }
            _playlistState.update { state.copy(data = newList) }
            _selectedSongItem.update { null }
        }
    }
}

sealed interface PlayListState {
    data class Error(val t: Throwable) : PlayListState
    object Loading : PlayListState
    object Init : PlayListState
    data class Success(val data: ImmutableList<SongItem>) : PlayListState
}
