package com.seoulventure.melonbox.feature.complete

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.seoulventure.melonbox.Action
import com.seoulventure.melonbox.MelonBoxAppState
import com.seoulventure.melonbox.R
import com.seoulventure.melonbox.ui.theme.BackgroundPreviewColor
import com.seoulventure.melonbox.ui.theme.MelonBoxTheme
import com.seoulventure.melonbox.ui.theme.StaticMelonButton


private const val PACKAGE_YT_MUSIC = "com.google.android.apps.youtube.music"

@Composable
fun CompleteScreen(
    appState: MelonBoxAppState,
    insertedSongCount: Int,
) {
    val context = LocalContext.current

    CompleteContent(
        insertedSongCount = insertedSongCount,
        onClickBack = {
            appState.navController.popBackStack()
        },
        onClickDone = {
            runCatching {
                val intent = context.packageManager.getLaunchIntentForPackage(PACKAGE_YT_MUSIC)
                    ?: Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("market://details?id=$PACKAGE_YT_MUSIC")
                    }
                context.startActivity(intent)

            }.onFailure {
                Toast.makeText(
                    context,
                    context.getString(R.string.msg_cannot_open_yt_music),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    )
}

@Composable
private fun CompleteContent(
    insertedSongCount: Int,
    onClickBack: Action,
    onClickDone: Action
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MelonBoxTheme.colors.background),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                stringResource(id = R.string.msg_complete),
                fontSize = 48.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                stringResource(id = R.string.msg_complete_sub, insertedSongCount),
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.size(82.dp))
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                painter = painterResource(id = R.drawable.melon), contentDescription = null
            )
            Spacer(modifier = Modifier.size(60.dp))
            StaticMelonButton(
                text = stringResource(R.string.action_go_to_yt_music),
                onClick = onClickDone,
                containerColor = MelonBoxTheme.colors.melon
            )
        }
        IconButton(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            onClick = onClickBack
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = "go back"
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = BackgroundPreviewColor, showSystemUi = true)
@Composable
fun CompleteContentPreview() {
    MelonBoxTheme {
        CompleteContent(
            insertedSongCount = 3,
            onClickBack = {},
            onClickDone = {}
        )
    }
}