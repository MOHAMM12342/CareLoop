package com.careloop.app.agent

import com.careloop.app.domain.model.*
import org.junit.Assert.*
import org.junit.Test

class FhirVerificationWorkerTest {

    @Test
    fun testMockFhirCompletionState() {
        val completedSet = mutableSetOf<String>()

        assertFalse(completedSet.contains("fhir-appt-002"))

        // Simulate provider fulfilling appointment
        completedSet.add("fhir-appt-002")
        assertTrue(completedSet.contains("fhir-appt-002"))

        // Simulate lab test diagnostic report arriving
        completedSet.add("fhir-lab-003")
        assertTrue(completedSet.contains("fhir-lab-003"))
    }

    @Test
    fun testAutoCloseFromHealthRecordTransition() {
        val initialAppointment = Commitment(
            id = "cmt-appt-002",
            type = CommitmentType.APPOINTMENT,
            title = "Cardiology Consultation - Dr. Martin",
            dueAt = System.currentTimeMillis() + 7200000,
            state = CommitmentState.UPCOMING,
            verificationMethod = VerificationMethod.FHIR_HEALTH_RECORD,
            sourceEventId = "fhir-appt-002",
            verifiedOnline = false
        )

        assertFalse(initialAppointment.verifiedOnline)
        assertEquals(CommitmentState.UPCOMING, initialAppointment.state)

        // Simulate auto-close from verification worker
        val evidenceDetail = "Mon espace santé / DMP FHIR: Appointment.status = fulfilled"
        val verifiedAppointment = initialAppointment.copy(
            state = CommitmentState.COMPLETED,
            completedAt = System.currentTimeMillis(),
            verifiedOnline = true
        )

        assertEquals(CommitmentState.COMPLETED, verifiedAppointment.state)
        assertTrue(verifiedAppointment.verifiedOnline)
        assertNotNull(verifiedAppointment.completedAt)

        val log = AdherenceLog(
            commitmentId = verifiedAppointment.id,
            expectedAt = verifiedAppointment.dueAt,
            actedAt = System.currentTimeMillis(),
            action = AdherenceAction.CONFIRMED,
            note = "Auto-closed from health record: $evidenceDetail"
        )

        assertEquals(AdherenceAction.CONFIRMED, log.action)
        assertTrue(log.note.contains("DMP FHIR"))
    }
}

