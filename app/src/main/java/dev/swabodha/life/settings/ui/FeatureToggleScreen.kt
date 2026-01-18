package dev.swabodha.life.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.swabodha.life.features.FeatureRegistry
import dev.swabodha.life.settings.data.FeaturePrefs
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureToggleScreen() {
    val context = LocalContext.current
    val prefs = remember { FeaturePrefs(context) }
    val headerTint = rememberTimeTint()

    val features = remember {
        FeatureRegistry.all().map { it.descriptor() }
    }

    Scaffold { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                headerTint,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Text(
                        text = "Enable / Disable Features",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Control which features appear in the app",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ===== Content Card =====
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp),
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 3.dp
            ) {
                LazyColumn(
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    items(features.size) { index ->
                        val feature = features[index]

                        var enabled by remember {
                            mutableStateOf(
                                prefs.isEnabled(feature.id, feature.enabled)
                            )
                        }

                        FeatureToggleItem(
                            title = feature.title,
                            checked = enabled,
                            onCheckedChange = {
                                enabled = it
                                prefs.setEnabled(feature.id, it)
                            }
                        )
                    }

                    item {
                        Spacer(Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureToggleItem(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun rememberTimeTint(): Color {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 5..11 -> MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
        in 12..16 -> MaterialTheme.colorScheme.tertiary.copy(alpha = 0.16f)
        in 17..21 -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.16f)
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f)
    }
}
