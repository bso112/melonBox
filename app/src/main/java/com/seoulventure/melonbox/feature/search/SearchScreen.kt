package com.seoulventure.melonbox.feature.search

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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.seoulventure.melonbox.Action
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.feature.preview.data.SongItem
import com.seoulventure.melonbox.feature.search.data.SongSearchUIModel
import com.seoulventure.melonbox.ui.theme.BackgroundPreviewColor
import com.seoulventure.melonbox.ui.theme.LoadingView
import com.seoulventure.melonbox.ui.theme.MelonBoxTheme
import com.seoulventure.melonbox.ui.theme.MelonButton
import com.seoulventure.melonbox.ui.theme.stylelessTextFieldColors
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun SearchScreen(
    appState: MelonBoxAppState,
    viewModel: SearchViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val searchState by viewModel.searchState.collectAsStateWithLifecycle()
    val searchKeyWord by viewModel.searchKeyWord.collectAsStateWithLifecycle()
    val isLoading by remember {
        derivedStateOf { searchState.isLoading }
    }

    LaunchedEffect(searchState.error) {
        if (searchState.error != null) {
            appState.snackBarHostState.showSnackbar(context.getString(R.string.msg_error_generic))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (searchState.data.isNotEmpty()) {
            SearchContent(
                searchResultList = searchState.data,
                searchKeyWord = searchKeyWord,
                onKeyWordChanged = {
                    viewModel.updateKeyword(it)
                },

                onClickSelect = {
                    SearchScreenResult(appState.navController).setResult(
                        SearchScreenResult.Argument(
                            targetSongId = viewModel.targetSongId,
                            replaceSongItem = SongItem(
                                name = it.songName,
                                artistName = it.artistName
                            )
                        )
                    )
                    appState.navController.popBackStack()
                },
                onClickSearch = {
                    viewModel.search()
                }
            )
        }
        if (isLoading) {
            LoadingView(modifier = Modifier.align(Alignment.Center))
        }
    }

}

@Composable
private fun SearchContent(
    searchResultList: ImmutableList<SongSearchUIModel>,
    searchKeyWord: String,
    onKeyWordChanged: (String) -> Unit,
    onClickSelect: (SongSearchUIModel) -> Unit,
    onClickSearch: Action,
) {
    var selectedSong by remember {
        mutableStateOf<SongSearchUIModel?>(null)
    }

    val isSelectButtonEnabled by remember {
        derivedStateOf { selectedSong != null }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MelonBoxTheme.colors.background)
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
                    color = MelonBoxTheme.colors.textNotImportant,
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
    selectedSong: SongSearchUIModel?,
    resultList: ImmutableList<SongSearchUIModel>,
    onSelectItem: (SongSearchUIModel?) -> Unit
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
            containerColor = MelonBoxTheme.colors.cardBackground
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
                    startColor = MelonBoxTheme.colors.cardBackground,
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
                    endColor = MelonBoxTheme.colors.cardBackground
                )
            }
        }

    }

}

@Composable
private fun SongSearchResultItem(
    isSelected: Boolean,
    songSearchResult: SongSearchUIModel,
    onSelectItem: (SongSearchUIModel) -> Unit
) {
    val backgroundColor =
        if (isSelected) MelonBoxTheme.colors.selectedItemBackground else Color.Transparent

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
            color = MelonBoxTheme.colors.textNotImportant,
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
        text = stringResource(R.string.action_select),
        onClick = onClickSelect,
        enabled = enabled
    )
}


@Composable
@Preview(showBackground = true, backgroundColor = BackgroundPreviewColor, showSystemUi = true)
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
        SongSearchUIModel(
            songName = "한숨이 나온다아아아아아아아아아아아아아아아아아아아아아아아아",
            artistName = "이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이"
        ),
        SongSearchUIModel(
            songName = "New Beginnings",
            artistName = "Jasmine Myra"
        ),
        SongSearchUIModel(
            songName = "Blue",
            artistName = "Portraits in Jazz, Claus Waidtløw"
        ),
        SongSearchUIModel(
            songName = "그대만 있다면 (여름날 우리 X 너드커넥션 (Nerd Connection))",
            artistName = "너드커넥션"
        ),
        SongSearchUIModel(
            songName = "가까운 듯 먼 그대여 (Closely Far Away)",
            artistName = "카더가든"
        ),
        SongSearchUIModel(
            songName = "네 생각",
            artistName = "존박"
        ),
        SongSearchUIModel(
            songName = "ただ好きと言えたら",
            artistName = "KERENMI 및 Atarayo"
        ),
    ).toImmutableList()