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
data class FcmRelayRequest(
    val pairingToken: String,
    val caregiverFcmToken: String,
    val patientName: String,
    val alertMessage: String, // Strictly data minimised
    val priority: String = "HIGH"
)

@Serializable
data class FcmRelayResponse(
    val success: Boolean,
    val relayMessageId: String? = null,
    val error: String? = null
)

class FcmRelayClient(
    private val backendBaseUrl: String = "http://10.0.2.2:8080"
) {
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    suspend fun dispatchCaregiverPush(request: FcmRelayRequest): Result<FcmRelayResponse> = withContext(Dispatchers.IO) {
        try {
            val response: HttpResponse = httpClient.post("$backendBaseUrl/api/fcm/relay") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            if (response.status.isSuccess()) {
                Result.success(FcmRelayResponse(success = true, relayMessageId = "fcm-${System.currentTimeMillis()}"))
            } else {
                Result.failure(Exception("FCM Relay failed with HTTP ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

