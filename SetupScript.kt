package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "setup_scripts")
data class SetupScript(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // "AUDIO", "GPU", "STYLUS", "DEV", "DESKTOP"
    val description: String,
    val bashScript: String,
    val isRecommendedForPad5: Boolean = true,
    val isExecuted: Boolean = false
)
