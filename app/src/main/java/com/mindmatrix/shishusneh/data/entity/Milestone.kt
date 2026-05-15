package com.mindmatrix.shishusneh.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milestones")
data class Milestone(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val weekNumber: Int,
    val title: String,
    val description: String,
    val status: MilestoneStatus = MilestoneStatus.NotChecked,
    val checkedDate: Long? = null
)

enum class MilestoneStatus {
    NotChecked,
    Achieved,
    NotYet,
    Skipped
}
