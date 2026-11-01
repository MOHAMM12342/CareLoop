package com.careloop.app.domain.escalation

data class EscalationResult(
    val success: Boolean,
    val channelType: String,
    val message: String,
    val error: String? = null
)

interface EscalationChannel {
    val channelName: String
    suspend fun sendEmergencyAlert(caregiverPhone: String, patientName: String): EscalationResult

    companion object {
        /**
         * Strictly data-minimised message required by privacy specifications.
         * NEVER contains medication names, dosages, or diagnostic info.
         */
        fun buildDataMinimisedMessage(patientName: String): String {
            return "$patientName has not confirmed an important reminder."
        }
    }
}

