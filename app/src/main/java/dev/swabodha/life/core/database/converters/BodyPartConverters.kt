package dev.swabodha.life.core.database.converters

import androidx.room.TypeConverter
import dev.swabodha.life.features.gym.data.entity.BodyPart

class BodyPartConverters {

    @TypeConverter
    fun fromList(parts: List<BodyPart>): String =
        parts.joinToString(",") { it.name }

    @TypeConverter
    fun toList(value: String): List<BodyPart> =
        if (value.isBlank()) emptyList()
        else value.split(",").map { BodyPart.valueOf(it) }
}
