package com.mindmatrix.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mindmatrix.shishusneh.data.entity.FeedingRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedingRecordDao {

    @Insert
    suspend fun insert(record: FeedingRecord)

    @Query("SELECT * FROM feeding_records WHERE babyId = :babyId ORDER BY startTime DESC")
    fun observeForBaby(babyId: Long): Flow<List<FeedingRecord>>
}