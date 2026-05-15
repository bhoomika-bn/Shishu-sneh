package com.mindmatrix.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mindmatrix.shishusneh.data.entity.Milestone
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM milestones WHERE babyId = :babyId ORDER BY weekNumber")
    fun observeForBaby(babyId: Long): Flow<List<Milestone>>

    @Query("SELECT COUNT(*) FROM milestones WHERE babyId = :babyId")
    suspend fun countForBaby(babyId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(milestones: List<Milestone>)

    @Update
    suspend fun update(milestone: Milestone)
}
