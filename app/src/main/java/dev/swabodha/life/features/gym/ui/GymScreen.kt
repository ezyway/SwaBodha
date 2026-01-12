package dev.swabodha.life.features.gym.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.swabodha.life.features.gym.data.entity.BodyPart
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymScreen(
    vm: GymViewModel = viewModel()
) {
    val entries by vm.entries.collectAsState()

    // ⚠️ MUST be remember, not rememberSaveable
    val selected = remember {
        mutableStateMapOf<BodyPart, Boolean>()
    }

    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ===== Header =====
            item {
                Column(Modifier.padding(24.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Gym log",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Track the muscle groups you train.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        thickness = 1.dp
                    )
                }
            }

            // ===== Body Part Selection =====
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Worked muscles",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BodyPart.values().forEach { part ->
                            FilterChip(
                                selected = selected[part] == true,
                                onClick = {
                                    selected[part] = !(selected[part] ?: false)
                                },
                                label = {
                                    Text(
                                        part.name
                                            .lowercase()
                                            .replaceFirstChar { it.uppercase() }
                                    )
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        onClick = {
                            vm.log(
                                selected.filter { it.value }.keys.toList()
                            )
                            selected.clear()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selected.any { it.value }
                    ) {
                        Text("Log workout")
                    }
                }
            }

            // ===== History =====
            item {
                AnimatedVisibility(visible = entries.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "History",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(8.dp))
                    }
                }
            }

            items(entries) { entry ->
                ListItem(
                    headlineContent = {
                        Text(formatDate(entry.date))
                    },
                    supportingContent = {
                        Text(
                            entry.bodyParts.joinToString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }

            item {
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

private fun formatDate(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        .format(Date(millis))
