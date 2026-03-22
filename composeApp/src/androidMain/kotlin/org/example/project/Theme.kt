package org.example.project

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

fun lightColorSchemeCustom() = lightColorScheme(
    primary = Color(0xFF9A89CC),
    onPrimary = Color.White,
    secondary = Color(0xFFE7CCCC),
    background = Color(0xFFF5F1EC),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF1ECE6),
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    onSurfaceVariant = Color(0xFF6B6B6B)
)

fun darkColorSchemeCustom() = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF2B1D52),
    secondary = Color(0xFFCCC2DC),
    background = Color(0xFF050505),
    surface = Color(0xFF121212),
    surfaceVariant = Color(0xFF2A2A2A),
    onBackground = Color(0xFFF5F5F5),
    onSurface = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFFD0D0D0)
)