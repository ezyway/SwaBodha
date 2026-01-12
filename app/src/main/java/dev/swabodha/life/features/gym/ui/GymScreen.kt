package dev.swabodha.life.features.gym.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.swabodha.life.features.gym.data.entity.BodyPart
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun GymScreen(
    vm: GymViewModel = viewModel()
) {
    val entries by vm.entries.collectAsState()
    val selected = remember { mutableStateMapOf<BodyPart, Boolean>() }

    Column(Modifier.padding(16.dp)) {
        Text("Gym Log", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(12.dp))

        BodyPart.values().forEach { part ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(
                    checked = selected[part] == true,
                    onCheckedChange = {
                        selected[part] = it
                    }
                )
                Text(part.name.lowercase().replaceFirstChar { it.uppercase() })
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = {
                vm.log(
                    selected.filter { it.value }.keys.toList()
                )
                selected.clear()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Log workout")
        }

        Spacer(Modifier.height(24.dp))

        LazyColumn {
            items(entries) { entry ->
                Text(
                    text = "${formatDate(entry.date)} → ${entry.bodyParts.joinToString()}"
                )
            }
        }
    }
}

private fun formatDate(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))
