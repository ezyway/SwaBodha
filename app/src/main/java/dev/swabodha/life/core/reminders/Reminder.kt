package dev.swabodha.life.core.reminders

data class Reminder(
    val id: String,
    val title: String,
    val message: String,
    val hour: Int,
    val minute: Int
)
