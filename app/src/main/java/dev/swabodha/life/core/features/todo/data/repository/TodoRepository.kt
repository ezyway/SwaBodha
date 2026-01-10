package dev.swabodha.life.core.features.todo.data.repository

import dev.swabodha.life.core.database.DatabaseInitializer
import dev.swabodha.life.core.reminders.Reminder
import dev.swabodha.life.core.reminders.ReminderScheduler
import dev.swabodha.life.core.features.todo.data.entity.TodoEntity
import kotlinx.coroutines.flow.Flow

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
}
