package com.careloop.app.data.source

import android.content.Context
import com.careloop.app.domain.model.CommitmentType
import com.careloop.app.domain.model.HealthEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

interface HealthEventSource {
    val sourceType: String
    suspend fun fetchEvents(): List<HealthEvent>
    suspend fun checkEventCompletion(sourceEventId: String): Boolean
}

class MockFhirSource(private val context: Context) : HealthEventSource {
    override val sourceType: String = "MOCK_FHIR"

    // In-memory simulation state for verification worker demo
    private val completedEvents = mutableSetOf<String>()

    override suspend fun fetchEvents(): List<HealthEvent> = withContext(Dispatchers.IO) {
        val jsonString = context.assets.open("mock_fhir_events.json").bufferedReader().use { it.readText() }
        val array = JSONArray(jsonString)
        val list = mutableListOf<HealthEvent>()
        val now = System.currentTimeMillis()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            val id = obj.getString("id")
            val typeStr = obj.getString("type")
            val title = obj.getString("title")
            val dueMinutes = obj.optLong("dueMinutesFromNow", 60)
            val dueAt = now + (dueMinutes * 60 * 1000)
            val isCompleted = completedEvents.contains(id) || obj.optBoolean("completedInSource", false)

            list.add(
                HealthEvent(
                    id = id,
                    sourceType = sourceType,
                    type = CommitmentType.valueOf(typeStr),
                    title = title,
                    dueAt = dueAt,
                    rawJson = obj.toString(),
                    isCompletedInSource = isCompleted
                )
            )
        }
        list
    }

    override suspend fun checkEventCompletion(sourceEventId: String): Boolean {
        return completedEvents.contains(sourceEventId)
    }

    fun markEventCompletedInFhir(sourceEventId: String) {
        completedEvents.add(sourceEventId)
    }
}

class ManualEntrySource : HealthEventSource {
    override val sourceType: String = "MANUAL"

    override suspend fun fetchEvents(): List<HealthEvent> {
        return emptyList()
    }

    override suspend fun checkEventCompletion(sourceEventId: String): Boolean {
        return false
    }

    fun createEvent(type: CommitmentType, title: String, dueAt: Long, notes: String): HealthEvent {
        val id = "manual-${System.currentTimeMillis()}"
        val raw = JSONObject().apply {
            put("type", type.name)
            put("title", title)
            put("dueAt", dueAt)
            put("notes", notes)
        }.toString()

        return HealthEvent(
            id = id,
            sourceType = sourceType,
            type = type,
            title = title,
            dueAt = dueAt,
            rawJson = raw,
            isCompletedInSource = false
        )
    }
}

class OcrPrescriptionSource : HealthEventSource {
    override val sourceType: String = "OCR"

    override suspend fun fetchEvents(): List<HealthEvent> {
        return emptyList()
    }

    override suspend fun checkEventCompletion(sourceEventId: String): Boolean {
        return false
    }

    fun createEventFromOcr(title: String, dueAt: Long, instructions: String): HealthEvent {
        val id = "ocr-${System.currentTimeMillis()}"
        return HealthEvent(
            id = id,
            sourceType = sourceType,
            type = CommitmentType.MEDICATION,
            title = title,
            dueAt = dueAt,
            rawJson = JSONObject().apply {
                put("instructions", instructions)
                put("confirmedByPatient", true)
            }.toString(),
            isCompletedInSource = false
        )
    }
}

