package dev.swabodha.life.features.gym.data.repository

import dev.swabodha.life.core.database.DatabaseInitializer
import dev.swabodha.life.features.gym.data.entity.BodyPart
import dev.swabodha.life.features.gym.data.entity.GymEntryEntity
import kotlinx.coroutines.flow.Flow

class GymRepository {

    private val dao = DatabaseInitializer.db.gymDao()

    fun observeEntries(): Flow<List<GymEntryEntity>> =
        dao.observeAll()

    suspend fun logWorkoutAt(timeMillis: Long, parts: List<BodyPart>) {
        dao.insert(
            GymEntryEntity(
                date = timeMillis,
                bodyParts = parts,
                createdAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun delete(entry: GymEntryEntity) {
        dao.deleteById(entry.id)
    }

    suspend fun insert(entry: GymEntryEntity) {
        dao.insert(entry)
    }
}
