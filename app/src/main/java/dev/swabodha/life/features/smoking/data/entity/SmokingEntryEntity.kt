package dev.swabodha.life.features.smoking.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "smoking_entries")
data class SmokingEntryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val count: Int,
    val size: SmokingSize,
    val isMenthol: Boolean,
    val timestamp: Long,
    val createdAt: Long
)
