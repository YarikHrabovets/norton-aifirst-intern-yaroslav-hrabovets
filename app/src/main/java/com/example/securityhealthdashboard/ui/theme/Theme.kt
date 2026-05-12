package com.example.securityhealthdashboard.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = NortonYellow,
    onPrimary = NortonBlack,
    secondary = NortonBlack,
    onSecondary = Color.White,
    background = BackgroundLight,
    surface = CardWhite,
    onSurface = NortonBlack,
    onBackground = NortonBlack
)

@Composable
fun SecurityHealthDashboardTheme(
    content: @Composable () -> Unit
) {
    // Force light theme by ignoring darkTheme parameter
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
