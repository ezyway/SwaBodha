package dev.swabodha.life.features.water.reminder

import dev.swabodha.life.core.reminders.Reminder

object WaterReminder {

    const val ID = "water_hourly"

    val intervals = listOf(1, 15, 30, 45, 60, 75, 90, 105, 120)

    fun hourly() = Reminder(
        id = ID,
        title = "Drink Water",
        message = "Time to hydrate 💧",
        hour = 0,
        minute = 0
    )
}