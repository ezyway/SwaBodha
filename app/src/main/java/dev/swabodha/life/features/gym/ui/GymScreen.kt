package dev.swabodha.life.features.gym.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.swabodha.life.features.gym.data.entity.BodyPart
import dev.swabodha.life.features.gym.data.entity.GymEntryEntity
import dev.swabodha.life.ui.components.FeatureHeader
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymScreen(
    vm: GymViewModel = viewModel()
) {
    val entries by vm.entries.collectAsState()

    var editingEntry by remember { mutableStateOf<GymEntryEntity?>(null) }
    val selected = remember { mutableStateMapOf<BodyPart, Boolean>() }

    val now = remember { Calendar.getInstance() }
    var selectedDateMillis by remember { mutableStateOf(now.timeInMillis) }
    var selectedHour by remember { mutableStateOf(now.get(Calendar.HOUR_OF_DAY)) }
    var selectedMinute by remember { mutableStateOf(now.get(Calendar.MINUTE)) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(true) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun clearEdit() {
        editingEntry = null
        selected.clear()
        selectedDateMillis = now.timeInMillis
        selectedHour = now.get(Calendar.HOUR_OF_DAY)
        selectedMinute = now.get(Calendar.MINUTE)
    }

    fun startEdit(entry: GymEntryEntity) {
        editingEntry = entry
        selected.clear()
        entry.bodyParts.forEach { selected[it] = true }

        val cal = Calendar.getInstance().apply { timeInMillis = entry.date }
        selectedDateMillis = entry.date
        selectedHour = cal.get(Calendar.HOUR_OF_DAY)
        selectedMinute = cal.get(Calendar.MINUTE)
        expanded = true
    }

    /* ---------- Pickers ---------- */

    if (showDatePicker) {
        val state = rememberDatePickerState(selectedDateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedDateMillis = state.selectedDateMillis ?: selectedDateMillis
                    showDatePicker = false
                }) { Text("OK") }
            }
        ) { DatePicker(state = state) }
    }

    if (showTimePicker) {
        val state = rememberTimePickerState(selectedHour, selectedMinute, false)
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedHour = state.hour
                    selectedMinute = state.minute
                    showTimePicker = false
                }) { Text("OK") }
            },
            text = { TimePicker(state) }
        )
    }

    /* ---------- UI ---------- */

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            /* ===== Header ===== */
            item {
                FeatureHeader(
                    title = "Gym log",
                    description = "Track the muscle groups you train.",
                    icon = Icons.Outlined.FitnessCenter
                )
            }

            /* ===== Input Section (FULL BACKGROUND) ===== */
            item {
                InputSection(
                    expanded = expanded,
                    editing = editingEntry != null,
                    onToggle = { expanded = !expanded },
                    onCancel = { clearEdit() }
                ) {
                    /* ---- Inner Card ---- */
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text("Date & Time", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))

                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { showDatePicker = true }
                            ) {
                                Icon(Icons.Outlined.CalendarToday, null)
                                Spacer(Modifier.width(8.dp))
                                Text(formatDate(selectedDateMillis))
                            }

                            Spacer(Modifier.height(8.dp))

                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { showTimePicker = true }
                            ) {
                                Icon(Icons.Outlined.Schedule, null)
                                Spacer(Modifier.width(8.dp))
                                Text(formatTime12h(selectedHour, selectedMinute))
                            }

                            Spacer(Modifier.height(16.dp))
                            Text("Worked muscles", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))

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
                                                part.name.lowercase()
                                                    .replaceFirstChar { it.uppercase() }
                                            )
                                        }
                                    )
                                }
                            }

                            Spacer(Modifier.height(16.dp))

                            FilledTonalButton(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = selected.any { it.value },
                                onClick = {
                                    val cal = Calendar.getInstance().apply {
                                        timeInMillis = selectedDateMillis
                                        set(Calendar.HOUR_OF_DAY, selectedHour)
                                        set(Calendar.MINUTE, selectedMinute)
                                        clear(Calendar.SECOND)
                                        clear(Calendar.MILLISECOND)
                                    }

                                    vm.save(
                                        GymEntryEntity(
                                            id = editingEntry?.id
                                                ?: UUID.randomUUID().toString(),
                                            date = cal.timeInMillis,
                                            bodyParts = selected.filter { it.value }.keys.toList(),
                                            createdAt = editingEntry?.createdAt
                                                ?: System.currentTimeMillis()
                                        )
                                    )
                                    clearEdit()
                                }
                            ) {
                                Text(if (editingEntry == null) "Log workout" else "Save changes")
                            }
                        }
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        thickness = 1.dp
                    )
                }
            }

            /* ===== History ===== */
            item {
                AnimatedVisibility(entries.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                            .fillMaxWidth()
                    ) { Text("History", style = MaterialTheme.typography.titleMedium) }
                }
            }

            /* ===== History ===== */
            items(entries, key = { it.id }) { entry ->
                HistoryCard(
                    entry = entry,
                    onClick = { startEdit(entry) },
                    onDelete = {
                        vm.delete(entry)
                        scope.launch {
                            val res = snackbarHostState.showSnackbar(
                                message = "Workout deleted",
                                actionLabel = "Undo",
                                withDismissAction = true,
                                duration = SnackbarDuration.Long
                            )
                            if (res == SnackbarResult.ActionPerformed) vm.save(entry)
                        }
                    }
                )
            }
        }
    }
}

/* ===== Input Section Container ===== */

@Composable
private fun InputSection(
    expanded: Boolean,
    editing: Boolean,
    onToggle: () -> Unit,
    onCancel: () -> Unit,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(if (expanded) 180f else 0f, label = "chevron")

    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = LocalIndication.current,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = onToggle
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.AddCircleOutline, null)
                Spacer(Modifier.width(12.dp))
                Text(
                    if (editing) "Edit workout" else "Log workout",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.weight(1f))
                Icon(Icons.Outlined.ExpandMore, null, Modifier.rotate(rotation))
            }

            AnimatedVisibility(editing) {
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Edit, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Editing workout")
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onCancel) { Text("Cancel") }
                }
            }

            AnimatedVisibility(expanded) {
                Column(Modifier.padding(top = 12.dp)) {
                    content()
                }
            }
        }
    }
}

/* ===== History Card & Utils ===== */

@Composable
private fun HistoryCard(
    entry: GymEntryEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(formatDateTime(entry.date), style = MaterialTheme.typography.bodySmall)
                Text(entry.bodyParts.joinToString())
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Outlined.Delete, null)
            }
        }
    }
}

private fun formatDate(millis: Long) =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))

private fun formatDateTime(millis: Long) =
    SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault()).format(Date(millis))

private fun formatTime12h(hour: Int, minute: Int) =
    SimpleDateFormat("hh:mm a", Locale.getDefault()).format(
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }.time
    )
