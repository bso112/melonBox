package com.seoulventure.melonbox.ui.theme

import androidx.compose.material3.CardColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Color.Companion.TextBlack inline get() = Color(0xFF2B2B2B)

//must be compile time constant
const val BackgroundPreviewColor = 0xFFEDEDED

@Immutable
data class MelonColors(
    val background: Color,
    val btnDisabled: Color,
    val btnEnabled: Color,
    val warning: Color,
    val melon: Color,
    val selectedItemBackground: Color,
    val textNotImportant: Color,
    val iconGray: Color,
    val cardBackground: Color,
    val text: Color,
)

val LocalMelonColors = staticCompositionLocalOf {
    MelonColors(
        background = Color.Unspecified,
        btnDisabled = Color.Unspecified,
        btnEnabled = Color.Unspecified,
        warning = Color.Unspecified,
        melon = Color.Unspecified,
        selectedItemBackground = Color.Unspecified,
        textNotImportant = Color.Unspecified,
        iconGray = Color.Unspecified,
        cardBackground = Color.Unspecified,
        text = Color.Unspecified,
    )
}


val stylelessTextFieldColors
    @Composable get() = TextFieldDefaults.colors(
        focusedTextColor = Color.TextBlack,
        unfocusedTextColor = Color.TextBlack,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )

val melonCardColors
    @Composable get() = CardColors(
        containerColor = MelonBoxTheme.colors.cardBackground,
        contentColor = MelonBoxTheme.colors.text,
        disabledContainerColor = MelonBoxTheme.colors.cardBackground,
        disabledContentColor = MelonBoxTheme.colors.text
    )

