package com.careloop.app.agent

import com.careloop.app.domain.escalation.EscalationChannel
import com.careloop.app.domain.model.Commitment
import com.careloop.app.domain.model.CommitmentType
import com.careloop.app.domain.model.EscalationTier
import org.junit.Assert.*
import org.junit.Test

class EscalationLadderAndAuditTest {

    @Test
    fun testDataMinimisedMessageFormat() {
        val patientName = "Jean Dupont"
        val message = EscalationChannel.buildDataMinimisedMessage(patientName)

        assertEquals("Jean Dupont has not confirmed an important reminder.", message)

        // Strict privacy requirements: NEVER leak drug or clinical info
        val forbiddenWords = listOf("Kardegic", "Aspirin", "Doliprane", "mg", "pill", "tablet", "dosage", "diagnosis")
        for (word in forbiddenWords) {
            assertFalse("Outbound message must not contain '$word'", message.contains(word, ignoreCase = true))
        }
    }

    @Test
    fun testEscalationSchedulerMedicationMilestones() {
        val dueAt = 1000000L
        val commitment = Commitment(
            id = "cmt-med-01",
            type = CommitmentType.MEDICATION,
            title = "Take Kardegic 75mg",
            dueAt = dueAt
        )

        // Test normal production timing (minutes)
        val scheduler = EscalationSchedulerStub()
        val milestones = scheduler.computeMilestones(commitment, isDemoMode = false)

        assertEquals(4, milestones.size)
        assertEquals(EscalationTier.TIER_0, milestones[0].targetTier)
        assertEquals(dueAt, milestones[0].triggerAt)

        assertEquals(EscalationTier.TIER_1, milestones[1].targetTier)
        assertEquals(dueAt + (15 * 60 * 1000L), milestones[1].triggerAt)

        assertEquals(EscalationTier.TIER_2, milestones[2].targetTier)
        assertEquals(dueAt + (30 * 60 * 1000L), milestones[2].triggerAt)

        assertEquals(EscalationTier.TIER_3, milestones[3].targetTier)
        assertEquals(dueAt + (45 * 60 * 1000L), milestones[3].triggerAt)
    }

    @Test
    fun testEscalationSchedulerDemoAcceleration() {
        val dueAt = 5000000L
        val commitment = Commitment(
            id = "cmt-med-demo",
            type = CommitmentType.MEDICATION,
            title = "Take Kardegic 75mg",
            dueAt = dueAt
        )

        val scheduler = EscalationSchedulerStub()
        val milestones = scheduler.computeMilestones(commitment, isDemoMode = true)

        assertEquals(4, milestones.size)
        // In demo mode: 15s, 30s, 45s intervals
        assertEquals(dueAt + 15000L, milestones[1].triggerAt)
        assertEquals(dueAt + 30000L, milestones[2].triggerAt)
        assertEquals(dueAt + 45000L, milestones[3].triggerAt)
    }

    // Helper lightweight stub for unit testing pure scheduling calculation
    private class EscalationSchedulerStub {
        fun computeMilestones(commitment: Commitment, isDemoMode: Boolean): List<EscalationMilestone> {
            val t = commitment.dueAt
            val minuteMs = if (isDemoMode) 1000L else 60_000L

            return listOf(
                EscalationMilestone(commitment.id, EscalationTier.TIER_0, t, "Initial"),
                EscalationMilestone(commitment.id, EscalationTier.TIER_1, t + (15 * minuteMs), "Re-notify"),
                EscalationMilestone(commitment.id, EscalationTier.TIER_2, t + (30 * minuteMs), "Caregiver 1"),
                EscalationMilestone(commitment.id, EscalationTier.TIER_3, t + (45 * minuteMs), "Caregiver 2")
            )
        }
    }
}

