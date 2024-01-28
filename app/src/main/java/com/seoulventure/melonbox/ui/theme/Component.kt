package com.seoulventure.melonbox.ui.theme

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@Preview
@Composable
fun MelonButtonPreview() {
    MelonButton(
        textRes = R.string.action_input_share_melon_uri,
        onClick = {}
    )
}