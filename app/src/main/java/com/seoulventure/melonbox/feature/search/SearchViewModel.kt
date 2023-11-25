package com.seoulventure.melonbox.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.seoulventure.melonbox.feature.search.data.SongSearchResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val targetSongId = savedStateHandle.get<String>(ARG_SONG_ID).orEmpty()
    val initialKeyword = savedStateHandle.get<String>(ARG_KEYWORD).orEmpty()

    private var _searchKeyWord =
        MutableStateFlow(initialKeyword)
    val searchKeyWord = _searchKeyWord.asStateFlow()

    val searchResultList: MutableStateFlow<ImmutableList<SongSearchResult>> =
        MutableStateFlow(fakeSongResultList)

    fun updateKeyword(keyword: String) {
        _searchKeyWord.update { keyword }
    }

    fun search() {

    }
}