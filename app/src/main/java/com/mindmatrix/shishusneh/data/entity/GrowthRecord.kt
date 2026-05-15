package com.mindmatrix.shishusneh.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "growth_records")
data class GrowthRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val recordDate: Long,
    val weightKg: Float,
    val heightCm: Float,
    val ageInWeeks: Int,
    val weightPercentile: Float? = null,
    val heightPercentile: Float? = null,
    val notes: String? = null
)
