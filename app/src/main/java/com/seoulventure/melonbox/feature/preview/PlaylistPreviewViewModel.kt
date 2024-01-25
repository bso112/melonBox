package com.seoulventure.melonbox.feature.preview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seoulventure.melonbox.Empty
import com.seoulventure.melonbox.domain.CreatePlaylistUseCase
import com.seoulventure.melonbox.domain.GetYtPlaylistUseCase
import com.seoulventure.melonbox.domain.Song
import com.seoulventure.melonbox.feature.preview.data.SongItem
import com.seoulventure.melonbox.feature.preview.data.toUIModel
import com.seoulventure.melonbox.logE
import com.seoulventure.melonbox.newList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistPreviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getYtPlaylistUseCase: GetYtPlaylistUseCase,
    private val createPlaylistUseCase: CreatePlaylistUseCase,
) : ViewModel() {

    private val _selectedSongItem = MutableStateFlow<SongItem?>(null)
    val selectedSong = _selectedSongItem.asStateFlow()

    private val _playlistState = MutableStateFlow(PlayListUiState())
    val playlistState: StateFlow<PlayListUiState> = _playlistState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow(ARG_MELON_PLAYLIST_URL, String.Empty)
                .filter { it.isNotBlank() }
                .map {
                    getYtPlaylistUseCase(it).map(Song::toUIModel).toImmutableList()
                }
                .onStart { _playlistState.update { it.loading() } }
                .onEach { data -> _playlistState.update { it.valid(data) } }
                .catch { e -> _playlistState.update { it.error(e) } }
                .collect()
        }

        viewModelScope.launch {
            playlistState.collectLatest {
                if (it.error != null) {
                    _uiEvent.emit(UIEvent.Error(it.error))
                }
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
        val newList =
            _playlistState.value.data.newList { replaceAll { if (it.id == targetSongId) replaceSongItem else it } }
        _playlistState.update { it.valid(newList) }
        _selectedSongItem.update { replaceSongItem }
    }

    fun createPlaylist(playlistTitle: String) {
        viewModelScope.launch {
            try {
                val insertedMusicCount = createPlaylistUseCase(
                    playListTitle = playlistTitle,
                    videoIdList = playlistState.value.data.map { it.id })

                _uiEvent.emit(UIEvent.NavigateComplete(insertedMusicCount))
            } catch (e: Exception) {
                _uiEvent.emit(UIEvent.Error(e))
                logE(e.message.toString())
            }
        }
    }


    fun deleteSelectedSong() {
        val newList = _playlistState.value.data.newList { remove(_selectedSongItem.value) }
        _playlistState.update { it.valid(data = newList) }
        _selectedSongItem.update { null }
    }
}

data class PlayListUiState(
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val data: ImmutableList<SongItem> = persistentListOf(),
) {
    fun loading() = copy(isLoading = true)
    fun error(t: Throwable): PlayListUiState = copy(error = t, isLoading = false)
    fun valid(data: ImmutableList<SongItem>) = copy(data = data, isLoading = false, error = null)
}

sealed interface UIEvent {
    data class Error(val t: Throwable) : UIEvent
    data class NavigateComplete(val insertedMusicCount: Int) : UIEvent
}

