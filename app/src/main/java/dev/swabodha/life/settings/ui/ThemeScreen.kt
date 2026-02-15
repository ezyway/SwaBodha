package dev.swabodha.life.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.swabodha.life.settings.data.ThemeMode
import dev.swabodha.life.settings.data.ThemePrefs

@Composable
fun ThemeScreen() {

    val context = LocalContext.current
    val prefs = remember { ThemePrefs.get(context) }
    val currentMode by prefs.mode.collectAsState()

    Column(Modifier.padding(24.dp)) {

        Text("Theme", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(24.dp))

        ThemeMode.values().forEach { mode ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                RadioButton(
                    selected = currentMode == mode,
                    onClick = { prefs.setMode(mode) }
                )
                Spacer(Modifier.width(12.dp))
                Text(mode.name.lowercase().replaceFirstChar { it.uppercase() })
            }
        }
    }
}
