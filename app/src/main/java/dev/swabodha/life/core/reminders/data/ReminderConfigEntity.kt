package dev.swabodha.life.core.reminders.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_configs")
data class ReminderConfigEntity(
    @PrimaryKey val id: String,
    val enabled: Boolean,
    val intervalMinutes: Int
)
