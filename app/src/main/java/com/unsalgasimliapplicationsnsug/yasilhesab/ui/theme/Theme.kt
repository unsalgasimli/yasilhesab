package com.unsalgasimliapplicationsnsug.yasilhesab.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val EcoLightColorScheme = lightColorScheme(
    primary = ecoGreen,
    onPrimary = Color.White,
    background = ecoLightBg,
    onBackground = ecoLightOnBg,
    surface = ecoLightSurface,
    onSurface = ecoDark,
    error = ecoError,
    outline = ecoLightBorder
)

private val EcoDarkColorScheme = darkColorScheme(
    primary = ecoGreen,
    onPrimary = Color.Black,
    background = ecoDarkBg,
    onBackground = ecoDarkOnBg,
    surface = ecoDarkSurface,
    onSurface = Color.White,
    error = ecoError,
    outline = ecoDarkBorder
)

@Composable
fun EcoTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) EcoDarkColorScheme else EcoLightColorScheme

    // Provide custom colors (passive, accent, etc) using CompositionLocal
    val textPassive = if (darkTheme) ecoTextPassiveDark else ecoTextPassiveLight

    CompositionLocalProvider(LocalEcoColors provides EcoColors(
        textPassive = textPassive
    )) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = EcoTypography,
            shapes = EcoShapes,
            content = content
        )
    }
}

// Custom EcoColors class for extra colors
data class EcoColors(
    val textPassive: Color,
)
val LocalEcoColors = staticCompositionLocalOf { EcoColors(textPassive = ecoTextPassiveDark) }

// Accessor (call in composables)
val EcoTextPassive @Composable get() = LocalEcoColors.current.textPassive
val EcoGreen get() = ecoGreen
val EcoGreenLight get() = ecoGreenLight
val EcoDark get() = ecoDark
val EcoError get() = ecoError
val EcoBg @Composable get() = MaterialTheme.colorScheme.background
val EcoCardBg @Composable get() = MaterialTheme.colorScheme.surface
val EcoBorder @Composable get() = MaterialTheme.colorScheme.outline
val EcoTextNormal get() = Color.White

// Optionally define shapes and typography here
val EcoTypography = Typography()
val EcoShapes = Shapes()
