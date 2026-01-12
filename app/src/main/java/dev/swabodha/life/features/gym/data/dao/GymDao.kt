package dev.swabodha.life.features.gym.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.swabodha.life.features.gym.data.entity.GymEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GymDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: GymEntryEntity)

    @Query("SELECT * FROM gym_entries ORDER BY date DESC")
    fun observeAll(): Flow<List<GymEntryEntity>>
}
