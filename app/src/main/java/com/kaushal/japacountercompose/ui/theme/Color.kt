package com.kaushal.japacountercompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val BrandColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color(0xFFE5BFA9) else Color(0xFF452112)

val AlphaBrandColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color(0xFFE5BFA9).copy(0.2f) else BrandColor.copy(0.5f)

val ProgressBarBgColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color(0xFF5A463E) else Color(0xFFC2AEA4)

val LightTextColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color(0xFFC5B4AD) else Color(0xFF6B4F44)

val JapaCardColor: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color(0xFF2D1E19) else Color(0xFFE3DAD8)

val NotStarted: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color(0xFF9F8F7F) else Color(0xFFBFAF9F)

val Active: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color(0xFF90B0D0) else Color(0xFF7D9AB8)

val Completed: Color
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) Color(0xFF9CBA8F) else Color(0xFF8AA57E)

val EmptyJapaListText = Color.Gray

val SuccessGreen = Color(0xFF2E7D32)
val ErrorRed = Color(0xFFC62828)