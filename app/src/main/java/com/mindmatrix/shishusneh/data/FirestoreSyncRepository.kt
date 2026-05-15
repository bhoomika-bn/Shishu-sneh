package com.mindmatrix.shishusneh.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.mindmatrix.shishusneh.data.entity.BabyProfile
import com.mindmatrix.shishusneh.data.entity.GrowthRecord
import com.mindmatrix.shishusneh.data.entity.Milestone
import com.mindmatrix.shishusneh.data.entity.Vaccination
import kotlinx.coroutines.tasks.await

class FirestoreSyncRepository(private val authRepository: AuthRepository) {
    private val firestore = Firebase.firestore

    suspend fun uploadBabyProfile(profile: BabyProfile) = withUser { uid ->
        firestore.collection("users")
            .document(uid)
            .collection("babyProfile")
            .document("main")
            .set(profile, SetOptions.merge())
            .await()
    }

    suspend fun uploadGrowthRecord(record: GrowthRecord) = withUser { uid ->
        firestore.collection("users")
            .document(uid)
            .collection("growthRecords")
            .document(record.id.toString())
            .set(record, SetOptions.merge())
            .await()
    }

    suspend fun uploadVaccination(vaccination: Vaccination) = withUser { uid ->
        firestore.collection("users")
            .document(uid)
            .collection("vaccinations")
            .document(vaccination.id.toString())
            .set(vaccination, SetOptions.merge())
            .await()
    }

    suspend fun uploadMilestone(milestone: Milestone) = withUser { uid ->
        firestore.collection("users")
            .document(uid)
            .collection("milestones")
            .document(milestone.id.toString())
            .set(milestone, SetOptions.merge())
            .await()
    }

    private suspend fun withUser(block: suspend (String) -> Unit) {
        val uid = authRepository.currentUser?.uid ?: return
        runCatching { block(uid) }
    }
}
