package com.careloop.app.domain.model

enum class ChannelPreference {
    PUSH,
    SMS,
    CALL
}

data class Caregiver(
    val id: String,
    val patientId: String = "default_patient",
    val name: String,
    val relation: String, // e.g. "Daughter", "Sibling", "Son", "Neighbor"
    val phone: String,
    val email: String = "",
    val fcmToken: String? = null,
    val priorityRank: Int = 1, // 1 is first contacted, 2 is next
    val channelPreference: ChannelPreference = ChannelPreference.SMS,
    val consentGrantedAt: Long? = null, // GDPR compliance audit timestamp
    val pairingToken: String? = null
)

enum class AdherenceAction {
    CONFIRMED,
    SNOOZED,
    MISSED,
    ESCALATED,
    RESTORED
}

data class AdherenceLog(
    val id: Long = 0,
    val commitmentId: String,
    val expectedAt: Long,
    val actedAt: Long = System.currentTimeMillis(),
    val action: AdherenceAction,
    val note: String = ""
)

data class HealthEvent(
    val id: String,
    val sourceType: String, // "MOCK_FHIR", "MANUAL", "OCR", "DMP_FHIR"
    val type: CommitmentType,
    val title: String,
    val dueAt: Long,
    val rawJson: String = "",
    val isCompletedInSource: Boolean = false
)

data class Patient(
    val id: String = "default_patient",
    val name: String,
    val birthDate: String = "",
    val emergencyNote: String = ""
)

