package dev.swabodha.life.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,

    background = androidx.compose.ui.graphics.Color(0xFF000000),
    surface = androidx.compose.ui.graphics.Color(0xFF000000),

    onPrimary = androidx.compose.ui.graphics.Color.Black,
    onSecondary = androidx.compose.ui.graphics.Color.White,
    onTertiary = androidx.compose.ui.graphics.Color.Black,

    onBackground = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.White
)


private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun SwaBodhaTheme(
    themeMode: dev.swabodha.life.settings.data.ThemeMode,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when (themeMode) {

        dev.swabodha.life.settings.data.ThemeMode.SYSTEM -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (isSystemInDarkTheme())
                    dynamicDarkColorScheme(context)
                else
                    dynamicLightColorScheme(context)
            } else {
                if (isSystemInDarkTheme())
                    DarkColorScheme
                else
                    LightColorScheme
            }
        }

        dev.swabodha.life.settings.data.ThemeMode.DARK -> {
            DarkColorScheme
        }

        dev.swabodha.life.settings.data.ThemeMode.LIGHT -> {
            LightColorScheme
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}