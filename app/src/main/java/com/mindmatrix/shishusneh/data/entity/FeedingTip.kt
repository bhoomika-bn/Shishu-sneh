package com.mindmatrix.shishusneh.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeding_tips")
data class FeedingTip(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val dateEpochDay: Long,
    val content: String,
    val babyAgeWeeks: Int
)
