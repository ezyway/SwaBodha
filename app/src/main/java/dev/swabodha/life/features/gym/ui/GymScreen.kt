package dev.swabodha.life.features.gym.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.swabodha.life.features.gym.data.entity.BodyPart
import dev.swabodha.life.features.gym.data.entity.GymEntryEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymScreen(
    vm: GymViewModel = viewModel()
) {
    val entries by vm.entries.collectAsState()

    val selected = remember {
        mutableStateMapOf<BodyPart, Boolean>()
    }

    val now = remember { Calendar.getInstance() }

    var selectedDateMillis by remember { mutableStateOf(now.timeInMillis) }
    var selectedHour by remember { mutableStateOf(now.get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableStateOf(now.get(Calendar.MINUTE)) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        val state = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = state.selectedDateMillis ?: selectedDateMillis
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) {
            DatePicker(state = state)
        }
    }

    if (showTimePicker) {
        val state = rememberTimePickerState(
            initialHour = selectedHour,
            initialMinute = selectedMinute,
            is24Hour = false
        )

        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedHour = state.hour
                    selectedMinute = state.minute
                    showTimePicker = false
                }) { Text("OK") }
            },
            text = { TimePicker(state = state) }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
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
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }

            // ===== Date & Time =====
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Outlined.CalendarToday, null)
                        Spacer(Modifier.width(8.dp))
                        Text(formatDate(selectedDateMillis))
                    }

                    OutlinedButton(
                        onClick = { showTimePicker = true }
                    ) {
                        Icon(Icons.Outlined.Schedule, null)
                        Spacer(Modifier.width(8.dp))
                        Text(formatTime12h(selectedHour, selectedMinute))
                    }
                }
            }

            // ===== Worked Muscles =====
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                        .fillMaxWidth()
                ) {
                    Text("Worked muscles", style = MaterialTheme.typography.titleMedium)

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
                            val calendar = Calendar.getInstance().apply {
                                timeInMillis = selectedDateMillis
                                set(Calendar.HOUR_OF_DAY, selectedHour)
                                set(Calendar.MINUTE, selectedMinute)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }

                            vm.logAt(
                                calendar.timeInMillis,
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

            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
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
                        Text("History", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }

            items(entries, key = { it.id }) { entry ->
                HistoryCard(
                    entry = entry,
                    onDelete = {
                        vm.delete(entry)

                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Workout deleted",
                                actionLabel = "Undo",
                                duration = SnackbarDuration.Long,
                                withDismissAction = true
                            )

                            if (result == SnackbarResult.ActionPerformed) {
                                vm.undoDelete()
                            }
                        }
                    }
                )
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun HistoryCard(
    entry: GymEntryEntity,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 6.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = formatDateTime(entry.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    entry.bodyParts.joinToString(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Outlined.Delete, null)
            }
        }
    }
}

private fun formatDate(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))

private fun formatDateTime(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault()).format(Date(millis))

private fun formatTime12h(hour: Int, minute: Int): String =
    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }.time
    )
