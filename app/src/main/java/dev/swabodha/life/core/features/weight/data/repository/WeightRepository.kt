package dev.swabodha.life.features.weight.data.repository

import dev.swabodha.life.core.database.DatabaseInitializer
import dev.swabodha.life.features.weight.data.entity.WeightEntryEntity
import kotlinx.coroutines.flow.Flow

class WeightRepository {

    private val dao = DatabaseInitializer.db.weightDao()

    fun observeWeights(): Flow<List<WeightEntryEntity>> {
        return dao.observeAll()
    }

    suspend fun addWeight(weightKg: Float) {
        val now = System.currentTimeMillis()
        dao.insert(
            WeightEntryEntity(
                weightKg = weightKg,
                timestamp = now,
                createdAt = now,
                updatedAt = now
            )
        )
    }
}
