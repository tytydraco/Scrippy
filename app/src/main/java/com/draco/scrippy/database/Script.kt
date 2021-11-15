package com.draco.scrippy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Script(
    @PrimaryKey val name: String,
    var contents: String
)