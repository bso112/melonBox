package com.seoulventure.melonbox.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val dark_surfaceVariant = Color(0xFF1D2125)

const val BACKGROUND_PREVIEW: Long = 0xEDEDED

val color_background_light = Color(0xFFEDEDED)

val color_background @Composable get() = color_background_light
val color_btn_disable @Composable get() = Color(0xFFB6B6B6)
val color_btn_enable @Composable get() = color_melon
val color_warning @Composable get() = Color(0xFFFF6E6E)
val color_melon @Composable get() = Color(0xFFB0D699)

val color_selected_item_background @Composable get() = Color(0xFFDCF5CC)
val color_text_not_important @Composable get() = Color(0xFFC2C2C2)

val color_icon_gray @Composable get() = Color(0xFFD8D8D8)

val color_card_background @Composable get() = if (isSystemInDarkTheme()) Color(0xFF1D2125) else Color.White

val color_text @Composable get() = if (isSystemInDarkTheme()) Color.White else color_text_black

val color_text_black = Color(0xFF2B2B2B)


val stylelessTextFieldColors
    @Composable get() = TextFieldDefaults.colors(
        focusedTextColor = color_text_black,
        unfocusedTextColor = color_text_black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )

