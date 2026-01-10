package dev.swabodha.life.features.weight.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "weight_entries")
data class WeightEntryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val weightKg: Float,
    val timestamp: Long,
    val createdAt: Long,
    val updatedAt: Long
)
