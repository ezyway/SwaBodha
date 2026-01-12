package dev.swabodha.life.core.features.todo.data.repository

import android.content.Context
import dev.swabodha.life.core.database.DatabaseInitializer
import dev.swabodha.life.core.reminders.ReminderScheduler
import dev.swabodha.life.core.features.todo.data.entity.TodoEntity
import dev.swabodha.life.core.reminders.Reminder

class TodoRepository {

    private val dao = DatabaseInitializer.db.todoDao()

    fun observeTodos() = dao.observeAll()

    suspend fun addTodo(
        text: String,
        reminderAt: Long?
    ): TodoEntity {
        val todo = TodoEntity(
            text = text,
            reminderAt = reminderAt,
            createdAt = System.currentTimeMillis()
        )

        dao.insert(todo)
        return todo
    }

    suspend fun delete(todo: TodoEntity, context: Context) {
        dao.deleteById(todo.id)

        // Cancel reminder if it existed
        if (todo.reminderAt != null) {
            ReminderScheduler.cancel(
                context,
                "todo_${todo.id}"
            )
        }
    }

    suspend fun restore(todo: TodoEntity, context: Context) {
        dao.insert(todo)

        if (todo.reminderAt != null) {
            ReminderScheduler.scheduleAt(
                context = context,
                reminder = Reminder(
                    id = "todo_${todo.id}",
                    title = "Todo",
                    message = todo.text,
                    hour = 0,
                    minute = 0
                ),
                triggerAtMillis = todo.reminderAt
            )
        }
    }

    suspend fun toggleCompleted(
        context: Context,
        todo: TodoEntity
    ) {
        val newCompleted = !todo.completed
        dao.setCompleted(todo.id, newCompleted)

        // If marking completed → cancel reminder
        if (newCompleted && todo.reminderAt != null) {
            ReminderScheduler.cancel(
                context,
                "todo_${todo.id}"
            )
        }
    }

    suspend fun update(context: Context, todo: TodoEntity) {
        dao.update(todo)

        // Cancel old reminder
        ReminderScheduler.cancel(
            context,
            "todo_${todo.id}"
        )

        // Reschedule if needed
        if (todo.reminderAt != null && !todo.completed) {
            ReminderScheduler.scheduleAt(
                context = context,
                reminder = Reminder(
                    id = "todo_${todo.id}",
                    title = "Todo",
                    message = todo.text,
                    hour = 0,
                    minute = 0
                ),
                triggerAtMillis = todo.reminderAt
            )
        }
    }

}
