package dev.swabodha.life.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meta: MetaEntity)

    @Query("SELECT * FROM meta WHERE id = :id")
    suspend fun get(id: String): MetaEntity?
}
