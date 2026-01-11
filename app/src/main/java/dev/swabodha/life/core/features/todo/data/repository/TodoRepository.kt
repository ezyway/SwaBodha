package dev.swabodha.life.core.features.todo.data.repository

import android.content.Context
import dev.swabodha.life.core.database.DatabaseInitializer
import dev.swabodha.life.core.reminders.ReminderScheduler
import dev.swabodha.life.core.features.todo.data.entity.TodoEntity

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

}
