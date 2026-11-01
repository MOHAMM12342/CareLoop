package com.careloop.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.careloop.app.CareLoopApplication
import com.careloop.app.ui.gesture.multiTapDetector
import com.careloop.app.ui.screens.*

@Composable
fun CareLoopNavGraph(
    navController: NavHostController,
    app: CareLoopApplication
) {
    // Multi-tap detector wrapper: 3 rapid taps anywhere on the screen directly opens the Quick Commitment Checker
    Box(
        modifier = Modifier
            .fillMaxSize()
            .multiTapDetector(requiredTaps = 3, timeWindowMs = 1200L) {
                navController.navigate(Screen.QuickChecker.route)
            }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Today.route
        ) {
            composable(Screen.Today.route) {
                TodayTimelineScreen(
                    app = app,
                    onNavigateToDetail = { id -> navController.navigate(Screen.Detail.createRoute(id)) },
                    onNavigateToQuickChecker = { navController.navigate(Screen.QuickChecker.route) },
                    onNavigateToManual = { navController.navigate(Screen.ManualEntry.route) },
                    onNavigateToOcr = { navController.navigate(Screen.OcrPrescription.route) },
                    onNavigateToCaregivers = { navController.navigate(Screen.CaregiverSetup.route) },
                    onNavigateToPharmacies = { navController.navigate(Screen.PharmacyFinder.route) },
                    onNavigateToDemo = { navController.navigate(Screen.DemoMode.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
                )
            }

            composable(Screen.QuickChecker.route) {
                QuickCheckerScreen(
                    app = app,
                    onDismiss = { navController.popBackStack() }
                )
            }

            composable(Screen.Detail.route) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("commitmentId") ?: ""
                CommitmentDetailScreen(
                    app = app,
                    commitmentId = id,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.ManualEntry.route) {
                ManualEntryScreen(
                    app = app,
                    onNavigateBack = { navController.popBackStack() },
                    onCommitmentCreated = { navController.popBackStack() }
                )
            }

            composable(Screen.OcrPrescription.route) {
                OcrPrescriptionScreen(
                    app = app,
                    onNavigateBack = { navController.popBackStack() },
                    onCommitmentCreated = { navController.popBackStack() }
                )
            }

            composable(Screen.CaregiverSetup.route) {
                CaregiverSetupScreen(
                    app = app,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToTimeline = { navController.navigate(Screen.CaregiverTimeline.route) }
                )
            }

            composable(Screen.CaregiverTimeline.route) {
                CaregiverTimelineScreen(
                    app = app,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.PharmacyFinder.route) {
                PharmacyFinderScreen(
                    app = app,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    app = app,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Screen.DemoMode.route) {
                DemoModeScreen(
                    app = app,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
