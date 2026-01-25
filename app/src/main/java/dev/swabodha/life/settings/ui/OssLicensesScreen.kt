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
fun OssLicensesScreen() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header =====
            AppHeader(
                title = "Open-Source Licenses",
                subtitle = "Disclosure on what we use for the app to work"
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
                        text = """
                            This app uses the following open-source software:

                            • Android Open Source Project (AOSP)
                              License: Apache License 2.0

                            • AndroidX Libraries
                              License: Apache License 2.0

                            • Jetpack Compose
                              License: Apache License 2.0

                            Full license texts are available at:
                            https://www.apache.org/licenses/LICENSE-2.0
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}
