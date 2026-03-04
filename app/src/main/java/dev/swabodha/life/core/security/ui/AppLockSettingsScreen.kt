package dev.swabodha.life.core.security.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.swabodha.life.core.security.AppLockPrefs
import dev.swabodha.life.settings.ui.components.SettingsItem
import dev.swabodha.life.settings.ui.components.SettingsSwitchItem
import dev.swabodha.life.ui.components.AppHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLockSettingsScreen() {

    val context = LocalContext.current
    val prefs = remember { AppLockPrefs.get(context) }

    var enabled by remember { mutableStateOf(prefs.isEnabled()) }

    var showPinSetup by remember { mutableStateOf(false) }
    var showTimeoutDialog by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header =====
            AppHeader(
                title = "App Lock",
                subtitle = "Let's keep the peeping tom out ...!"
            )

            // ===== Content Card =====
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp),
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 3.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    SettingsSwitchItem(
                        icon = Icons.Outlined.Lock,
                        title = "Enable App Lock",
                        subtitle = "Require PIN or biometrics",
                        checked = enabled,
                        onCheckedChange = {

                            if (it && !prefs.hasPin()) {
                                showPinSetup = true
                            } else {
                                prefs.setEnabled(it)
                                enabled = it
                            }
                        }
                    )

                    if (enabled) {

                        SettingsItem(
                            icon = Icons.Outlined.Password,
                            title = "Change PIN",
                            subtitle = "Update your unlock PIN",
                            onClick = {
                                showPinSetup = true
                            }
                        )

                        SettingsItem(
                            icon = Icons.Outlined.Schedule,
                            title = "Lock timeout",
                            subtitle = "${prefs.getTimeoutSeconds()} seconds",
                            onClick = {
                                showTimeoutDialog = true
                            }
                        )
                    }
                }

                if (showPinSetup) {
                    PinSetupScreen {
                        showPinSetup = false
                    }
                }
                if (showTimeoutDialog) {
                    LockTimeoutDialog {
                        showTimeoutDialog = false
                    }
                }
            }
        }
    }
}