package dev.swabodha.life.settings.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.swabodha.life.settings.data.ScreenshotProtectionPrefs
import dev.swabodha.life.settings.data.ThemeMode
import dev.swabodha.life.settings.data.ThemePrefs
import dev.swabodha.life.ui.components.AppHeader
import dev.swabodha.life.ui.components.rememberSnackbarController
import dev.swabodha.life.ui.components.rememberTimeTint
import kotlinx.coroutines.launch
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
    val context = LocalContext.current

    val snackbar = rememberSnackbarController()

    val themePrefs = remember { ThemePrefs.get(context) }
    val currentTheme by themePrefs.mode.collectAsState()

    var showThemeDialog by remember { mutableStateOf(false) }

    val screenshotPrefs = remember { ScreenshotProtectionPrefs(context) }
    val screenshotEnabled by screenshotPrefs.enabled.collectAsState()


    Scaffold(
        snackbarHost = { SnackbarHost(snackbar.hostState) }
    ) { padding ->
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
                            subtitle = "Manage your account",
                            onClick = { snackbar.comingSoon() }
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Sync,
                            title = "Sync status",
                            subtitle = "Last sync and errors",
                            onClick = { snackbar.comingSoon() }
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Schedule,
                            title = "Sync frequency",
                            subtitle = "How often data is synced",
                            onClick = { snackbar.comingSoon() }
                        )

                        SettingsItem(
                            icon = Icons.Outlined.CloudSync,
                            title = "Sync now",
                            subtitle = "Force a manual sync",
                            onClick = { snackbar.comingSoon() }
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Devices,
                            title = "Device list",
                            subtitle = "Devices using this account",
                            onClick = { snackbar.comingSoon() }
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
                            subtitle = "PIN / biometrics",
                            onClick = { snackbar.comingSoon() }
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Security,
                            title = "Encrypted database",
                            subtitle = "Local data protection",
                            onClick = { snackbar.comingSoon() }
                        )

                        SettingsItem(
                            icon = Icons.Outlined.VisibilityOff,
                            title = "Hide sensitive content",
                            subtitle = "Blur previews and notifications",
                            onClick = { snackbar.comingSoon() }
                        )

                        SettingsSwitchItem(
                            icon = Icons.Outlined.Block,
                            title = "Screenshot protection",
                            subtitle = "Prevent screenshots",
                            checked = screenshotEnabled,
                            onCheckedChange = {
                                screenshotPrefs.setEnabled(it)
                            }
                        )
                        SettingsItem(
                            icon = Icons.Outlined.DeleteForever,
                            title = "Clear App Data",
                            subtitle = "Opens Android system storage settings",
                            onClick = {
                                openAppSystemSettings(context)
                            },
                            danger = true
                        )
                    }

                    SettingsSection("Appearance") {
                        SettingsItem(
                            icon = Icons.Outlined.Palette,
                            title = "Theme",
                            subtitle = currentTheme.name.lowercase()
                                .replaceFirstChar { it.uppercase() },
                            onClick = { showThemeDialog = true }
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Dashboard,
                            title = "Home layout",
                            subtitle = "Grid and spacing",
                            onClick = { snackbar.comingSoon() }
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
    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Theme") },
            text = {
                Column {
                    ThemeMode.values().forEach { mode ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            RadioButton(
                                selected = currentTheme == mode,
                                onClick = {
                                    themePrefs.setMode(mode)
                                    showThemeDialog = false
                                }
                            )

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = mode.name.lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                    }
                }
            },
            confirmButton = {}
        )
    }

}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Spacer(Modifier.height(24.dp))

    Text(
        text = title.uppercase(),
        modifier = Modifier.padding(horizontal = 24.dp),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary
    )

    Spacer(Modifier.height(8.dp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    danger: Boolean = false,
    onClick: () -> Unit = {}
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                color = if (danger)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.onSurface
            )
        },
        supportingContent = subtitle?.let {
            { Text(it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (danger)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                onClick = onClick
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = subtitle?.let {
            { Text(it) }
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = null
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ) {
                onCheckedChange(!checked)
            }
    )
}

private fun openAppSystemSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}
