package com.careloop.app.feature.ocr

import android.content.Context
import org.json.JSONArray
import java.util.regex.Pattern

data class ParsedPrescriptionResult(
    val medicationName: String,
    val dosageForm: String,
    val strength: String,
    val frequency: String,
    val instructions: String,
    val isDictionaryMatch: Boolean,
    val confidenceScore: Float // 0.0 to 1.0
)

class PrescriptionOcrParser(private val context: Context? = null) {

    private val dictionary = mutableListOf<MedicationDictionaryItem>()

    data class MedicationDictionaryItem(
        val name: String,
        val generic: String,
        val defaultStrength: String,
        val defaultForm: String
    )

    init {
        loadDictionary()
    }

    private fun loadDictionary() {
        try {
            val json = context?.assets?.open("medication_dictionary.json")?.bufferedReader()?.use { it.readText() }
            if (json != null) {
                val array = JSONArray(json)
                for (i in 0 until array.length()) {
                    val item = array.getJSONObject(i)
                    dictionary.add(
                        MedicationDictionaryItem(
                            name = item.getString("name"),
                            generic = item.getString("generic"),
                            defaultStrength = item.getString("defaultStrength"),
                            defaultForm = item.getString("defaultForm")
                        )
                    )
                }
            } else {
                addFallbackDictionary()
            }
        } catch (e: Exception) {
            addFallbackDictionary()
        }
    }

    private fun addFallbackDictionary() {
        dictionary.add(MedicationDictionaryItem("Kardegic", "Aspirin", "75mg", "sachet"))
        dictionary.add(MedicationDictionaryItem("Doliprane", "Paracetamol", "1000mg", "tablet"))
    }

    fun parsePrescriptionText(extractedText: String): ParsedPrescriptionResult {
        val lowerText = extractedText.lowercase()

        // 1. Match against local dictionary
        var matchedItem: MedicationDictionaryItem? = null
        for (item in dictionary) {
            if (lowerText.contains(item.name.lowercase()) || lowerText.contains(item.generic.lowercase())) {
                matchedItem = item
                break
            }
        }

        // 2. Regex for strength (e.g. 75mg, 500 mg, 1000mg, 50mcg, 2.5mg)
        val strengthPattern = Pattern.compile("(\\d+(\\.\\d+)?\\s*(mg|g|mcg|ml))", Pattern.CASE_INSENSITIVE)
        val strengthMatcher = strengthPattern.matcher(extractedText)
        val strength = if (strengthMatcher.find()) {
            strengthMatcher.group(1)?.trim() ?: (matchedItem?.defaultStrength ?: "")
        } else {
            matchedItem?.defaultStrength ?: ""
        }

        // 3. Regex for dosage form
        val formPattern = Pattern.compile("(tablet|comprimé|capsule|gélule|sachet|drop|goutte)", Pattern.CASE_INSENSITIVE)
        val formMatcher = formPattern.matcher(extractedText)
        val form = if (formMatcher.find()) {
            formMatcher.group(1)?.trim() ?: (matchedItem?.defaultForm ?: "tablet")
        } else {
            matchedItem?.defaultForm ?: "tablet"
        }

        // 4. Regex for frequency & instructions (e.g., once daily, bid, tid, q8h, after meals, for 7 days)
        val freqPattern = Pattern.compile("(once daily|1\\s*fois\\s*par\\s*jour|bid|tid|q8h|after meals|après les repas|for \\d+ days|pendant \\d+ jours)", Pattern.CASE_INSENSITIVE)
        val freqMatcher = freqPattern.matcher(extractedText)
        val frequency = if (freqMatcher.find()) {
            freqMatcher.group(1)?.trim() ?: "Once daily at 08:00"
        } else {
            "Once daily at 08:00"
        }

        val medName = matchedItem?.name ?: run {
            // If no dictionary match, pick first plausible capitalized word or leave blank
            val words = extractedText.split("\\s+".toRegex())
            words.firstOrNull { it.length > 4 && it[0].isUpperCase() } ?: ""
        }

        val confidence = when {
            matchedItem != null && strength.isNotEmpty() -> 0.95f
            matchedItem != null -> 0.80f
            medName.isNotEmpty() && strength.isNotEmpty() -> 0.65f
            else -> 0.40f
        }

        return ParsedPrescriptionResult(
            medicationName = medName,
            dosageForm = form,
            strength = strength,
            frequency = frequency,
            instructions = "$form of $strength - $frequency",
            isDictionaryMatch = matchedItem != null,
            confidenceScore = confidence
        )
    }
}

