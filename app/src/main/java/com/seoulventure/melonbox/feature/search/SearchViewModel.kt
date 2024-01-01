package com.seoulventure.melonbox.feature.search

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seoulventure.melonbox.Empty
import com.seoulventure.melonbox.domain.SearchYtSongUseCase
import com.seoulventure.melonbox.domain.Song
import com.seoulventure.melonbox.feature.search.data.SongSearchUIModel
import com.seoulventure.melonbox.feature.search.data.toSearchUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    searchYtSongUseCase: SearchYtSongUseCase
) : ViewModel() {

    val targetSongId = savedStateHandle.get<String>(ARG_SONG_ID).orEmpty()
    private val initialKeyword =
        savedStateHandle.getStateFlow(ARG_KEYWORD, String.Empty).map(Uri::decode)
            .stateIn(viewModelScope, SharingStarted.Eagerly, String.Empty)

    private var _searchKeyWord =
        MutableStateFlow(initialKeyword.value)
    val searchKeyWord = _searchKeyWord.asStateFlow()

    private val searchEvent = MutableSharedFlow<String>()

    val searchState: StateFlow<SearchState> =
        merge(initialKeyword, searchEvent).map<String, SearchState> { keyword ->
            searchYtSongUseCase.invoke(keyword, MAX_SEARCH_SIZE)
                .map(Song::toSearchUIModel).toImmutableList()
                .let(SearchState::Success)
        }.onStart { emit(SearchState.Loading) }
            .catch { emit(SearchState.Error(it)) }
            .stateIn(viewModelScope, SharingStarted.Eagerly, SearchState.Init)


    fun updateKeyword(keyword: String) {
        _searchKeyWord.update { keyword }
    }

    fun search() {
        viewModelScope.launch {
            searchEvent.emit(_searchKeyWord.value)
        }
    }

    companion object {
        private const val MAX_SEARCH_SIZE = 20
    }
}

sealed interface SearchState {

    object Init : SearchState
    object Loading : SearchState
    data class Error(val t: Throwable) : SearchState
    data class Success(val data: ImmutableList<SongSearchUIModel>) : SearchState
}