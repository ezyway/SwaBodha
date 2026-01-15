package dev.swabodha.life.features.smoking.data.repository

import dev.swabodha.life.core.database.DatabaseInitializer
import dev.swabodha.life.features.smoking.data.entity.SmokingEntryEntity
import dev.swabodha.life.features.smoking.data.entity.SmokingSize
import kotlinx.coroutines.flow.Flow

class SmokingRepository {

    private val dao = DatabaseInitializer.db.smokingDao()

    fun observeEntries(): Flow<List<SmokingEntryEntity>> =
        dao.observeAll()

    suspend fun log(
        count: Int,
        size: SmokingSize,
        isMenthol: Boolean
    ) {
        val now = System.currentTimeMillis()
        dao.insert(
            SmokingEntryEntity(
                count = count,
                size = size,
                isMenthol = isMenthol,
                timestamp = now,
                createdAt = now
            )
        )
    }

    suspend fun delete(entry: SmokingEntryEntity) {
        dao.delete(entry)
    }

    suspend fun insert(entry: SmokingEntryEntity) {
        dao.insert(entry)
    }
}
