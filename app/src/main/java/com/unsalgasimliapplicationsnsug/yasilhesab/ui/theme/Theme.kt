package com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme

import android.os.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = ecoGreen,
    onPrimary = Color.White,
    background = LightBg,
    onBackground = LightOnBg,
    surface = LightSurface,
    onSurface = ecoDark,
    error = ecoError,
)

private val DarkColorScheme = darkColorScheme(
    primary = ecoGreen,
    onPrimary = Color.Black,
    background = DarkBg,
    onBackground = DarkOnBg,
    surface = DarkSurface,
    onSurface = Color.White,
    error = ecoError,
)

@Composable
fun YasilhesabTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
