package dev.swabodha.life.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.swabodha.life.features.FeatureRegistry
import dev.swabodha.life.settings.data.FeaturePrefs

@Composable
fun FeatureToggleScreen() {
    val context = LocalContext.current
    val prefs = remember { FeaturePrefs(context) }

    val features = FeatureRegistry.all()
        .map { it.descriptor() }

    Column(Modifier.padding(24.dp)) {
        Text(
            "Enable / Disable Features",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(24.dp))

        features.forEach { feature ->
            var enabled by remember {
                mutableStateOf(
                    prefs.isEnabled(feature.id, feature.enabled)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(feature.title)

                Switch(
                    checked = enabled,
                    onCheckedChange = {
                        enabled = it
                        prefs.setEnabled(feature.id, it)
                    }
                )
            }
        }
    }
}
