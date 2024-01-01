package com.seoulventure.melonbox.feature.main

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.seoulventure.melonbox.Empty
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.feature.preview.navigatePlaylistPreview
import com.seoulventure.melonbox.ui.theme.MelonBoxTheme
import com.seoulventure.melonbox.ui.theme.MelonButton
import com.seoulventure.melonbox.ui.theme.color_background
import com.seoulventure.melonbox.ui.theme.color_text_not_important
import com.seoulventure.melonbox.ui.theme.stylelessTextFieldColors


@Composable
fun MainScreen(appState: MelonBoxAppState) {
    MainContent {
        appState.navController.navigatePlaylistPreview(it)
    }
}

@Composable
fun MainContent(
    onMelonDropAnimationEnd: (String) -> Unit,
) {
    var melonPlaylistUrl by remember { mutableStateOf(String.Empty) }
    var isButtonClicked by remember { mutableStateOf(false) }

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
            .background(color_background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
                        color = color_text_not_important,
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
            textRes = R.string.action_input_share_melon_uri,
            onClick = { isButtonClicked = true },
            enabled = melonPlaylistUrl.length > 1,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    MelonBoxTheme {
        Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
            MainContent(
                onMelonDropAnimationEnd = {}
            )
        }

    }
}