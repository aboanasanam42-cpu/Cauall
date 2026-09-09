package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val MathDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = DeepSpaceBlack,
    primaryContainer = GlassSurface,
    onPrimaryContainer = NeonCyan,
    secondary = NeonPurple,
    onSecondary = TextWhite,
    secondaryContainer = GlassSurfaceDark,
    onSecondaryContainer = TextPurple,
    tertiary = NeonMagenta,
    onTertiary = TextWhite,
    background = DeepSpaceBlack,
    onBackground = TextWhite,
    surface = DeepNavyCard,
    onSurface = TextWhite,
    surfaceVariant = GlassSurface,
    onSurfaceVariant = TextMuted,
    outline = NeonCyan
)

@Composable
fun MathLibraryTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MathDarkColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MathLibraryTheme(content = content)
}
