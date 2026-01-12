package dev.swabodha.life.features.weight.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.swabodha.life.features.weight.data.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: WeightEntryEntity)

    @Delete
    suspend fun delete(entry: WeightEntryEntity)

    @Query("SELECT * FROM weight_entries ORDER BY timestamp DESC")
    fun observeAll(): Flow<List<WeightEntryEntity>>
}
