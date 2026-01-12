package dev.swabodha.life.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import dev.swabodha.life.core.database.converters.BodyPartConverters
import dev.swabodha.life.core.database.converters.SmokingSizeConverter
import dev.swabodha.life.core.reminders.data.ReminderConfigEntity
import dev.swabodha.life.core.reminders.data.ReminderConfigDao

import dev.swabodha.life.features.weight.data.entity.WeightEntryEntity
import dev.swabodha.life.features.weight.data.dao.WeightDao

import dev.swabodha.life.features.gym.data.entity.GymEntryEntity
import dev.swabodha.life.features.gym.data.dao.GymDao
import dev.swabodha.life.features.smoking.data.dao.SmokingDao
import dev.swabodha.life.features.smoking.data.entity.SmokingEntryEntity

import dev.swabodha.life.features.todo.data.entity.TodoEntity
import dev.swabodha.life.features.todo.data.dao.TodoDao

@TypeConverters(
    BodyPartConverters::class,
    SmokingSizeConverter::class
)

@Database(
    entities = [
        MetaEntity::class,
        WeightEntryEntity::class,
        ReminderConfigEntity::class,
        TodoEntity::class,
        GymEntryEntity::class,
        SmokingEntryEntity::class
    ],

    version = 8,
    exportSchema = false
)

abstract class SwaBodhaDatabase : RoomDatabase() {
    abstract fun metaDao(): MetaDao
    abstract fun weightDao(): WeightDao
    abstract fun reminderConfigDao(): ReminderConfigDao
    abstract fun todoDao(): TodoDao
    abstract fun gymDao(): GymDao
    abstract fun smokingDao(): SmokingDao


}
