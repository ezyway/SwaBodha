package dev.swabodha.life.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.swabodha.life.ui.components.AppHeader
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToFeatureToggles: () -> Unit,
    onNavigateToReorderHomeTiles: () -> Unit,
    onNavigateToPrivacyPolicy: () -> Unit,
    onNavigateToOssLicenses: () -> Unit,
    onNavigateToContact: () -> Unit
) {
    val headerTint = rememberTimeTint()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header =====
            AppHeader(
                title = "Settings",
                subtitle = "Manage your app, in your own way"
            )

            // ===== Content Card =====
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = (12).dp),
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 12.dp)
                ) {
                    SettingsSection("Feature Controls") {
                        SettingsItem(
                            icon = Icons.Outlined.ToggleOn,
                            title = "Enable / Disable features",
                            subtitle = "Show or hide app features",
                            onClick = onNavigateToFeatureToggles
                        )

                        SettingsItem(
                            icon = Icons.Outlined.DragIndicator,
                            title = "Reorder home tiles",
                            subtitle = "Change feature order",
                            onClick = onNavigateToReorderHomeTiles
                        )
                    }

                    SettingsSection("Account & Sync") {
                        SettingsItem(
                            icon = Icons.Outlined.Person,
                            title = "Sign in / Sign out",
                            subtitle = "Manage your account"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Sync,
                            title = "Sync status",
                            subtitle = "Last sync and errors"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Schedule,
                            title = "Sync frequency",
                            subtitle = "How often data is synced"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.CloudSync,
                            title = "Sync now",
                            subtitle = "Force a manual sync"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Devices,
                            title = "Device list",
                            subtitle = "Devices using this account"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Warning,
                            title = "Reset cloud data",
                            subtitle = "Deletes all synced data",
                            danger = true
                        )
                    }

                    SettingsSection("Privacy & Security") {
                        SettingsItem(
                            icon = Icons.Outlined.Lock,
                            title = "App lock",
                            subtitle = "PIN / biometrics"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Security,
                            title = "Encrypted database",
                            subtitle = "Local data protection"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.VisibilityOff,
                            title = "Hide sensitive content",
                            subtitle = "Blur previews and notifications"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Block,
                            title = "Screenshot protection",
                            subtitle = "Prevent screenshots"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.DeleteForever,
                            title = "Delete all local data",
                            subtitle = "This cannot be undone",
                            danger = true
                        )
                    }

                    SettingsSection("Appearance") {
                        SettingsItem(
                            icon = Icons.Outlined.Palette,
                            title = "Theme",
                            subtitle = "Light / Dark / System"
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Dashboard,
                            title = "Home layout",
                            subtitle = "Grid and spacing"
                        )
                    }

                    SettingsSection("About") {
                        SettingsItem(
                            icon = Icons.Outlined.Info,
                            title = "App version",
                            subtitle = appVersionSubtitle()
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Policy,
                            title = "Privacy policy",
                            subtitle = "How your data is handled",
                            onClick = onNavigateToPrivacyPolicy
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Code,
                            title = "Open-source licenses",
                            subtitle = "Libraries used",
                            onClick = onNavigateToOssLicenses
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Email,
                            title = "Contact & feedback",
                            subtitle = "Report issues or suggest features",
                            onClick = onNavigateToContact
                        )
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
    subtitle: String? = null,
    danger: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )

            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (danger)
                MaterialTheme.colorScheme.error
            else
                MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (danger)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurface
            )

            subtitle?.let {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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
