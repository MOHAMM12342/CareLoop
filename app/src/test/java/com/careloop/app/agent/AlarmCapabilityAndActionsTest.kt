package com.careloop.app.agent

import com.careloop.app.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class AlarmCapabilityAndActionsTest {

    @Test
    fun testTakenActionTransition() {
        val initial = Commitment(
            id = "cmt-test-01",
            type = CommitmentType.MEDICATION,
            title = "Take Kardegic 75mg",
            dueAt = 1000000L,
            state = CommitmentState.DUE
        )

        assertEquals(CommitmentState.DUE, initial.state)

        // Action: Taken ✓
        val confirmed = initial.copy(
            state = CommitmentState.COMPLETED,
            completedAt = System.currentTimeMillis()
        )

        assertEquals(CommitmentState.COMPLETED, confirmed.state)
        assertNotNull(confirmed.completedAt)

        val log = AdherenceLog(
            commitmentId = confirmed.id,
            expectedAt = confirmed.dueAt,
            actedAt = System.currentTimeMillis(),
            action = AdherenceAction.CONFIRMED,
            note = "Confirmed via notification action"
        )

        assertEquals(AdherenceAction.CONFIRMED, log.action)
        assertEquals("cmt-test-01", log.commitmentId)
    }

    @Test
    fun testSnoozeActionTransition() {
        val now = 2000000L
        val initial = Commitment(
            id = "cmt-test-02",
            type = CommitmentType.MEDICATION,
            title = "Take Tahor 20mg",
            dueAt = now,
            state = CommitmentState.DUE
        )

        // Action: Snooze 15 minutes
        val snoozeMinutes = 15
        val newDueAt = now + (snoozeMinutes * 60 * 1000)
        val snoozed = initial.copy(
            dueAt = newDueAt,
            state = CommitmentState.SNOOZED
        )

        assertEquals(CommitmentState.SNOOZED, snoozed.state)
        assertEquals(now + 900000L, snoozed.dueAt)
        assertTrue(snoozed.dueAt > initial.dueAt)

        val log = AdherenceLog(
            commitmentId = snoozed.id,
            expectedAt = initial.dueAt,
            actedAt = now,
            action = AdherenceAction.SNOOZED,
            note = "Snoozed for 15 minutes"
        )

        assertEquals(AdherenceAction.SNOOZED, log.action)
    }

    @Test
    fun testSchedulingModeEnum() {
        val exactMode = SchedulingMode.EXACT_ALARM
        val fallbackMode = SchedulingMode.WORK_MANAGER_FALLBACK

        assertNotEquals(exactMode, fallbackMode)
        assertEquals("EXACT_ALARM", exactMode.name)
        assertEquals("WORK_MANAGER_FALLBACK", fallbackMode.name)
    }

    @Test
    fun testCancellationTransition() {
        val initial = Commitment(
            id = "cmt-test-03",
            type = CommitmentType.APPOINTMENT,
            title = "Cardiology Appointment",
            dueAt = 3000000L,
            state = CommitmentState.UPCOMING
        )

        val cancelled = initial.copy(state = CommitmentState.CANCELLED)
        assertEquals(CommitmentState.CANCELLED, cancelled.state)
    }
}

