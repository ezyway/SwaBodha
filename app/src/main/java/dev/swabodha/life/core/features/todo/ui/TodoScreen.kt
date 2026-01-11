package dev.swabodha.life.core.features.todo.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.swabodha.life.core.features.todo.data.entity.TodoEntity
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

    var editingTodo by remember { mutableStateOf<TodoEntity?>(null) }
    var text by remember { mutableStateOf("") }
    var reminderAt by remember { mutableStateOf<Long?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val dateFormatter = remember {
        SimpleDateFormat("EEE, dd MMM • hh:mm a", Locale.getDefault())
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
                .padding(padding)
        ) {

            /* ===== Header ===== */
            item {
                Column(Modifier.padding(24.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.ListAlt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = "Todos",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(Modifier.height(4.dp))

                    Text(
                        text = "Things you don’t want to forget.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item { Divider() }

            /* ===== Add / Edit Section ===== */
            item {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = {
                            Text(if (editingTodo == null) "Todo" else "Edit todo")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = {
                                pickDateTime { reminderAt = it }
                            }
                        ) {
                            Icon(Icons.Outlined.Alarm, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Set reminder")
                        }

                        Spacer(Modifier.width(12.dp))

                        AnimatedVisibility(visible = reminderAt != null) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = reminderAt?.let {
                                        dateFormatter.format(Date(it))
                                    } ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                IconButton(onClick = { reminderAt = null }) {
                                    Icon(
                                        imageVector = Icons.Outlined.Close,
                                        contentDescription = "Remove reminder"
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (text.isBlank()) return@Button

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

                            text = ""
                            reminderAt = null
                            editingTodo = null
                        }
                    ) {
                        Text(if (editingTodo == null) "Add todo" else "Save changes")
                    }
                }
            }

            item { Divider() }

            /* ===== Todo List ===== */
            items(todos) { todo ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            editingTodo = todo
                            text = todo.text
                            reminderAt = todo.reminderAt
                        },
                    shape = MaterialTheme.shapes.medium
                )
                {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        // ✔ Completed checkbox + content
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = todo.completed,
                                onCheckedChange = {
                                    vm.toggleCompleted(context, todo)
                                }
                            )

                            Spacer(Modifier.width(8.dp))

                            Column {
                                Text(
                                    text = todo.text,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = if (todo.completed)
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )

                                todo.reminderAt?.let {
                                    if (!todo.completed) {
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            text = dateFormatter.format(Date(it)),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        // 🗑 Delete with Undo
                        IconButton(
                            onClick = {
                                vm.delete(context, todo)

                                scope.launch {
                                    val result = snackbarHostState.showSnackbar(
                                        message = "Todo deleted",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                    )

                                    if (result == SnackbarResult.ActionPerformed) {
                                        vm.restore(context, todo)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Remove todo",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}
