package com.seoulventure.melonbox.feature.preview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavOptions
import com.seoulventure.melonbox.Action
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.feature.complete.navigateComplete
import com.seoulventure.melonbox.feature.main.MAIN_ROUTE
import com.seoulventure.melonbox.feature.preview.data.Song
import com.seoulventure.melonbox.feature.search.SearchScreenResult
import com.seoulventure.melonbox.feature.search.navigateSearch
import com.seoulventure.melonbox.ui.theme.BACKGROUND_PREVIEW
import com.seoulventure.melonbox.ui.theme.StaticMelonButton
import com.seoulventure.melonbox.ui.theme.color_background
import com.seoulventure.melonbox.ui.theme.color_btn_disable
import com.seoulventure.melonbox.ui.theme.color_btn_enable
import com.seoulventure.melonbox.ui.theme.color_card_background
import com.seoulventure.melonbox.ui.theme.color_icon_gray
import com.seoulventure.melonbox.ui.theme.color_melon
import com.seoulventure.melonbox.ui.theme.color_text
import com.seoulventure.melonbox.ui.theme.color_text_not_important
import com.seoulventure.melonbox.ui.theme.color_warning
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.ceil


@Composable
fun PlaylistPreviewScreen(
    appState: MelonBoxAppState,
    viewModel: PlaylistPreviewViewModel = hiltViewModel()
) {
    val songList by viewModel.songList.collectAsStateWithLifecycle()
    val selectedSong by viewModel.selectedSong.collectAsStateWithLifecycle()

    SideEffect {
        val replaceResult = SearchScreenResult(appState.navController)
            .getResult()

        if (replaceResult != null) {
            viewModel.replaceSong(
                targetSongId = replaceResult.targetSongId,
                replaceSong = replaceResult.replaceSong
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedSong()
        }
    }

    PlaylistPreviewContent(
        songList = songList,
        selectedSong = selectedSong,
        onClickSongItem = {
            viewModel.selectSong(it)
        },
        onClickDelete = {
            viewModel.deleteSelectedSong()
        },
        onClickReplace = {
            appState.navController.navigateSearch(songId = it.id, keyword = it.name)
        },
        onClickConfirm = {
            appState.navController.navigateComplete {
                popUpTo(MAIN_ROUTE)
            }
        },
        onClickCancel = {
            appState.navController.popBackStack()
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistPreviewContent(
    songList: ImmutableList<Song>,
    selectedSong: Song?,
    onClickSongItem: (Song) -> Unit,
    onClickDelete: (Song) -> Unit,
    onClickReplace: (Song) -> Unit,
    onClickConfirm: Action,
    onClickCancel: Action,
) {

    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet: Boolean by remember {
        mutableStateOf(selectedSong != null)
    }
    var showDeleteAlertDialog: Boolean by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color_background),
        verticalArrangement = Arrangement.Center
    ) {
        Column(modifier = Modifier.padding(top = 24.dp, start = 24.dp)) {
            Text(
                text = stringResource(id = R.string.title_playlist_preview_ytMusic),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(text = stringResource(id = R.string.subtitle_playlist_preview_ytMusic))
        }
        Spacer(modifier = Modifier.size(16.dp))
        PlaylistPager(
            modifier = Modifier.weight(1f),
            songList = songList,
            onClickSongItem = {
                showBottomSheet = true
                onClickSongItem(it)
            })
        Spacer(modifier = Modifier.size(60.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            StaticMelonButton(
                textRes = R.string.action_cancel,
                onClick = onClickCancel,
                containerColor = color_btn_disable
            )
            Spacer(modifier = Modifier.size(10.dp))
            StaticMelonButton(
                textRes = R.string.action_confirm,
                onClick = onClickConfirm,
                containerColor = color_btn_enable
            )
        }
        Spacer(modifier = Modifier.size(80.dp))
    }

    if (showBottomSheet) {
        val bottomSheetTextStyle = TextStyle(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Start,
        )

        ModalBottomSheet(
            containerColor = color_card_background,
            contentColor = color_text,
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            tonalElevation = 15.dp
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(start = 15.dp),
            ) {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                        }
                        onClickReplace(selectedSong!!)
                    }) {
                    Text(
                        text = stringResource(id = R.string.action_replace),
                        color = color_text,
                        style = bottomSheetTextStyle,
                    )
                }
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                        }.invokeOnCompletion {
                            showBottomSheet = false
                        }
                        showDeleteAlertDialog = true
                    }) {
                    Text(
                        text = stringResource(id = R.string.action_delete),
                        style = bottomSheetTextStyle,
                        color = color_warning,
                    )
                }
            }
        }
    }

    if (showDeleteAlertDialog) {
        DeleteAlertDialog(
            onConfirmation = {
                onClickDelete(selectedSong!!)
                showDeleteAlertDialog = false
            },
            onDismissRequest = {
                showDeleteAlertDialog = false
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistPager(
    modifier: Modifier = Modifier,
    songList: ImmutableList<Song>,
    onClickSongItem: (Song) -> Unit
) {
    //TODO 몇으로 해야하나..
    val visibleSongListCount = 6

    val pagerState = rememberPagerState {
        ceil(songList.size / visibleSongListCount.toFloat()).toInt()
    }

    //modifier.padding(horizontal = 18.dp, vertical = 25.dp)
    HorizontalPager(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp),
        pageSpacing = 10.dp,
        state = pagerState,
        beyondBoundsPageCount = 1,
        verticalAlignment = Alignment.Top
    ) { index ->
        Card(
            modifier = Modifier
                .carouselTransition(index, pagerState),
            colors = CardDefaults.cardColors(
                containerColor = color_card_background
            ),
            shape = CardDefaults.elevatedShape,
        ) {
            val startIndex = index * visibleSongListCount
            val endIndex = startIndex + visibleSongListCount
            val visibleSongList =
                songList.subList(
                    startIndex.coerceIn(0, songList.lastIndex),
                    endIndex.coerceIn(0, songList.size)
                )
            PlaylistPage(songList = visibleSongList, onClickSongItem = onClickSongItem)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun Modifier.carouselTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset =
            ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

        val transformation =
            lerp(
                start = 0.9f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        alpha = transformation
        scaleY = transformation
    }

@Composable
fun PlaylistPageIndicator(modifier: Modifier, currentPage: Int, totalPage: Int) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPage) { index ->
            val isPageSelected = index == currentPage
            val size = if (isPageSelected) 10.dp else 8.dp
            val color = if (isPageSelected) color_melon else color_icon_gray
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(color)
                    .size(size)
            )
        }
    }
}

@Composable
fun PlaylistPage(songList: ImmutableList<Song>, onClickSongItem: (Song) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 25.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        songList.forEach {
            SongItem(song = it, onClickItem = onClickSongItem)
        }
    }
}

@Composable
fun SongItem(song: Song, onClickItem: (Song) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClickItem(song) }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = song.name, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = song.artistName,
                fontSize = 13.sp,
                color = color_text_not_important,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.size(5.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            tint = color_icon_gray,
            contentDescription = null
        )
    }
}


@Composable
fun DeleteAlertDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
) {
    AlertDialog(
        containerColor = color_card_background,
        text = {
            Text(
                stringResource(id = R.string.msg_confirm_delete),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirmation) {
                Text(text = stringResource(id = R.string.action_confirm), color = color_melon)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(id = R.string.action_cancel), color = color_melon)
            }
        }
    )
}

@Composable
@Preview
fun DeleteAlertDialogPreview() {
    DeleteAlertDialog()
}

@Composable
@Preview(
    showBackground = true, backgroundColor = BACKGROUND_PREVIEW, showSystemUi = true,
    device = "id:pixel_4"
)
fun PlaylistPreviewPreview() {
    PlaylistPreviewContent(
        songList = fakeSongList,
        selectedSong = null,
        onClickSongItem = {},
        onClickReplace = {},
        onClickConfirm = {},
        onClickCancel = {},
        onClickDelete = {}
    )
}

val fakeSongList
    get() = listOf(
        Song(
            name = "한숨이 나온다아아아아아아아아아아아아아아아아아아아아아아아아",
            artistName = "이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이"
        ),
        Song(
            name = "New Beginnings",
            artistName = "Jasmine Myra"
        ),
        Song(
            name = "Blue",
            artistName = "Portraits in Jazz, Claus Waidtløw"
        ),
        Song(
            name = "그대만 있다면 (여름날 우리 X 너드커넥션 (Nerd Connection))",
            artistName = "너드커넥션"
        ),
        Song(
            name = "가까운 듯 먼 그대여 (Closely Far Away)",
            artistName = "카더가든"
        ),
        Song(
            name = "네 생각",
            artistName = "존박"
        ),
        Song(
            name = "ただ好きと言えたら",
            artistName = "KERENMI 및 Atarayo"
        ),
    ).toImmutableList()