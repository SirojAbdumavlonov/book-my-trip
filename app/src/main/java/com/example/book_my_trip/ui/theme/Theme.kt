package com.example.book_my_trip.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// App colors based on Figma design
val Primary = Color(0xFF4C40F7)
val Secondary = Color(0xFFEBF0FF)
val Background = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF000000)
val TextSecondary = Color(0xFF6E6E6E)
val AccentYellow = Color(0xFFFFBE3C)
val AccentPink = Color(0xFFF46880)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    background = Background,
    surface = Background,
    onPrimary = Color.White,
    onSecondary = Primary,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    secondary = Color(0xFF1D1B3E),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun TiniFlightBookingTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CustomTypography,
        content = content
    )
}