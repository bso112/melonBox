package com.seoulventure.melonbox.ui.theme

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.seoulventure.melonbox.Action
import com.seoulventure.melonbox.R


@Composable
fun MelonButton(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    onClick: Action,
    contentPadding: PaddingValues = PaddingValues(horizontal = 60.dp, vertical = 20.dp),
    enabled: Boolean = true,
) {
    InternalMelonButton(
        modifier = modifier,
        textRes = textRes,
        onClick = onClick,
        containerColor = null,
        contentPadding = contentPadding,
        enabled = enabled
    )
}

/**
 * enable, disable 에 따라 색이 변하지 않는 MelonButton
 */
@Composable
fun StaticMelonButton(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    onClick: Action,
    containerColor: Color,
    contentPadding: PaddingValues = PaddingValues(horizontal = 60.dp, vertical = 20.dp),
    enabled: Boolean = true,
) {
    InternalMelonButton(
        modifier = modifier,
        textRes = textRes,
        onClick = onClick,
        containerColor = containerColor,
        contentPadding = contentPadding,
        enabled = enabled
    )
}

@Composable
private fun InternalMelonButton(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int,
    onClick: Action,
    containerColor: Color? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 60.dp, vertical = 20.dp),
    enabled: Boolean = true,
) {
    val colors = if (containerColor == null) {
        ButtonDefaults.buttonColors(
            containerColor = MelonBoxTheme.colors.btnEnabled,
            disabledContainerColor = MelonBoxTheme.colors.btnDisabled,
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White,
        )
    }

    Button(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        onClick = onClick,
        enabled = enabled,
        contentPadding = contentPadding,
        colors = colors
    ) {
        Text(
            text = stringResource(id = textRes),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        color = MelonBoxTheme.colors.melon,
        strokeWidth = 5.dp,
        modifier = modifier.size(50.dp),
    )
}


@Preview
@Composable
fun MelonButtonPreview() {
    MelonButton(
        textRes = R.string.action_input_share_melon_uri,
        onClick = {}
    )
}

@Preview
@Composable
fun LoadingPreview() {
    LoadingView()
}

@Composable
fun MelonAlertDialog(
    text: String,
    cancelText: String,
    onDismissRequest: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .padding(16.dp),
            colors = melonCardColors,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = text,
                    color = MelonBoxTheme.colors.text,
                    textAlign = TextAlign.Center,
                    lineHeight = 25.sp,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 25.dp),
                )
                TextButton(
                    onClick = {
                        onCancel()
                        onDismissRequest()
                    },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(cancelText, color = MelonBoxTheme.colors.warning)
                }
            }
        }
    }
}

@Preview
@Composable
fun MelonAlertDialogPreview() {
    MelonBoxTheme {
        MelonAlertDialog(text = "플레이리스트를 생성하는 중입니다\n 40% 완료...", cancelText = "확인")
    }
}