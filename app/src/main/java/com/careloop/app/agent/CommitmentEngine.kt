package com.careloop.app.agent

import com.careloop.app.data.repository.CommitmentRepository
import com.careloop.app.domain.model.*

class CommitmentEngine(
    private val commitmentRepository: CommitmentRepository
) {
    suspend fun createCommitmentFromEvent(event: HealthEvent): Commitment {
        val verificationMethod = when (event.sourceType) {
            "MOCK_FHIR", "DMP_FHIR" -> VerificationMethod.FHIR_HEALTH_RECORD
            "OCR" -> VerificationMethod.OCR_PRESCRIPTION
            else -> VerificationMethod.MANUAL
        }

        val commitment = Commitment(
            id = "cmt-${event.id}",
            patientId = "default_patient",
            type = event.type,
            title = event.title,
            dueAt = event.dueAt,
            state = if (event.dueAt <= System.currentTimeMillis()) CommitmentState.DUE else CommitmentState.UPCOMING,
            verificationMethod = verificationMethod,
            sourceEventId = event.id,
            notes = event.rawJson
        )

        commitmentRepository.insertCommitment(commitment)
        return commitment
    }

    suspend fun confirmCommitmentTaken(commitmentId: String, note: String = "Confirmed by patient") {
        commitmentRepository.updateState(
            id = commitmentId,
            newState = CommitmentState.COMPLETED,
            action = AdherenceAction.CONFIRMED,
            note = note
        )
    }

    suspend fun snoozeCommitment(commitmentId: String, snoozeMinutes: Int = 15) {
        val commitment = commitmentRepository.getCommitmentById(commitmentId) ?: return
        val newDueAt = System.currentTimeMillis() + (snoozeMinutes * 60 * 1000)
        val updated = commitment.copy(
            dueAt = newDueAt,
            state = CommitmentState.SNOOZED
        )
        commitmentRepository.insertCommitment(updated)
        commitmentRepository.updateState(
            id = commitmentId,
            newState = CommitmentState.SNOOZED,
            action = AdherenceAction.SNOOZED,
            note = "Snoozed for $snoozeMinutes minutes"
        )
    }

    suspend fun markMissed(commitmentId: String) {
        commitmentRepository.updateState(
            id = commitmentId,
            newState = CommitmentState.MISSED,
            action = AdherenceAction.MISSED,
            note = "Grace period elapsed without confirmation"
        )
    }

    suspend fun autoCloseFromHealthRecord(commitmentId: String, evidenceDetail: String) {
        commitmentRepository.updateState(
            id = commitmentId,
            newState = CommitmentState.COMPLETED,
            action = AdherenceAction.CONFIRMED,
            note = "Auto-closed from health record: $evidenceDetail",
            verifiedOnline = true
        )
    }
}

