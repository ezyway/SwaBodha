package dev.swabodha.life.settings.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.RichText
import dev.swabodha.life.ui.components.AppHeader
import dev.swabodha.life.util.fetchMarkdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen() {

    val markdownState = produceState<String?>(initialValue = null) {
        value = try {
            fetchMarkdown(
                "https://raw.githubusercontent.com/ezyway/SwaBodha/refs/heads/dev/docs/Privacy%20Policy.md"
            )
        } catch (e: Exception) {
            Log.e("PrivacyPolicy", "Markdown load failed", e)
            "Failed to load privacy policy."
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header =====
            AppHeader(
                title = "Privacy Policy",
                subtitle = "How your data is handled inside the app"
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

                    when (val md = markdownState.value) {
                        null -> {
                            CircularProgressIndicator()
                        }
                        else -> {
                            RichText {
                                Markdown(md)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                }
            }
        }
    }
}
