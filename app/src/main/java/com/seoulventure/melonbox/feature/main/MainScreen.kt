package com.seoulventure.melonbox.feature.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.util.Consumer
import androidx.hilt.navigation.compose.hiltViewModel
import com.seoulventure.melonbox.Empty
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.feature.preview.navigatePlaylistPreview
import com.seoulventure.melonbox.logD
import com.seoulventure.melonbox.logE
import com.seoulventure.melonbox.ui.theme.MelonBoxTheme
import com.seoulventure.melonbox.ui.theme.MelonButton
import com.seoulventure.melonbox.ui.theme.stylelessTextFieldColors
import com.seoulventure.melonbox.util.FirebaseAnalytics
import com.seoulventure.melonbox.util.FirebaseEvent
import com.seoulventure.melonbox.util.emptyDisposeResult
import com.seoulventure.melonbox.util.getActivity


@Composable
fun MainScreen(
    appState: MelonBoxAppState,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var snackBarMsg by remember {
        mutableStateOf(String.Empty)
    }

    LaunchedEffect(snackBarMsg) {
        if (snackBarMsg.isNotBlank()) {
            appState.snackBarHostState.showSnackbar(snackBarMsg)
        }
    }

    MainContent(
        tutorialUrl = viewModel.getTutorialUrl(),
        onMelonDropAnimationEnd = {
            appState.navController.navigatePlaylistPreview(Uri.encode(it))
            FirebaseAnalytics.logEvent(FirebaseEvent.ClickPutMelon)
        },
        onFailToOpenTutorial = { snackBarMsg = context.getString(R.string.msg_error_generic) }
    )
}

@Composable
fun MainContent(
    tutorialUrl: String,
    onMelonDropAnimationEnd: (String) -> Unit = {},
    onFailToOpenTutorial: () -> Unit = {}
) {
    val context = LocalContext.current

    var melonPlaylistUrl by remember { mutableStateOf(context.getSharedUrl().orEmpty()) }
    var isButtonClicked by remember { mutableStateOf(false) }


    DisposableEffect(Unit) {
        val activity = context.getActivity() ?: return@DisposableEffect emptyDisposeResult
        val listener = Consumer<Intent> { intent ->
            melonPlaylistUrl = intent.getSharedUrl().orEmpty()
        }
        activity.addOnNewIntentListener(listener)
        onDispose {
            activity.removeOnNewIntentListener(listener)
        }
    }

    val melonTransitionY by animateFloatAsState(
        animationSpec = tween(300),
        targetValue = if (isButtonClicked) 340f else 0f,
        label = "melonTransitionY"
    )

    val melonAlpha by animateFloatAsState(
        animationSpec = tween(300),
        targetValue = if (isButtonClicked) 0f else 1f,
        finishedListener = { onMelonDropAnimationEnd(melonPlaylistUrl) },
        label = "melonAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MelonBoxTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(horizontal = 44.dp, vertical = 15.dp)
                .clickable {
                    FirebaseAnalytics.logEvent(FirebaseEvent.ClickTutorial)
                    runCatching {
                        context.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(tutorialUrl)
                            )
                        )
                    }.onFailure {
                        onFailToOpenTutorial()
                        logE("cannot open tutorial. url: $tutorialUrl", it)
                    }
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(10.dp),
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = MelonBoxTheme.colors.text
            )
            Spacer(modifier = Modifier.size(2.dp))
            Text(
                text = "링크를 어디서 얻나요?",
                color = MelonBoxTheme.colors.text,
                fontSize = 10.sp,
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 44.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(horizontal = 5.dp)
        ) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                value = melonPlaylistUrl,
                onValueChange = { melonPlaylistUrl = it },
                colors = stylelessTextFieldColors,
                textStyle = TextStyle(fontWeight = FontWeight.SemiBold),
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.hint_input_melon_playlist_url),
                        color = MelonBoxTheme.colors.textNotImportant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                })
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
                .padding(horizontal = 98.dp)
                .zIndex(1f)
                .graphicsLayer {
                    alpha = melonAlpha
                    translationY = melonTransitionY
                },
            painter = painterResource(id = R.drawable.melon), contentDescription = null
        )
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 73.dp),
            painter = painterResource(id = R.drawable.box),
            contentDescription = null
        )
        MelonButton(
            text = stringResource(R.string.action_input_share_melon_uri),
            onClick = { isButtonClicked = true },
            enabled = melonPlaylistUrl.length > 1,
        )
    }
}


private fun Context.getSharedUrl(): String? {
    return getActivity()?.intent?.getSharedUrl()
}

private fun Intent.getSharedUrl(): String? {
    return if (action == Intent.ACTION_SEND && type == "text/plain") {
        val sharedText = getStringExtra(Intent.EXTRA_TEXT) ?: return null
        Regex("https://.*").find(sharedText)?.value
    } else {
        null
    }.also {
        logD("sharedUrl $it")
    }
}


@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    MelonBoxTheme {
        Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            MainContent(
                tutorialUrl = "",
                onMelonDropAnimationEnd = {}
            )
        }

    }
}