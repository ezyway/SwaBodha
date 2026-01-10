package dev.swabodha.life

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import dev.swabodha.life.core.ui.SwabodhaTheme
import dev.swabodha.life.navigation.AppNavHost
import dev.swabodha.life.ui.theme.SwaBodhaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true // dark icons

        setContent {
            SwaBodhaTheme {
                setContent {
                    SwabodhaTheme {
                        AppNavHost()
                    }
                }
            }
        }
    }
}
