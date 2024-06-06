package com.team4studio.travelnow.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
//// onpriary  0, 25, 83
//    onPrimary = Color(22, 51, 113), //anything (e.g Text) on a component that uses primary color
//    onSecondary = Color(0xFF000000), // 242B3B
//    onTertiary = Color(0xFFE91E63),
//    onSurfaceVariant = Color(0xFFB1C7FF), //text in cards that do not use the defailt color
//
//    onBackground = Color(0xFF1C1B1F),
////onSurface = Color(0xFF2900A5),
//
//    secondaryContainer = Color(0xFFB1C7FF), //top bar and selected bottom bar and filter chips background
//    onPrimaryContainer = Color(0xFFadc7ff), //
//    primaryContainer = Color(0xFF000000),
//    onSecondaryContainer = Color(0xFFB1C7FF),
//    surfaceVariant = Color(0xFF000000) // card color
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40, //button container color
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = Color.White, //pages
    surface = Color.White, //surface login mobile

    onPrimary = Color(219, 229, 250), //anything (e.g Text) on a component that uses primary color
    onSecondary = Color(0xFF000000), // 242B3B
    onTertiary = Color(0xFFE91E63),
    onSurfaceVariant = Color(0xFF000000), //text in cards that do not use the defailt color

    onBackground = Color(0xFF1C1B1F),
    //onSurface = Color(0xFF2900A5),
    secondaryContainer = Color(0xFFB1C7FF), //top bar and selected bottom bar and filter chips background
    onPrimaryContainer = Color(0xFF000000), //
    primaryContainer = Color(0xFFadc7ff),
    onSecondaryContainer = Color(0xFF000000),
    surfaceVariant = Color(0xFFB1C7FF) // card color


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

@Composable
fun EmerceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
//    val colorScheme = pickColorScheme(dynamicColor, darkTheme)
//    val view = LocalView.current
//
//    if (!view.isInEditMode) {
//        /* getting the current window by tapping into the Activity */
//        val currentWindow = (view.context as? Activity)?.window
//            ?: throw Exception("Not in an activity - unable to get Window reference")
//
//        SideEffect {
//            /* the default code did the same cast here - might as well use our new variable! */
//            currentWindow.statusBarColor = colorScheme.primary.toArgb()
//            /* accessing the insets controller to change appearance of the status bar, with 100% less deprecation warnings */
//            WindowCompat.getInsetsController(currentWindow, view).isAppearanceLightStatusBars =
//                darkTheme
//        }
//    }


}

//@Composable
//fun pickColorScheme(
//    dynamicColor: Boolean,
//    darkTheme: Boolean
//): ColorScheme {
//    return when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//        darkTheme -> DarkColorScheme
//        else -> LightColorScheme
//    }
//}