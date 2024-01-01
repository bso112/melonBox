package com.seoulventure.melonbox.feature.search

import android.os.Parcelable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.seoulventure.melonbox.Action
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.feature.preview.data.SongItem
import com.seoulventure.melonbox.feature.search.data.SongSearchResult
import com.seoulventure.melonbox.ui.theme.BACKGROUND_PREVIEW
import com.seoulventure.melonbox.ui.theme.MelonBoxTheme
import com.seoulventure.melonbox.ui.theme.MelonButton
import com.seoulventure.melonbox.ui.theme.color_background
import com.seoulventure.melonbox.ui.theme.color_card_background
import com.seoulventure.melonbox.ui.theme.color_selected_item_background
import com.seoulventure.melonbox.ui.theme.color_text_not_important
import com.seoulventure.melonbox.ui.theme.stylelessTextFieldColors
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

class SearchScreenResult(
    private val navController: NavController
) {
    data class Argument(
        val targetSongId: String,
        val replaceSongItem: SongItem
    )

    fun setResult(argument: Argument) {
        navController.previousBackStackEntry?.savedStateHandle?.apply {
            set(KEY_TARGET_SONG, argument.targetSongId)
            set(KEY_REPLACE_SONG, argument.replaceSongItem)
        }
    }

    fun getResult(): Argument? {
        return navController.currentBackStackEntry?.savedStateHandle?.run {
            val targetSongId = get<String>(KEY_TARGET_SONG)
            val replaceSongItem = get<Parcelable>(KEY_REPLACE_SONG) as? SongItem
            if (targetSongId == null || replaceSongItem == null) return null
            Argument(
                targetSongId = targetSongId,
                replaceSongItem = replaceSongItem
            )
        }
    }

    companion object {
        private const val KEY_TARGET_SONG = "KEY_TARGET_SONG"
        private const val KEY_REPLACE_SONG = "KEY_REPLACE_SONG"
    }
}

@Composable
fun SearchScreen(
    appState: MelonBoxAppState,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val searchResultList by viewModel.searchResultList.collectAsStateWithLifecycle()
    val searchKeyWord by viewModel.searchKeyWord.collectAsStateWithLifecycle()

    SearchContent(
        searchResultList = searchResultList,
        searchKeyWord = searchKeyWord,
        onKeyWordChanged = {
            viewModel.updateKeyword(it)
        },
        onClickSelect = {
            SearchScreenResult(appState.navController).setResult(
                SearchScreenResult.Argument(
                    targetSongId = viewModel.targetSongId,
                    replaceSongItem = SongItem(name = it.songName, artistName = it.artistName)
                )
            )
            appState.navController.popBackStack()
        },
        onClickSearch = {
            viewModel.search()
        }
    )
}

@Composable
private fun SearchContent(
    searchResultList: ImmutableList<SongSearchResult>,
    searchKeyWord: String,
    onKeyWordChanged: (String) -> Unit,
    onClickSelect: (SongSearchResult) -> Unit,
    onClickSearch: Action,
) {
    var selectedSong by remember {
        mutableStateOf<SongSearchResult?>(null)
    }

    val isSelectButtonEnabled by remember {
        derivedStateOf { selectedSong != null }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color_background)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchBar(
                searchKeyWord = searchKeyWord,
                onKeyWordChanged = onKeyWordChanged,
                onClickSearch = onClickSearch
            )
            SearchResultList(
                modifier = Modifier.padding(top = 37.dp),
                selectedSong = selectedSong,
                resultList = searchResultList,
                onSelectItem = { song ->
                    selectedSong = if (song == selectedSong) {
                        null
                    } else {
                        song
                    }
                }
            )
        }
        SelectSongButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(58.dp),
            onClickSelect = { selectedSong?.let { onClickSelect(it) } },
            enabled = isSelectButtonEnabled
        )
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    searchKeyWord: String,
    onClickSearch: Action,
    onKeyWordChanged: (String) -> Unit
) {
    Row(
        modifier = modifier
            .padding(horizontal = 37.dp)
            .padding(top = 44.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier
                .weight(1f)
                .padding(start = 5.dp),
            value = searchKeyWord,
            onValueChange = onKeyWordChanged,
            colors = stylelessTextFieldColors,
            singleLine = true,
            textStyle = TextStyle(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            placeholder = {
                Text(
                    text = stringResource(id = R.string.hint_input_melon_playlist_url),
                    color = color_text_not_important,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            })
        IconButton(
            modifier = Modifier
                .size(50.dp)
                .padding(end = 10.dp), onClick = onClickSearch
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Filled.Search,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SearchResultList(
    modifier: Modifier = Modifier,
    selectedSong: SongSearchResult?,
    resultList: ImmutableList<SongSearchResult>,
    onSelectItem: (SongSearchResult?) -> Unit
) {
    val lazyColumnState = rememberLazyListState()
    val canScrollForward by remember {
        derivedStateOf { lazyColumnState.canScrollForward }
    }
    val canScrollBackward by remember {
        derivedStateOf { lazyColumnState.canScrollBackward }
    }

    Card(
        modifier = modifier
            .fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = color_card_background
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 40.dp, bottom = 150.dp)
        ) {
            LazyColumn(
                state = lazyColumnState,
            ) {
                items(resultList) { item ->
                    SongSearchResultItem(
                        isSelected = selectedSong == item,
                        songSearchResult = item,
                        onSelectItem = onSelectItem
                    )
                }
            }
            if (canScrollBackward) {
                VerticalGradientView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.TopCenter),
                    startColor = color_card_background,
                    endColor = Color.Transparent
                )
            }
            if (canScrollForward) {
                VerticalGradientView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.BottomCenter),
                    startColor = Color.Transparent,
                    endColor = color_card_background
                )
            }
        }

    }

}

@Composable
private fun SongSearchResultItem(
    isSelected: Boolean,
    songSearchResult: SongSearchResult,
    onSelectItem: (SongSearchResult) -> Unit
) {
    val backgroundColor = if (isSelected) color_selected_item_background else Color.Transparent

    Column(
        modifier = Modifier
            .clickable { onSelectItem(songSearchResult) }
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 40.dp, vertical = 12.dp)
    ) {
        Text(
            songSearchResult.songName,
            fontSize = 16.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            songSearchResult.artistName,
            fontSize = 13.sp,
            color = color_text_not_important,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}

@Composable
private fun SelectSongButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClickSelect: Action
) {
    MelonButton(
        modifier = modifier,
        textRes = R.string.action_select,
        onClick = onClickSelect,
        enabled = enabled
    )
}


@Composable
@Preview(showBackground = true, backgroundColor = BACKGROUND_PREVIEW, showSystemUi = true)
fun SearchContentPreview() {
    MelonBoxTheme {
        SearchContent(
            searchResultList = fakeSongResultList,
            searchKeyWord = "한숨",
            onKeyWordChanged = {},
            onClickSelect = {},
            onClickSearch = {}
        )
    }
}

@Composable
fun VerticalGradientView(
    modifier: Modifier,
    startColor: Color,
    endColor: Color
) {
    val brush = Brush.verticalGradient(listOf(startColor, endColor))
    Canvas(
        modifier = modifier,
        onDraw = {
            drawRect(brush)
        }
    )
}

val fakeSongResultList
    get() = listOf(
        SongSearchResult(
            songName = "한숨이 나온다아아아아아아아아아아아아아아아아아아아아아아아아",
            artistName = "이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이"
        ),
        SongSearchResult(
            songName = "New Beginnings",
            artistName = "Jasmine Myra"
        ),
        SongSearchResult(
            songName = "Blue",
            artistName = "Portraits in Jazz, Claus Waidtløw"
        ),
        SongSearchResult(
            songName = "그대만 있다면 (여름날 우리 X 너드커넥션 (Nerd Connection))",
            artistName = "너드커넥션"
        ),
        SongSearchResult(
            songName = "가까운 듯 먼 그대여 (Closely Far Away)",
            artistName = "카더가든"
        ),
        SongSearchResult(
            songName = "네 생각",
            artistName = "존박"
        ),
        SongSearchResult(
            songName = "ただ好きと言えたら",
            artistName = "KERENMI 및 Atarayo"
        ),
    ).toImmutableList()