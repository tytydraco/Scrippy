package com.draco.scrippy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Script(
    @PrimaryKey val name: String,
    val contents: String
)