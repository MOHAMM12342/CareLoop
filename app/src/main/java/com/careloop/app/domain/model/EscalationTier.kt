package com.careloop.app.domain.model

enum class EscalationTier(val level: Int, val description: String) {
    TIER_0(0, "Local high-priority notification with sound & vibration"),
    TIER_1(1, "Local re-notification with Text-to-Speech voice prompt"),
    TIER_2(2, "Caregiver push notification via FCM (requires patient & caregiver online)"),
    TIER_3(3, "Automated SMS / Voice call via stateless backend (requires patient online)"),
    TIER_4(4, "Offline fallback: user-initiated SMS intent or full-screen call prompt")
}

data class Pharmacy(
    val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String? = null,
    val distanceMeters: Int? = null,
    val isCached: Boolean = false
)

