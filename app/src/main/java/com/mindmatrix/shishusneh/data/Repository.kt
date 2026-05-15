package com.mindmatrix.shishusneh.data

import com.mindmatrix.shishusneh.data.entity.BabyProfile
import com.mindmatrix.shishusneh.data.entity.FeedingRecord
import com.mindmatrix.shishusneh.data.entity.FeedingTip
import com.mindmatrix.shishusneh.data.entity.GrowthRecord
import com.mindmatrix.shishusneh.data.entity.Milestone
import com.mindmatrix.shishusneh.data.entity.Vaccination
import com.mindmatrix.shishusneh.domain.HealthData
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

class Repository(
    private val db: AppDatabase,
    private val syncRepository: FirestoreSyncRepository
) {
    private val geminiAdvisor = GeminiAdvisor()
    val babyProfile: Flow<BabyProfile?> = db.babyProfileDao().observeProfile()

    suspend fun saveBaby(profile: BabyProfile): Long {
        val id = db.babyProfileDao().insert(profile)
        syncRepository.uploadBabyProfile(profile.copy(id = id))
        seedCoreData(id, profile.dateOfBirth)
        return id
    }

    suspend fun ensureSeeded() {
        val baby = db.babyProfileDao().getProfile() ?: return
        seedCoreData(baby.id, baby.dateOfBirth)
    }

    fun growthRecords(babyId: Long): Flow<List<GrowthRecord>> = db.growthRecordDao().observeForBaby(babyId)

    suspend fun addGrowthRecord(record: GrowthRecord) {
        val id = db.growthRecordDao().insert(record)
        syncRepository.uploadGrowthRecord(record.copy(id = id))
    }
    suspend fun saveBabyProfile(baby: BabyProfile) {
        db.babyProfileDao().insert(baby)
    }

    fun vaccinations(babyId: Long): Flow<List<Vaccination>> = db.vaccinationDao().observeForBaby(babyId)

    suspend fun updateVaccination(vaccination: Vaccination) {
        db.vaccinationDao().update(vaccination)
        syncRepository.uploadVaccination(vaccination)
    }

    fun milestones(babyId: Long): Flow<List<Milestone>> = db.milestoneDao().observeForBaby(babyId)
    fun feedingRecords(babyId: Long) =
        db.feedingRecordDao().observeForBaby(babyId)

    suspend fun addFeedingRecord(record: FeedingRecord) {
        db.feedingRecordDao().insert(record)
    }
    suspend fun updateMilestone(milestone: Milestone) {
        db.milestoneDao().update(milestone)
        syncRepository.uploadMilestone(milestone)
    }

    suspend fun todayTip(babyId: Long, ageWeeks: Int): FeedingTip {
        val today = LocalDate.now().toEpochDay()

        val existing = db.feedingTipDao().getForDate(
            babyId,
            dateEpochDay = today
        )

        if (existing != null) return existing

        val aiTip = geminiAdvisor.feedingTip(ageWeeks)

        val tip = FeedingTip(
            babyId = babyId,
            dateEpochDay = today,
            content = aiTip,
            babyAgeWeeks = ageWeeks
        )

        db.feedingTipDao().insert(tip)
        return tip
    }

    private suspend fun seedCoreData(babyId: Long, dobMillis: Long) {
        if (db.vaccinationDao().countForBaby(babyId) == 0) {
            db.vaccinationDao().insertAll(HealthData.indianVaccinationSchedule(babyId, dobMillis))
        }
        if (db.milestoneDao().countForBaby(babyId) == 0) {
            db.milestoneDao().insertAll(HealthData.firstYearMilestones(babyId))
        }
    }
}
