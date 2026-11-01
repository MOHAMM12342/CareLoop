package com.careloop.app.service

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class EscalationRequest(
    val caregiverName: String,
    val caregiverPhone: String,
    val patientName: String,
    val commitmentTitle: String,
    val missedTimeFormatted: String,
    val channel: String // "SMS" or "VOICE_CALL"
)

@Serializable
data class EscalationResponse(
    val success: Boolean,
    val messageId: String? = null,
    val error: String? = null
)

class BackendEscalationClient(
    private val backendBaseUrl: String = "http://10.0.2.2:8080" // local emulator default / configurable
) {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun sendCaregiverAlert(request: EscalationRequest): Result<EscalationResponse> = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = httpClient.post("$backendBaseUrl/api/escalate") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status.isSuccess()) {
                Result.success(EscalationResponse(success = true))
            } else {
                Result.failure(Exception("Backend responded with HTTP ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

