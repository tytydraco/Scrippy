package com.draco.scrippy.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Script::class], version = 1)
abstract class ScriptDatabase : RoomDatabase() {
    companion object {
        const val NAME = "database"
    }

    abstract fun scriptDao(): ScriptDao
}