package com.mindmatrix.shishusneh.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaccinations")
data class Vaccination(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val vaccineName: String,
    val diseaseInfo: String,
    val scheduledDate: Long,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null,
    val reminderEnabled: Boolean = true,
    val notes: String? = null
)
