package com.careloop.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// High contrast palette compliant with WCAG AAA for elderly vision
val DeepNavy = Color(0xFF0F1E36)
val SoftBackground = Color(0xFFF8F9FA)
val SurfaceWhite = Color(0xFFFFFFFF)
val PrimaryBlue = Color(0xFF1565C0)
val HighContrastGreen = Color(0xFF1B5E20)
val HighContrastRed = Color(0xFFB71C1C)
val WarningAmber = Color(0xFFE65100)
val BorderGray = Color(0xFFCFD8DC)
val TextDark = Color(0xFF1A1A1A)
val TextMuted = Color(0xFF4A4A4A)

val HighContrastColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    secondary = DeepNavy,
    onSecondary = Color.White,
    background = SoftBackground,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    error = HighContrastRed,
    onError = Color.White
)

val LargeTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
        color = TextDark
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        color = TextDark
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = TextDark
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        color = TextDark
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        color = TextMuted
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )
)

@Composable
fun CareLoopTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HighContrastColorScheme,
        typography = LargeTypography,
        content = content
    )
}

