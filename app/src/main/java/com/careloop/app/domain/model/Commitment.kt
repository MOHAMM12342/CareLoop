package com.careloop.app.domain.model

enum class CommitmentType {
    MEDICATION,
    APPOINTMENT,
    LAB_TEST
}

enum class CommitmentState {
    UPCOMING,
    DUE,
    SNOOZED,
    MISSED,
    ESCALATED,
    COMPLETED,
    CANCELLED
}

enum class VerificationMethod {
    PATIENT_CONFIRMATION,
    FHIR_HEALTH_RECORD,
    OCR_PRESCRIPTION,
    MANUAL
}

data class Commitment(
    val id: String,
    val patientId: String = "default_patient",
    val type: CommitmentType,
    val title: String,
    val dueAt: Long,
    val state: CommitmentState = CommitmentState.UPCOMING,
    val verificationMethod: VerificationMethod = VerificationMethod.PATIENT_CONFIRMATION,
    val sourceEventId: String? = null,
    val escalationLevel: Int = 0,
    val completedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val notes: String = "",
    val verifiedOnline: Boolean = false
)

