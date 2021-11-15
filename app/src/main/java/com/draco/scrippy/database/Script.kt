package com.draco.scrippy.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Script(
    var name: String,
    var contents: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}