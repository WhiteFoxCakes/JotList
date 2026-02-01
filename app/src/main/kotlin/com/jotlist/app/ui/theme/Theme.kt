package com.jotlist.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = ElectricBlue,
    onPrimary = White,
    primaryContainer = ElectricBlue,
    onPrimaryContainer = White,

    // Secondary colors
    secondary = Violet,
    onSecondary = White,
    secondaryContainer = Violet,
    onSecondaryContainer = White,

    // Tertiary colors
    tertiary = NeonGreen,
    onTertiary = White,
    tertiaryContainer = NeonGreen,
    onTertiaryContainer = White,

    // Error colors
    error = SoftRed,
    onError = White,
    errorContainer = SoftRed,
    onErrorContainer = White,

    // Background colors
    background = DarkBackground,
    onBackground = White,

    // Surface colors
    surface = SurfaceCard,
    onSurface = White,
    surfaceVariant = InputSurface,
    onSurfaceVariant = SoftGrey,

    // Outline
    outline = DarkGrey,
    outlineVariant = DarkGrey
)

@Composable
fun JotListTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
