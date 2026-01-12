package dev.swabodha.life.features.gym.data.repository

import dev.swabodha.life.core.database.DatabaseInitializer
import dev.swabodha.life.features.gym.data.entity.BodyPart
import dev.swabodha.life.features.gym.data.entity.GymEntryEntity
import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class GymRepository {

    private val dao = DatabaseInitializer.db.gymDao()

    fun observeEntries(): Flow<List<GymEntryEntity>> =
        dao.observeAll()

    suspend fun logWorkout(parts: List<BodyPart>) {
        val now = System.currentTimeMillis()
        val dayStart = Calendar.getInstance().apply {
            timeInMillis = now
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        dao.insert(
            GymEntryEntity(
                date = dayStart,
                bodyParts = parts,
                createdAt = now
            )
        )
    }
}
