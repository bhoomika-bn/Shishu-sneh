package com.mindmatrix.shishusneh.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feeding_records")
data class FeedingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val babyId: Long,
    val type: String, // Breastfeeding / Formula / Solids / Water
    val startTime: Long,
    val durationMinutes: Int,
    val amount: String? = null
)