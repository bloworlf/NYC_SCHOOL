package com.example.nyc_school.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Immutable
data class CustomColorsPalette(
    val backgroundColor: Color = Color.White,
    val onBackgroundColor: Color = Color(0xFFD6D6D6),
    val foregroundColor: Color = Color(0xFF507834),
    val buttonContainerColor: Color = Color(0xFF507834),
    val statusColor: Color = Color.White,
    val navigationColor: Color = Color.White,
    val iconColor: Color = Color(0xFF61703C),
    val menuIconColor: Color = Color.Black,
    val titleTextColor: Color = Color.Black,
    val subTextColor: Color = Color.DarkGray,
    val bodyTextColor: Color = Color(0xFF383838),
    val borderColor: Color = Color.Gray,
    val watermarkTextColor: Color = Color.White,
    val toolbarColor: Color = Color.White,
    val bottomNavigationBarColor: Color = Color.White,
)

val LocalCustomColorsPalette = staticCompositionLocalOf { CustomColorsPalette() }

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

private val CustomLightScheme = CustomColorsPalette()
private val CustomDarkScheme = CustomColorsPalette(
    backgroundColor = Color(0xFF222222),
    onBackgroundColor = Color(0xFF424242),
    titleTextColor = Color.White,
    bodyTextColor = Color(0xFFC5C5C5),
    watermarkTextColor = Color(0xFF222222),
    borderColor = Color.LightGray,
    iconColor = Color(0xFF299A0B),
    menuIconColor = Color.White,
    navigationColor = Color(0xFF222222),
    bottomNavigationBarColor = Color(0xFF222222),
    statusColor = Color(0xFF222222),
)

val MaterialTheme.customColorsPalette: CustomColorsPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColorsPalette.current

@Composable
fun NYC_SCHOOLTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    val colorScheme = when {
        darkTheme -> CustomDarkScheme
        else -> CustomLightScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.statusColor.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    CompositionLocalProvider(
        LocalCustomColorsPalette provides colorScheme
    ) {
        MaterialTheme(
            colorScheme = colors,
            typography = Typography,
            shapes = Shapes(),
            content = content
        )
    }
}