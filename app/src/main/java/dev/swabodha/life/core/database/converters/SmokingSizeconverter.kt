package dev.swabodha.life.core.database.converters

import androidx.room.TypeConverter
import dev.swabodha.life.features.smoking.data.entity.SmokingSize

class SmokingSizeConverter {

    @TypeConverter
    fun fromSize(size: SmokingSize): String = size.name

    @TypeConverter
    fun toSize(value: String): SmokingSize =
        SmokingSize.valueOf(value)
}
