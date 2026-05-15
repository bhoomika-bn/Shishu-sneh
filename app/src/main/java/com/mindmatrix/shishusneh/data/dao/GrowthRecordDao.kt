package com.mindmatrix.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mindmatrix.shishusneh.data.entity.GrowthRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface GrowthRecordDao {
    @Query("SELECT * FROM growth_records WHERE babyId = :babyId ORDER BY recordDate")
    fun observeForBaby(babyId: Long): Flow<List<GrowthRecord>>

    @Insert
    suspend fun insert(record: GrowthRecord): Long
}
