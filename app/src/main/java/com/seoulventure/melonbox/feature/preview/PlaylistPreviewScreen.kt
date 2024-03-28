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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.seoulventure.melonbox.Action
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.feature.complete.navigateComplete
import com.seoulventure.melonbox.feature.main.MAIN_ROUTE
import com.seoulventure.melonbox.feature.preview.data.SongItem
import com.seoulventure.melonbox.feature.search.SearchScreenResult
import com.seoulventure.melonbox.feature.search.navigateSearch
import com.seoulventure.melonbox.ui.theme.BackgroundPreviewColor
import com.seoulventure.melonbox.ui.theme.LoadingView
import com.seoulventure.melonbox.ui.theme.MelonAlertDialog
import com.seoulventure.melonbox.ui.theme.MelonBoxTheme
import com.seoulventure.melonbox.ui.theme.StaticMelonButton
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.ceil


@Composable
fun PlaylistPreviewScreen(
    appState: MelonBoxAppState,
    viewModel: PlaylistPreviewViewModel = hiltViewModel()
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val playlistState by viewModel.playlistState.collectAsStateWithLifecycle()
    val selectedSong by viewModel.selectedSong.collectAsStateWithLifecycle()
    val isCreatingPlaylist by viewModel.isCreatingPlaylist.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()

    SideEffect {
        val replaceResult = SearchScreenResult(appState.navController)
            .getResult()

        if (replaceResult != null) {
            viewModel.replaceSong(
                targetSongId = replaceResult.targetSongId,
                replaceSongItem = replaceResult.replaceSongItem
            )
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedSong()
        }
    }


    LaunchedEffect(Unit) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.uiEvent.collectLatest { uiEvent ->
                when (uiEvent) {
                    is UIEvent.Error -> {
                        val error = uiEvent.t
                        val errorMsgRes =
                            if (error is ClientRequestException && error.response.status == HttpStatusCode.Forbidden) {
                                R.string.msg_error_exceed_api_limit
                            } else {
                                R.string.msg_error_generic
                            }

                        appState.snackBarHostState.showSnackbar(context.getString(errorMsgRes))
                        uiEvent.t.printStackTrace()
                    }

                    is UIEvent.NavigateComplete -> {
                        if (uiEvent.insertedMusicCount <= 0) {
                            appState.snackBarHostState.showSnackbar(context.getString(R.string.msg_error_generic))
                        } else {
                            appState.navController.navigateComplete(uiEvent.insertedMusicCount) {
                                popUpTo(MAIN_ROUTE)
                            }
                        }

                    }
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (playlistState.data.isNotEmpty()) {
            PlaylistPreviewContent(
                songItemList = playlistState.data,
                selectedSongItem = selectedSong,
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
                    val fileName = SimpleDateFormat("ddMMyy-hhmmss.SSS", Locale.KOREA).format(Date())
                    viewModel.createPlaylist(fileName)
                },
                onClickCancel = {
                    appState.navController.popBackStack()
                }
            )
        }
        if (playlistState.isLoading) {
            LoadingView(modifier = Modifier.align(Alignment.Center))
        } else if (isCreatingPlaylist) {
            val percent: Int = (progress * 100).toInt()
            MelonAlertDialog(
                onDismissRequest = { viewModel.cancelCreatingPlaylist() },
                text = "플레이리스트를 생성하는 중입니다\n ${percent}% 완료...",
                cancelText = "취소하기"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistPreviewContent(
    songItemList: ImmutableList<SongItem>,
    selectedSongItem: SongItem?,
    onClickSongItem: (SongItem) -> Unit,
    onClickDelete: (SongItem) -> Unit,
    onClickReplace: (SongItem) -> Unit,
    onClickConfirm: Action,
    onClickCancel: Action,
) {

    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet: Boolean by remember {
        mutableStateOf(selectedSongItem != null)
    }
    var showDeleteAlertDialog: Boolean by remember {
        mutableStateOf(false)
    }
    val colors = MelonBoxTheme.colors

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
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
            songItemList = songItemList,
            onClickSongItem = {
                showBottomSheet = true
                onClickSongItem(it)
            })
        Spacer(modifier = Modifier.size(60.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            StaticMelonButton(
                textRes = R.string.action_cancel,
                onClick = onClickCancel,
                containerColor = colors.btnDisabled
            )
            Spacer(modifier = Modifier.size(10.dp))
            StaticMelonButton(
                textRes = R.string.action_confirm,
                onClick = onClickConfirm,
                containerColor = colors.btnEnabled
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
            containerColor = colors.cardBackground,
            contentColor = colors.text,
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
                        onClickReplace(selectedSongItem!!)
                    }) {
                    Text(
                        text = stringResource(id = R.string.action_replace),
                        color = colors.text,
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
                        color = colors.warning,
                    )
                }
            }
        }
    }

    if (showDeleteAlertDialog) {
        DeleteAlertDialog(
            onConfirmation = {
                onClickDelete(selectedSongItem!!)
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
    songItemList: ImmutableList<SongItem>,
    onClickSongItem: (SongItem) -> Unit
) {
    //TODO 몇으로 해야하나..
    val visibleSongListCount = 6

    val pagerState = rememberPagerState {
        ceil(songItemList.size / visibleSongListCount.toFloat()).toInt()
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
                containerColor = MelonBoxTheme.colors.cardBackground
            ),
            shape = CardDefaults.elevatedShape,
        ) {
            val startIndex = index * visibleSongListCount
            val endIndex = startIndex + visibleSongListCount
            val visibleSongList =
                songItemList.subList(
                    startIndex.coerceIn(0, songItemList.lastIndex),
                    endIndex.coerceIn(0, songItemList.size)
                )
            PlaylistPage(songItemList = visibleSongList, onClickSongItem = onClickSongItem)
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
            val color =
                if (isPageSelected) MelonBoxTheme.colors.melon else MelonBoxTheme.colors.iconGray
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
fun PlaylistPage(songItemList: ImmutableList<SongItem>, onClickSongItem: (SongItem) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp, vertical = 25.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        songItemList.forEach {
            SongItem(songItem = it, onClickItem = onClickSongItem)
        }
    }
}

@Composable
fun SongItem(songItem: SongItem, onClickItem: (SongItem) -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClickItem(songItem) }
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = songItem.name,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                text = songItem.artistName,
                fontSize = 13.sp,
                color = MelonBoxTheme.colors.textNotImportant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.size(5.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            tint = MelonBoxTheme.colors.iconGray,
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
        containerColor = MelonBoxTheme.colors.cardBackground,
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
                Text(
                    text = stringResource(id = R.string.action_confirm),
                    color = MelonBoxTheme.colors.melon
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    stringResource(id = R.string.action_cancel),
                    color = MelonBoxTheme.colors.melon
                )
            }
        }
    )
}


@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
fun DeleteAlertDialogPreview() {
    DeleteAlertDialog()
}

@Composable
@Preview(
    showBackground = true, backgroundColor = BackgroundPreviewColor, showSystemUi = true,
    device = "id:pixel_4"
)
fun PlaylistPreviewPreview() {
    PlaylistPreviewContent(
        songItemList = fakeSongListItems,
        selectedSongItem = null,
        onClickSongItem = {},
        onClickReplace = {},
        onClickConfirm = {},
        onClickCancel = {},
        onClickDelete = {}
    )
}

val fakeSongListItems
    get() = listOf(
        SongItem(
            name = "한숨이 나온다아아아아아아아아아아아아아아아아아아아아아아아아",
            artistName = "이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이이하이"
        ),
        SongItem(
            name = "New Beginnings",
            artistName = "Jasmine Myra"
        ),
        SongItem(
            name = "Blue",
            artistName = "Portraits in Jazz, Claus Waidtløw"
        ),
        SongItem(
            name = "그대만 있다면 (여름날 우리 X 너드커넥션 (Nerd Connection))",
            artistName = "너드커넥션"
        ),
        SongItem(
            name = "가까운 듯 먼 그대여 (Closely Far Away)",
            artistName = "카더가든"
        ),
        SongItem(
            name = "네 생각",
            artistName = "존박"
        ),
        SongItem(
            name = "ただ好きと言えたら",
            artistName = "KERENMI 및 Atarayo"
        ),
    ).toImmutableList()