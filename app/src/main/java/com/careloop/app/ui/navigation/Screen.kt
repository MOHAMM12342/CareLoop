package com.careloop.app.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Today : Screen("today", "Today's Commitments")
    object Detail : Screen("detail/{commitmentId}", "Commitment Detail") {
        fun createRoute(id: String) = "detail/$id"
    }
    object ManualEntry : Screen("manual_entry", "Add Commitment")
    object OcrPrescription : Screen("ocr_prescription", "Scan Prescription")
    object CaregiverSetup : Screen("caregivers", "Caregivers")
    object CaregiverTimeline : Screen("timeline", "Caregiver Timeline")
    object PharmacyFinder : Screen("pharmacies", "Nearby Pharmacies")
    object Settings : Screen("settings", "Settings")
    object DemoMode : Screen("demo_mode", "Stage Demo Mode")
    object QuickChecker : Screen("quick_checker", "Quick Commitment Check")
    object CaregiverCompanion : Screen("caregiver_companion", "Family Companion")
    object Assistant : Screen("assistant", "CareLoop Agent")
}
