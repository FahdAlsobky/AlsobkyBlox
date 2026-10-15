package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val BloxFruitsDarkColorScheme = darkColorScheme(
    primary = BfPurplePrimary,
    onPrimary = BfTextPrimary,
    primaryContainer = BfDarkCardElevated,
    onPrimaryContainer = BfPurpleLight,
    secondary = BfGold,
    onSecondary = BfDarkBg,
    secondaryContainer = BfDarkSurfaceVariant,
    onSecondaryContainer = BfGoldLight,
    tertiary = BfCyan,
    onTertiary = BfDarkBg,
    background = BfDarkBg,
    onBackground = BfTextPrimary,
    surface = BfDarkSurface,
    onSurface = BfTextPrimary,
    surfaceVariant = BfDarkSurfaceVariant,
    onSurfaceVariant = BfTextSecondary,
    outline = BfBorder
)

@Composable
fun BloxFruitsTheme(
    darkTheme: Boolean = true, // Default to rich Blox Fruits dark theme
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            window?.let {
                it.statusBarColor = BfDarkBg.toArgb()
                it.navigationBarColor = BfDarkBg.toArgb()
                WindowCompat.getInsetsController(it, view).apply {
                    isAppearanceLightStatusBars = false
                    isAppearanceLightNavigationBars = false
                }
            }
        }
    }

    MaterialTheme(
        colorScheme = BloxFruitsDarkColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    BloxFruitsTheme(content = content)
}
