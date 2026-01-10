package dev.swabodha.life.core.reminders.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderConfigDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(config: ReminderConfigEntity)

    @Query("SELECT * FROM reminder_configs WHERE id = :id")
    fun observe(id: String): Flow<ReminderConfigEntity?>
}
