package dev.swabodha.life.core.reminders.repository

import dev.swabodha.life.core.database.DatabaseInitializer
import dev.swabodha.life.core.reminders.data.ReminderConfigEntity
import kotlinx.coroutines.flow.Flow

class ReminderConfigRepository {

    private val dao = DatabaseInitializer.db.reminderConfigDao()

    fun observe(id: String): Flow<ReminderConfigEntity?> =
        dao.observe(id)

    suspend fun save(
        id: String,
        enabled: Boolean,
        intervalMinutes: Int
    ) {
        dao.upsert(
            ReminderConfigEntity(
                id = id,
                enabled = enabled,
                intervalMinutes = intervalMinutes
            )
        )
    }
}
