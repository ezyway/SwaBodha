package dev.swabodha.life.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.swabodha.life.ui.components.AppHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptedDatabaseScreen() {

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header =====
            AppHeader(
                title = "Encrypted Database",
                subtitle = "How your local data is protected"
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
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {

                    Text(
                        text = "All data stored on this device is encrypted.",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "SwaBodha stores your information locally on your device. " +
                                "The database is encrypted to protect your records from " +
                                "unauthorized access.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "What this means:",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Spacer(Modifier.height(10.dp))

                    Text("• Your data is stored locally on your device")
                    Text("• Database files are encrypted")
                    Text("• Encryption is always enabled")
                    Text("• Your data remains private unless you enable sync")

                    Spacer(Modifier.height(20.dp))

                    Text(
                        text = "This protection helps keep your data safe if the device " +
                                "is lost or accessed by others.",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}