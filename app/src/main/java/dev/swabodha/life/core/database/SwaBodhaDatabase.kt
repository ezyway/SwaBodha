package dev.swabodha.life.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

import dev.swabodha.life.features.weight.data.entity.WeightEntryEntity
import dev.swabodha.life.features.weight.data.dao.WeightDao

import dev.swabodha.life.core.reminders.data.ReminderConfigEntity
import dev.swabodha.life.core.reminders.data.ReminderConfigDao


@Database(
    entities = [
        MetaEntity::class,
        WeightEntryEntity::class,
        ReminderConfigEntity::class
    ],

    version = 2,
    exportSchema = false
)

abstract class SwaBodhaDatabase : RoomDatabase() {
    abstract fun metaDao(): MetaDao
    abstract fun weightDao(): WeightDao
    abstract fun reminderConfigDao(): ReminderConfigDao

}
