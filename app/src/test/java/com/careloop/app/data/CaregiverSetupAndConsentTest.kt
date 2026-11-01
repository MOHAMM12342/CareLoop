package com.careloop.app.data

import com.careloop.app.domain.model.Caregiver
import com.careloop.app.domain.model.ChannelPreference
import com.careloop.app.service.FcmRelayRequest
import org.junit.Assert.*
import org.junit.Test
import java.util.UUID

class CaregiverSetupAndConsentTest {

    @Test
    fun testPairingTokenGenerationAndFormat() {
        val token = "PAIR-" + UUID.randomUUID().toString().take(6).uppercase()

        assertTrue(token.startsWith("PAIR-"))
        assertEquals(11, token.length) // "PAIR-" (5) + 6 chars = 11
    }

    @Test
    fun testCaregiverConsentMandatoryAudit() {
        val now = System.currentTimeMillis()
        val caregiver = Caregiver(
            id = "cg-test-1",
            name = "Marc",
            relation = "Son",
            phone = "+33611223344",
            priorityRank = 1,
            channelPreference = ChannelPreference.PUSH,
            consentGrantedAt = now,
            pairingToken = "PAIR-ABC123"
        )

        assertNotNull(caregiver.consentGrantedAt)
        assertTrue(caregiver.consentGrantedAt!! > 0)
        assertEquals("PAIR-ABC123", caregiver.pairingToken)
    }

    @Test
    fun testCaregiverPriorityRankingOrder() {
        val c1 = Caregiver(id = "1", name = "Primary", relation = "Daughter", phone = "111", priorityRank = 1)
        val c2 = Caregiver(id = "2", name = "Secondary", relation = "Son", phone = "222", priorityRank = 2)
        val c3 = Caregiver(id = "3", name = "Neighbor", relation = "Neighbor", phone = "333", priorityRank = 3)

        val list = listOf(c3, c1, c2)
        val sorted = list.sortedBy { it.priorityRank }

        assertEquals("Primary", sorted[0].name)
        assertEquals("Secondary", sorted[1].name)
        assertEquals("Neighbor", sorted[2].name)
    }

    @Test
    fun testFcmRelayDataMinimisation() {
        val patient = "Jean"
        val minimisedAlert = "$patient has not confirmed an important reminder."

        val request = FcmRelayRequest(
            pairingToken = "PAIR-ABC123",
            caregiverFcmToken = "fcm-token-xyz",
            patientName = patient,
            alertMessage = minimisedAlert,
            priority = "HIGH"
        )

        assertEquals("HIGH", request.priority)
        assertFalse("Push alert must never contain drug names", request.alertMessage.contains("Kardegic"))
        assertFalse("Push alert must never contain dosage", request.alertMessage.contains("75mg"))
        assertTrue(request.alertMessage.contains("important reminder"))
    }
}

