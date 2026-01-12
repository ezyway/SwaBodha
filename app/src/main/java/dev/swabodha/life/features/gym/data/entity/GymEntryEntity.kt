package dev.swabodha.life.features.gym.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "gym_entries")
data class GymEntryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val date: Long, // start of day millis
    val bodyParts: List<BodyPart>,
    val createdAt: Long
)
