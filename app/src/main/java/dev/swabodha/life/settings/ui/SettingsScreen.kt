package dev.swabodha.life.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val headerTint = rememberTimeTint()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header (MATCHED WITH HOME) =====
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
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
                        text = "Settings",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Manage your app & preferences",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ===== Content Card =====
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-12).dp),
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 12.dp)
                ) {
                    SettingsSection("Account & Sync") {
                        SettingsItem(Icons.Outlined.Person, "Sign in / Sign out")
                        SettingsItem(Icons.Outlined.Sync, "Sync status")
                        SettingsItem(Icons.Outlined.Schedule, "Sync frequency")
                        SettingsItem(Icons.Outlined.CloudSync, "Sync now")
                        SettingsItem(Icons.Outlined.Devices, "Device list")
                        SettingsItem(Icons.Outlined.Warning, "Reset cloud data", danger = true)
                    }

                    SettingsSection("Privacy & Security") {
                        SettingsItem(Icons.Outlined.Lock, "App lock")
                        SettingsItem(Icons.Outlined.Security, "Encrypted database")
                        SettingsItem(Icons.Outlined.VisibilityOff, "Hide sensitive content")
                        SettingsItem(Icons.Outlined.Block, "Screenshot protection")
                        SettingsItem(Icons.Outlined.DeleteForever, "Delete all local data", danger = true)
                    }

                    SettingsSection("Feature Controls") {
                        SettingsItem(Icons.Outlined.ToggleOn, "Enable / Disable features")
                        SettingsItem(Icons.Outlined.DragIndicator, "Reorder home tiles")
                    }

                    SettingsSection("Appearance") {
                        SettingsItem(Icons.Outlined.Palette, "Theme")
                        SettingsItem(Icons.Outlined.Dashboard, "Home layout")
                    }

                    SettingsSection("About") {
                        SettingsItem(Icons.Outlined.Info, "App version")
                        SettingsItem(Icons.Outlined.Policy, "Privacy policy")
                        SettingsItem(Icons.Outlined.Code, "Open-source licenses")
                        SettingsItem(Icons.Outlined.Email, "Contact & feedback")
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}


@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Spacer(Modifier.height(20.dp))
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 24.dp),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.height(8.dp))
    Column(content = content)
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    danger: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (danger)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = if (danger)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.onSurface
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
