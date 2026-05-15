package com.mindmatrix.shishusneh.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mindmatrix.shishusneh.data.dao.BabyProfileDao
import com.mindmatrix.shishusneh.data.dao.FeedingTipDao
import com.mindmatrix.shishusneh.data.dao.GrowthRecordDao
import com.mindmatrix.shishusneh.data.dao.MilestoneDao
import com.mindmatrix.shishusneh.data.dao.VaccinationDao
import com.mindmatrix.shishusneh.data.entity.BabyProfile
import com.mindmatrix.shishusneh.data.entity.FeedingTip
import com.mindmatrix.shishusneh.data.entity.GrowthRecord
import com.mindmatrix.shishusneh.data.entity.Milestone
import com.mindmatrix.shishusneh.data.entity.Vaccination
import com.mindmatrix.shishusneh.data.dao.FeedingRecordDao
import com.mindmatrix.shishusneh.data.entity.FeedingRecord

@Database(
    entities = [BabyProfile::class, GrowthRecord::class, Vaccination::class, Milestone::class, FeedingTip::class, FeedingRecord::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun babyProfileDao(): BabyProfileDao
    abstract fun growthRecordDao(): GrowthRecordDao
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun feedingTipDao(): FeedingTipDao

    abstract fun feedingRecordDao(): FeedingRecordDao


    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shishu_sneh.db"
                ).fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
    }
}
