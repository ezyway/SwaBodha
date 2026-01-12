package dev.swabodha.life.features.smoking.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.swabodha.life.features.smoking.data.entity.SmokingEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SmokingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: SmokingEntryEntity)

    @Query("SELECT * FROM smoking_entries ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<SmokingEntryEntity>>
}
