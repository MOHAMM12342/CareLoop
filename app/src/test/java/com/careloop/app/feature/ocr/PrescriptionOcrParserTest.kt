package com.careloop.app.feature.ocr

import org.junit.Assert.*
import org.junit.Test

class PrescriptionOcrParserTest {

    private val parser = PrescriptionOcrParser(null)

    @Test
    fun testParseKnownMedicationKardegicWithHighConfidence() {
        val ocrSample = """
            Cabinet Médical Dr. Laurent
            Patient: Jean Dupont
            Ordonnance:
            Prendre Kardegic 75mg sachet
            1 sachet par jour le matin pendant 30 jours
        """.trimIndent()

        val result = parser.parsePrescriptionText(ocrSample)

        assertEquals("Kardegic", result.medicationName)
        assertEquals("75mg", result.strength)
        assertEquals("sachet", result.dosageForm)
        assertTrue(result.isDictionaryMatch)
        assertTrue("Confidence should be >= 0.8 for known dictionary drug", result.confidenceScore >= 0.8f)
    }

    @Test
    fun testParseKnownMedicationDoliprane() {
        val ocrSample = """
            Doliprane 1000mg comprimé
            1 comprimé si douleur
        """.trimIndent()

        val result = parser.parsePrescriptionText(ocrSample)

        assertEquals("Doliprane", result.medicationName)
        assertEquals("1000mg", result.strength)
        assertEquals("comprimé", result.dosageForm)
        assertTrue(result.isDictionaryMatch)
    }

    @Test
    fun testUnknownMedicationLowConfidenceRequiresManualTyping() {
        val ocrSample = """
            UnrecognisedRx 250mg
            Take once daily
        """.trimIndent()

        val result = parser.parsePrescriptionText(ocrSample)

        assertFalse("Should not match dictionary", result.isDictionaryMatch)
        assertTrue("Confidence should be low (< 0.7) for unknown medication", result.confidenceScore < 0.7f)
    }
}

