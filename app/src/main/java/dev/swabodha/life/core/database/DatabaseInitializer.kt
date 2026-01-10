package dev.swabodha.life.core.database

import android.content.Context
import androidx.room.Room

object DatabaseInitializer {

    lateinit var db: SwaBodhaDatabase
        private set

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context,
            SwaBodhaDatabase::class.java,
            "swabodha.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
