package com.mindmatrix.shishusneh.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "baby_profile")
data class BabyProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dateOfBirth: Long,
    val gender: String,
    val birthWeightKg: Float,
    val birthHeightCm: Float,
    val hospitalName: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
