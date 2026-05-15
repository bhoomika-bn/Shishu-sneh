package com.mindmatrix.shishusneh.domain

import com.mindmatrix.shishusneh.data.entity.Milestone
import com.mindmatrix.shishusneh.data.entity.Vaccination

object HealthData {
    fun indianVaccinationSchedule(babyId: Long, dobMillis: Long): List<Vaccination> {
        fun dueAfterWeeks(name: String, weeks: Long, info: String) = Vaccination(
            babyId = babyId,
            vaccineName = name,
            diseaseInfo = info,
            scheduledDate = dobMillis + weeks * 7L * 24L * 60L * 60L * 1000L
        )

        return listOf(
            dueAfterWeeks("BCG", 0, "Protects against severe childhood tuberculosis."),
            dueAfterWeeks("OPV-0", 0, "Oral polio vaccine birth dose."),
            dueAfterWeeks("Hepatitis B Birth Dose", 0, "Protects against hepatitis B infection."),
            dueAfterWeeks("OPV-1", 6, "First primary polio dose."),
            dueAfterWeeks("Pentavalent-1", 6, "Protects against diphtheria, pertussis, tetanus, hepatitis B and Hib."),
            dueAfterWeeks("Rotavirus-1", 6, "Helps prevent severe rotavirus diarrhoea."),
            dueAfterWeeks("PCV-1", 6, "Protects against pneumococcal infections."),
            dueAfterWeeks("OPV-2", 10, "Second primary polio dose."),
            dueAfterWeeks("Pentavalent-2", 10, "Second pentavalent dose."),
            dueAfterWeeks("Rotavirus-2", 10, "Second rotavirus dose."),
            dueAfterWeeks("OPV-3", 14, "Third primary polio dose."),
            dueAfterWeeks("Pentavalent-3", 14, "Third pentavalent dose."),
            dueAfterWeeks("Rotavirus-3", 14, "Third rotavirus dose."),
            dueAfterWeeks("PCV-2", 14, "Second pneumococcal dose."),
            dueAfterWeeks("MR-1", 39, "Measles and rubella first dose."),
            dueAfterWeeks("JE-1", 39, "Japanese encephalitis dose in endemic districts."),
            dueAfterWeeks("PCV Booster", 39, "Pneumococcal booster dose.")
        )
    }

    fun firstYearMilestones(babyId: Long): List<Milestone> = listOf(
        Milestone(babyId = babyId, weekNumber = 4, title = "Looks at faces", description = "Briefly watches your face and responds to your voice."),
        Milestone(babyId = babyId, weekNumber = 8, title = "Social smile", description = "Smiles back during gentle talking or play."),
        Milestone(babyId = babyId, weekNumber = 12, title = "Lifts head", description = "Raises head during tummy time for a short while."),
        Milestone(babyId = babyId, weekNumber = 16, title = "Reaches for toys", description = "Tries to reach nearby colorful objects."),
        Milestone(babyId = babyId, weekNumber = 24, title = "Rolls over", description = "Rolls from tummy to back or back to tummy."),
        Milestone(babyId = babyId, weekNumber = 32, title = "Sits with support", description = "Sits with help and keeps head steady."),
        Milestone(babyId = babyId, weekNumber = 40, title = "Babbles", description = "Makes repeated sounds such as ba, ma or da."),
        Milestone(babyId = babyId, weekNumber = 52, title = "Stands with help", description = "Pulls to stand or stands while holding support.")
    )

    fun fallbackFeedingTip(ageWeeks: Int): String = when (ageWeeks) {
        in 0..8 -> "Feed on demand, day and night. Exclusive breastfeeding gives your baby water, energy and protection in the first months."
        in 9..16 -> "Growth spurts can make babies feed more often for a few days. Rest, fluids and frequent latching can help your supply."
        in 17..24 -> "Continue exclusive breastfeeding until 6 months unless your doctor advises otherwise. Avoid honey, water and animal milk."
        else -> "After 6 months, start soft complementary foods while continuing breastfeeding. Try iron-rich foods and watch for safe swallowing."
    }

    fun estimatedWeightPercentile(ageWeeks: Int, weightKg: Float): Float {
        val expected = 3.2f + ageWeeks * 0.14f
        return ((weightKg / expected) * 50f).coerceIn(3f, 97f)
    }

    fun estimatedHeightPercentile(ageWeeks: Int, heightCm: Float): Float {
        val expected = 50f + ageWeeks * 0.55f
        return ((heightCm / expected) * 50f).coerceIn(3f, 97f)
    }
}
