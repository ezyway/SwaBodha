package dev.swabodha.life

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import dev.swabodha.life.navigation.AppNavHost
import dev.swabodha.life.settings.data.ScreenshotProtectionPrefs
import dev.swabodha.life.settings.data.ThemeMode
import dev.swabodha.life.settings.data.ThemePrefs
import dev.swabodha.life.ui.theme.SwaBodhaTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var themePrefs: ThemePrefs
    private lateinit var screenshotPrefs: ScreenshotProtectionPrefs

    private var currentThemeMode by mutableStateOf(ThemeMode.SYSTEM)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)

        themePrefs = ThemePrefs.get(this)
        screenshotPrefs = ScreenshotProtectionPrefs(this)

        // Load initial theme immediately
        currentThemeMode = themePrefs.mode.value

        // Observe theme changes
        lifecycleScope.launch {
            themePrefs.mode.collectLatest {
                currentThemeMode = it
            }
        }

        // Apply screenshot flag immediately
        if (screenshotPrefs.enabled.value) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        lifecycleScope.launch {
            screenshotPrefs.enabled.collectLatest { enabled ->
                if (enabled) {
                    window.setFlags(
                        WindowManager.LayoutParams.FLAG_SECURE,
                        WindowManager.LayoutParams.FLAG_SECURE
                    )
                } else {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
                }
            }
        }

        setContent {

            val themePrefs = remember { ThemePrefs.get(this) }
            val themeMode by themePrefs.mode.collectAsState(initial = ThemeMode.SYSTEM)


            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            val activity = this@MainActivity

            LaunchedEffect(darkTheme) {
                val controller = WindowInsetsControllerCompat(
                    activity.window,
                    activity.window.decorView
                )

                // Light status bar icons when background is light
                controller.isAppearanceLightStatusBars = !darkTheme

                // Light navigation bar icons when background is light
                controller.isAppearanceLightNavigationBars = !darkTheme

            }


            SwaBodhaTheme(themeMode = themeMode) {
                AppNavHost()
            }

        }

    }
}
