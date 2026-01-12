package dev.swabodha.life.features.water.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.swabodha.life.core.reminders.ReminderScheduler
import dev.swabodha.life.features.water.reminder.WaterReminder
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterScreen(
    vm: WaterReminderViewModel = viewModel()
) {
    val context = LocalContext.current
    val config by vm.config.collectAsState()

    var isEnabled by rememberSaveable { mutableStateOf(false) }
    var interval by rememberSaveable { mutableStateOf(60) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(config) {
        config?.let {
            isEnabled = it.enabled
            interval = it.intervalMinutes
        }
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
                        imageVector = Icons.Outlined.WaterDrop,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Water reminder",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Get gentle reminders to stay hydrated.",
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

            // ===== Enable Switch =====
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Enable reminder", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Receive periodic hydration notifications",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { enabled ->
                            isEnabled = enabled

                            if (enabled) {
                                ReminderScheduler.schedulePeriodic(
                                    context,
                                    WaterReminder.ID,
                                    "Drink Water",
                                    "Time to hydrate 💧",
                                    interval
                                )
                            } else {
                                ReminderScheduler.cancel(context, WaterReminder.ID)
                            }

                            vm.save(isEnabled, interval)

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (enabled)
                                        "Water reminder enabled 💧"
                                    else
                                        "Water reminder disabled"
                                )
                            }
                        }
                    )
                }
            }

            // ===== Interval Grid (2 columns guaranteed) =====
            item {
                AnimatedVisibility(visible = isEnabled) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Reminder interval",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(Modifier.height(12.dp))

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.heightIn(max = 300.dp) // avoids infinite height
                        ) {
                            items(WaterReminder.intervals) { minutes ->
                                IntervalGridItem(
                                    minutes = minutes,
                                    selected = interval == minutes,
                                    onClick = {
                                        interval = minutes
                                        vm.save(isEnabled, interval)

                                        ReminderScheduler.schedulePeriodic(
                                            context,
                                            WaterReminder.ID,
                                            "Drink Water",
                                            "Time to hydrate 💧",
                                            interval
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Notifications repeat every $interval minutes.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IntervalGridItem(
    minutes: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text("$minutes min") },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    )
}
