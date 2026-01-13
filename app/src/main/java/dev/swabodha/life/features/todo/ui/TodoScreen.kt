package dev.swabodha.life.features.todo.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.swabodha.life.features.todo.data.entity.TodoEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    vm: TodoViewModel = viewModel()
) {
    val context = LocalContext.current
    val todos by vm.todos.collectAsState()

    val activeTodos = todos.filter { !it.completed }
    val completedTodos = todos.filter { it.completed }

    var editingTodo by remember { mutableStateOf<TodoEntity?>(null) }
    var text by remember { mutableStateOf("") }
    var reminderAt by remember { mutableStateOf<Long?>(null) }

    var addExpanded by remember { mutableStateOf(true) }
    var activeExpanded by remember { mutableStateOf(false) }
    var completedExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val dateFormatter = remember {
        SimpleDateFormat("EEE, dd MMM • hh:mm a", Locale.getDefault())
    }

    fun clearEdit() {
        editingTodo = null
        text = ""
        reminderAt = null
    }

    fun startEdit(todo: TodoEntity) {
        editingTodo = todo
        text = todo.text
        reminderAt = todo.reminderAt
        addExpanded = true
    }

    fun pickDateTime(onPicked: (Long) -> Unit) {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, y, m, d ->
                TimePickerDialog(
                    context,
                    { _, h, min ->
                        cal.set(y, m, d, h, min)
                        onPicked(cal.timeInMillis)
                    },
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    false
                ).show()
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            /* ===== Header ===== */
            item {
                Column(Modifier.padding(24.dp)) {
                    Icon(
                        Icons.Outlined.ListAlt,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Todos", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        "Things you don’t want to forget.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        thickness = 1.dp
                    )
                }
            }

            /* ===== Add / Edit (FULL BACKGROUND) ===== */
            item {
                AddTodoSection(
                    expanded = addExpanded,
                    editing = editingTodo != null,
                    onToggle = { addExpanded = !addExpanded },
                    onCancel = { clearEdit() }
                ) {

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(Modifier.padding(16.dp)) {

                            Text("Todo", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))

                            OutlinedTextField(
                                value = text,
                                onValueChange = { text = it },
                                placeholder = {
                                    Text(
                                        if (editingTodo == null)
                                            "New todo"
                                        else
                                            "Edit todo"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(Modifier.height(16.dp))

                            Text("Reminder", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(8.dp))

                            OutlinedButton(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { pickDateTime { reminderAt = it } }
                            ) {
                                Icon(Icons.Outlined.Alarm, null)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    reminderAt?.let {
                                        dateFormatter.format(Date(it))
                                    } ?: "Set reminder"
                                )
                            }

                            Spacer(Modifier.height(16.dp))

                            FilledTonalButton(
                                modifier = Modifier.fillMaxWidth(),
                                enabled = text.isNotBlank(),
                                onClick = {
                                    if (editingTodo == null) {
                                        vm.add(context, text, reminderAt)
                                    } else {
                                        vm.update(
                                            context,
                                            editingTodo!!.copy(
                                                text = text,
                                                reminderAt = reminderAt
                                            )
                                        )
                                    }
                                    clearEdit()
                                }
                            ) {
                                Text(
                                    if (editingTodo == null)
                                        "Add Todo"
                                    else
                                        "Save Changes"
                                )
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

            /* ===== Active ===== */
            item {
                SectionHeader(
                    title = "Active",
                    icon = Icons.Outlined.RadioButtonUnchecked,
                    expanded = activeExpanded,
                    count = activeTodos.size,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onToggle = { activeExpanded = !activeExpanded }
                )
            }

            if (activeExpanded) {
                items(activeTodos) { todo ->
                    TodoItem(
                        todo = todo,
                        onEdit = { startEdit(todo) },
                        onToggle = { vm.toggleCompleted(context, todo) },
                        onDelete = {
                            vm.delete(context, todo)
                            scope.launch {
                                val res = snackbarHostState.showSnackbar(
                                    "Todo deleted",
                                    "Undo"
                                )
                                if (res == SnackbarResult.ActionPerformed) {
                                    vm.restore(context, todo)
                                }
                            }
                        },
                        dateFormatter = dateFormatter
                    )
                }
            }

            /* ===== Completed ===== */
            item {
                SectionHeader(
                    title = "Completed",
                    icon = Icons.Outlined.CheckCircleOutline,
                    expanded = completedExpanded,
                    count = completedTodos.size,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    onToggle = { completedExpanded = !completedExpanded }
                )
            }

            if (completedExpanded) {
                items(completedTodos) { todo ->
                    TodoItem(
                        todo = todo,
                        onEdit = { startEdit(todo) },
                        onToggle = { vm.toggleCompleted(context, todo) },
                        onDelete = { vm.delete(context, todo) },
                        dateFormatter = dateFormatter
                    )
                }
            }
        }
    }
}

/* ===== Add Todo Section Container ===== */

@Composable
private fun AddTodoSection(
    expanded: Boolean,
    editing: Boolean,
    onToggle: () -> Unit,
    onCancel: () -> Unit,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        if (expanded) 180f else 0f,
        label = "chevron"
    )

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
                    if (editing) "Edit Todo" else "Add Todo",
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
                    Text("Editing todo")
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
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

/* ===== Section Header & Item ===== */

@Composable
private fun SectionHeader(
    title: String,
    icon: ImageVector,
    expanded: Boolean,
    count: Int? = null,
    containerColor: Color,
    onToggle: () -> Unit
) {
    val rotation by animateFloatAsState(
        if (expanded) 180f else 0f,
        label = "chevron"
    )

    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onToggle
            ),
        shape = MaterialTheme.shapes.large,
        color = containerColor,
        tonalElevation = if (expanded) 3.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.SemiBold)
            count?.let {
                Spacer(Modifier.width(8.dp))
                AssistChip(onClick = {}, enabled = false, label = { Text(it.toString()) })
            }
            Spacer(Modifier.weight(1f))
            Icon(Icons.Outlined.ExpandMore, null, Modifier.rotate(rotation))
        }
    }
}

@Composable
private fun TodoItem(
    todo: TodoEntity,
    onEdit: () -> Unit,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    dateFormatter: SimpleDateFormat
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .clickable(
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onEdit
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.completed)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(todo.completed, onCheckedChange = { onToggle() })
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(todo.text, fontWeight = FontWeight.Medium)
                todo.reminderAt?.takeIf { !todo.completed }?.let {
                    Text(
                        dateFormatter.format(Date(it)),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Outlined.Delete, null, tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}
