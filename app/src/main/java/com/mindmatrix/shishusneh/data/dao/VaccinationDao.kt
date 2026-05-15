package com.mindmatrix.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mindmatrix.shishusneh.data.entity.Vaccination
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccinationDao {
    @Query("SELECT * FROM vaccinations WHERE babyId = :babyId ORDER BY scheduledDate")
    fun observeForBaby(babyId: Long): Flow<List<Vaccination>>

    @Query("SELECT COUNT(*) FROM vaccinations WHERE babyId = :babyId")
    suspend fun countForBaby(babyId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vaccinations: List<Vaccination>)

    @Update
    suspend fun update(vaccination: Vaccination)
}
