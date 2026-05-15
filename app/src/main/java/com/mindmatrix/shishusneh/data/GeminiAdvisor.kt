package com.mindmatrix.shishusneh.data

import com.google.ai.client.generativeai.GenerativeModel
import com.mindmatrix.shishusneh.BuildConfig
import com.mindmatrix.shishusneh.domain.HealthData

class GeminiAdvisor {
    private val model: GenerativeModel? = BuildConfig.GEMINI_API_KEY
        .takeIf { it.isNotBlank() }
        ?.let { GenerativeModel(modelName = "gemini-1.5-flash", apiKey = it) }

    suspend fun feedingTip(ageWeeks: Int): String {
        val activeModel = model ?: return HealthData.fallbackFeedingTip(ageWeeks)
        val prompt = """
            Give one warm, practical baby feeding tip for an Indian mother with a $ageWeeks-week-old baby.
            Keep it under 80 words. Do not diagnose. Tell the parent to contact a doctor for urgent symptoms.
        """.trimIndent()

        return runCatching {
            activeModel.generateContent(prompt).text.orEmpty()
        }.getOrNull()?.takeIf { it.isNotBlank() } ?: HealthData.fallbackFeedingTip(ageWeeks)
    }
}
