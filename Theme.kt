package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PolishPrimaryContainer,
    onPrimary = PolishOnPrimaryContainer,
    primaryContainer = PolishPrimary,
    onPrimaryContainer = PolishOnPrimary,
    secondary = PolishSecondaryContainer,
    onSecondary = PolishOnSecondaryContainer,
    background = PolishBackgroundDark,
    onBackground = PolishOnBackgroundDark,
    surface = PolishSurfaceDark,
    onSurface = PolishOnBackgroundDark,
    surfaceVariant = PolishSurfaceVariantDark,
    onSurfaceVariant = PolishOutlineDark,
    outline = PolishOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = PolishPrimary,
    onPrimary = PolishOnPrimary,
    primaryContainer = PolishPrimaryContainer,
    onPrimaryContainer = PolishOnPrimaryContainer,
    secondary = PolishSecondaryContainer,
    onSecondary = PolishOnSecondaryContainer,
    background = PolishBackgroundLight,
    onBackground = PolishOnBackgroundLight,
    surface = PolishSurfaceLight,
    onSurface = PolishOnBackgroundLight,
    surfaceVariant = PolishSurfaceVariantLight,
    onSurfaceVariant = PolishOutlineLight,
    outline = PolishOutlineLight
)

@Composable
fun UbuntuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    UbuntuTheme(darkTheme = darkTheme, content = content)
}

