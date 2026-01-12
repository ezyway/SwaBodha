package dev.swabodha.life.features.todo.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val text: String,
    val reminderAt: Long?, // null = no reminder
    val completed: Boolean = false,
    val createdAt: Long
)
