package com.kaushal.japacountercompose.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE5BFA9),
    onPrimary = Color(0xFF452112),
    background = Color(0xFF1B110D),
    onBackground = Color(0xFFF4ECE8),
    surface = Color(0xFF241813),
    onSurface = Color(0xFFF4ECE8),
    surfaceVariant = Color(0xFF33231D),
    onSurfaceVariant = Color(0xFFC5B4AD),
    outline = Color(0xFF5A463E)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF452112),
    onPrimary = Color.White,
    background = Color(0xFFFAF6F5),
    onBackground = Color(0xFF452112),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF452112),
    surfaceVariant = Color(0xFFE3DAD8),
    onSurfaceVariant = Color(0xFF6B4F44),
    outline = Color(0xFFC2AEA4)
)

@Composable
fun JapaCounterComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}