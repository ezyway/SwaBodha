package dev.swabodha.life.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meta")
data class MetaEntity(
    @PrimaryKey val id: String,
    val value: String
)
