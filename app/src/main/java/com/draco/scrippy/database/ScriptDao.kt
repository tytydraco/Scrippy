package com.draco.scrippy.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScriptDao {
    @Query("SELECT * FROM Script")
    fun getAll(): Array<Script>

    @Insert
    fun insert(vararg script: Script)

    @Delete
    fun delete(script: Script)
}