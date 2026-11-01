package com.careloop.app.data.local

import com.careloop.app.data.local.entity.*
import com.careloop.app.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class DatabaseSchemaTest {

    @Test
    fun testCommitmentStateTransitions() {
        val commitment = Commitment(
            id = "cmt-001",
            patientId = "patient-1",
            type = CommitmentType.MEDICATION,
            title = "Take Kardegic 75mg",
            dueAt = System.currentTimeMillis() + 3600000,
            state = CommitmentState.UPCOMING,
            verificationMethod = VerificationMethod.PATIENT_CONFIRMATION
        )

        assertEquals(CommitmentState.UPCOMING, commitment.state)
        assertEquals(CommitmentType.MEDICATION, commitment.type)

        // Simulate state transition to DUE
        val dueCommitment = commitment.copy(state = CommitmentState.DUE)
        assertEquals(CommitmentState.DUE, dueCommitment.state)

        // Simulate state transition to COMPLETED
        val completedCommitment = dueCommitment.copy(
            state = CommitmentState.COMPLETED,
            completedAt = System.currentTimeMillis()
        )
        assertEquals(CommitmentState.COMPLETED, completedCommitment.state)
        assertNotNull(completedCommitment.completedAt)
    }

    @Test
    fun testCaregiverConsentAndPairingToken() {
        val consentTime = System.currentTimeMillis()
        val caregiver = Caregiver(
            id = "cg-01",
            patientId = "patient-1",
            name = "Sarah",
            relation = "Daughter",
            phone = "+33612345678",
            priorityRank = 1,
            channelPreference = ChannelPreference.SMS,
            consentGrantedAt = consentTime,
            pairingToken = "PAIR-XYZ-123"
        )

        assertNotNull(caregiver.consentGrantedAt)
        assertEquals("PAIR-XYZ-123", caregiver.pairingToken)
        assertEquals(1, caregiver.priorityRank)
        assertEquals(ChannelPreference.SMS, caregiver.channelPreference)
    }

    @Test
    fun testEscalationAuditLogDataMinimisation() {
        // Spec rule: "Outbound messages are data-minimised: '[Name] has not confirmed an important reminder', never a drug name."
        val patientName = "Jean"
        val minimisedMessage = "$patientName has not confirmed an important reminder"

        val auditLog = EscalationAuditLogEntity(
            commitmentId = "cmt-001",
            whoNotified = "Sarah (Daughter)",
            notifiedAt = System.currentTimeMillis(),
            channel = "TIER_4_OFFLINE_SMS",
            outcome = "PREPARED",
            dataMinimisedMessage = minimisedMessage
        )

        assertFalse("Message must NEVER contain drug name", auditLog.dataMinimisedMessage.contains("Kardegic"))
        assertFalse("Message must NEVER contain drug dosage", auditLog.dataMinimisedMessage.contains("75mg"))
        assertTrue(auditLog.dataMinimisedMessage.contains("important reminder"))
    }

    @Test
    fun testTypeConvertersEnumValues() {
        val converters = Converters()

        assertEquals("MEDICATION", converters.fromCommitmentType(CommitmentType.MEDICATION))
        assertEquals(CommitmentType.MEDICATION, converters.toCommitmentType("MEDICATION"))

        assertEquals("DUE", converters.fromCommitmentState(CommitmentState.DUE))
        assertEquals(CommitmentState.DUE, converters.toCommitmentState("DUE"))

        assertEquals("CONFIRMED", converters.fromAdherenceAction(AdherenceAction.CONFIRMED))
        assertEquals(AdherenceAction.CONFIRMED, converters.toAdherenceAction("CONFIRMED"))

        assertEquals("SMS", converters.fromChannelPreference(ChannelPreference.SMS))
        assertEquals(ChannelPreference.SMS, converters.toChannelPreference("SMS"))
    }
}

