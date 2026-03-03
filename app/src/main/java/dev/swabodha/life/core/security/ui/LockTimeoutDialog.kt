package dev.swabodha.life.core.security.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.*
import dev.swabodha.life.core.security.AppLockPrefs

@Composable
fun LockTimeoutDialog(
    onDismiss: () -> Unit
) {

    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember { AppLockPrefs.get(context) }

    val options = listOf(
        15 to "15 seconds",
        30 to "30 seconds",
        60 to "1 minute",
        120 to "2 minutes",
        300 to "5 minutes",
        600 to "10 minutes"
    )

    var selected by remember {
        mutableStateOf(prefs.getTimeoutSeconds())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                prefs.setTimeoutSeconds(selected)
                onDismiss()
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text("Lock timeout") },
        text = {
            Column {
                options.forEach { (value, label) ->
                    RadioButton(
                        selected = selected == value,
                        onClick = { selected = value }
                    )
                    Text(label)
                }
            }
        }
    )
}