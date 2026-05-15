package com.mindmatrix.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mindmatrix.shishusneh.data.entity.BabyProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyProfileDao {
    @Query("SELECT * FROM baby_profile ORDER BY id LIMIT 1")
    fun observeProfile(): Flow<BabyProfile?>

    @Query("SELECT * FROM baby_profile ORDER BY id LIMIT 1")
    suspend fun getProfile(): BabyProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: BabyProfile): Long
}
