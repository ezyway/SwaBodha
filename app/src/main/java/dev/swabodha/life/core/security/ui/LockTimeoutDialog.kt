package dev.swabodha.life.core.security.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.swabodha.life.core.security.AppLockPrefs

@Composable
fun LockTimeoutDialog(
    onDismiss: () -> Unit
) {

    val context = LocalContext.current
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
            TextButton(
                onClick = {
                    prefs.setTimeoutSeconds(selected)
                    onDismiss()
                }
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Lock timeout") },
        text = {

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                items(options) { (value, label) ->

                    TimeoutItem(
                        label = label,
                        selected = selected == value,
                        onSelect = { selected = value }
                    )
                }
            }
        }
    )
}

@Composable
private fun TimeoutItem(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit
) {

    val interaction = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interaction,
                indication = null   // avoids ripple crash
            ) { onSelect() },
        tonalElevation = if (selected) 2.dp else 0.dp,
        shape = MaterialTheme.shapes.medium
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = label,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge
            )

            RadioButton(
                selected = selected,
                onClick = onSelect
            )
        }
    }
}