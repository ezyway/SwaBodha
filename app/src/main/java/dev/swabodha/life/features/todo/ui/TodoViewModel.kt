package dev.swabodha.life.features.todo.ui


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.swabodha.life.features.todo.data.entity.TodoEntity
import dev.swabodha.life.features.todo.data.repository.TodoRepository
import dev.swabodha.life.core.reminders.Reminder
import dev.swabodha.life.core.reminders.ReminderScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(
    private val repo: TodoRepository = TodoRepository()
) : ViewModel() {

    val todos =
        repo.observeTodos()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                emptyList()
            )

    fun add(
        context: Context,
        text: String,
        reminderAt: Long?
    ) {
        viewModelScope.launch {
            val todo = repo.addTodo(text, reminderAt)

            if (reminderAt != null) {
                ReminderScheduler.scheduleAt(
                    context = context,
                    reminder = Reminder(
                        id = "todo_${todo.id}",
                        title = "Todo",
                        message = text,
                        hour = 0,
                        minute = 0
                    ),
                    triggerAtMillis = reminderAt
                )

            }
        }
    }

    fun delete(context: Context, todo: TodoEntity) {
        viewModelScope.launch {
            repo.delete(todo, context)
        }
    }

    fun restore(context: Context, todo: TodoEntity) {
        viewModelScope.launch {
            repo.restore(todo, context)
        }
    }

    fun toggleCompleted(context: Context, todo: TodoEntity) {
        viewModelScope.launch {
            repo.toggleCompleted(context, todo)
        }
    }

    fun update(context: Context, todo: TodoEntity) {
        viewModelScope.launch {
            repo.update(context, todo)
        }
    }

}
