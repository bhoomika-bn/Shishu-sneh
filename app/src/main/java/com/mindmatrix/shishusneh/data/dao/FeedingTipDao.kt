package com.mindmatrix.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mindmatrix.shishusneh.data.entity.FeedingTip

@Dao
interface FeedingTipDao {
    @Query("SELECT * FROM feeding_tips WHERE babyId = :babyId AND dateEpochDay = :dateEpochDay LIMIT 1")
    suspend fun getForDate(babyId: Long, dateEpochDay: Long): FeedingTip?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tip: FeedingTip)
}
