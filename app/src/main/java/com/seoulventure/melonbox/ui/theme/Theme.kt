package com.seoulventure.melonbox.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


@Composable
fun MelonBoxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val melonColors = MelonColors(
        btnDisabled = Color(0xFFB6B6B6),
        btnEnabled = Color(0xFFB0D699),
        warning = Color(0xFFFF6E6E),
        melon = Color(0xFFB0D699),
        selectedItemBackground = Color(0xFFDCF5CC),
        textNotImportant = Color(0xFFC2C2C2),
        iconGray = Color(0xFFD8D8D8),
        cardBackground = Color(0xFFFFFFFF),
        text = Color(0xFF2B2B2B),
        background = Color(0xFFEDEDED)
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = melonColors.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalMelonColors provides melonColors
    ) {
        MaterialTheme(
            typography = Typography,
            content = content
        )
    }
}

object MelonBoxTheme {
    val colors: MelonColors
        @Composable
        get() = LocalMelonColors.current
}