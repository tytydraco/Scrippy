package com.draco.scrippy.database

import androidx.room.*

@Dao
interface ScriptDao {
    @Query("SELECT * FROM Script")
    fun getAll(): Array<Script>

    @Insert
    fun insert(vararg script: Script)

    @Update
    fun update(vararg script: Script)

    @Delete
    fun delete(script: Script)
}