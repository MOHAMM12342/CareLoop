package com.careloop.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.careloop.app.CareLoopApplication
import com.careloop.app.domain.model.EscalationTier
import com.careloop.app.ui.components.ElderlyButton
import com.careloop.app.ui.components.OfflineStatusBanner
import com.careloop.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DemoModeScreen(
    app: CareLoopApplication,
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val isOnline by app.connectivityMonitor.isInternetReachable.collectAsState()
    var statusMessage by remember { mutableStateOf("Ready for stage demo sequence.") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stage Demo Controller", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(18.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OfflineStatusBanner(isOnline = isOnline)

            Surface(
                color = DeepNavy,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3-Minute Stage Demo Script",
                        style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Follow steps 1 through 5 below to demonstrate offline commitment tracking, exact alarms, caregiver escalation (Wi-Fi/data off, cellular on), and opportunistic FHIR verification.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.85f))
                    )
                }
            }

            Surface(
                color = Color(0xFFF1F8E9),
                border = BorderStroke(1.dp, HighContrastGreen),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Status: $statusMessage",
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.SemiBold,
                    color = HighContrastGreen
                )
            }

            // Step 1: Ingest sample events
            Text("Step 1: Ingest FHIR Events", fontWeight = FontWeight.Bold)
            ElderlyButton(
                text = "1. Load Sample Mock FHIR Events",
                icon = Icons.Default.CloudDownload,
                containerColor = PrimaryBlue,
                onClick = {
                    coroutineScope.launch {
                        val events = app.mockFhirSource.fetchEvents()
                        for (event in events) {
                            app.commitmentEngine.createCommitmentFromEvent(event)
                        }
                        statusMessage = "Loaded 3 commitments: Kardegic 75mg, Cardiology consultation, and Blood test."
                    }
                }
            )

            // Step 2: Trigger alarm
            Text("Step 2: Trigger Medication Alarm", fontWeight = FontWeight.Bold)
            ElderlyButton(
                text = "2. Fire Kardegic 75mg Alarm",
                icon = Icons.Default.Alarm,
                containerColor = WarningAmber,
                onClick = {
                    coroutineScope.launch {
                        val commitment = app.commitmentRepository.getCommitmentBySourceId("fhir-med-001")
                        if (commitment != null) {
                            app.escalationLadderManager.executeEscalation(commitment.id, EscalationTier.TIER_0)
                            statusMessage = "Fired high-priority notification with Taken/Snooze action buttons."
                        } else {
                            statusMessage = "Error: Load sample events first!"
                        }
                    }
                }
            )

            // Step 3: Trigger Caregiver Escalation (The money moment)
            Text("Step 3: Trigger Family Escalation (Money Moment)", fontWeight = FontWeight.Bold)
            ElderlyButton(
                text = "3. Simulate Unconfirmed Grace Period",
                icon = Icons.Default.FamilyRestroom,
                containerColor = HighContrastRed,
                onClick = {
                    coroutineScope.launch {
                        val commitment = app.commitmentRepository.getCommitmentBySourceId("fhir-med-001")
                        if (commitment != null) {
                            // First execute voice TTS prompt (Tier 1)
                            app.escalationLadderManager.executeEscalation(commitment.id, EscalationTier.TIER_1)
                            // Then execute Tier 3 (Cloud SMS/Call) or Tier 4 (Offline SMS Intent)
                            val note = app.escalationLadderManager.executeEscalation(
                                commitment.id,
                                if (isOnline) EscalationTier.TIER_3 else EscalationTier.TIER_4
                            )
                            statusMessage = "Escalation Ladder Executed: $note"
                        } else {
                            statusMessage = "Error: Load sample events first!"
                        }
                    }
                }
            )

            // Step 4: Verification worker auto-close (Appointment)
            Text("Step 4: Opportunistic Verification (Appointment)", fontWeight = FontWeight.Bold)
            ElderlyButton(
                text = "4. Simulate FHIR: Appointment Completed",
                icon = Icons.Default.EventAvailable,
                containerColor = HighContrastGreen,
                onClick = {
                    coroutineScope.launch {
                        val commitment = app.commitmentRepository.getCommitmentBySourceId("fhir-appt-002")
                        if (commitment != null) {
                            app.mockFhirSource.markEventCompletedInFhir("fhir-appt-002")
                            app.commitmentEngine.autoCloseFromHealthRecord(
                                commitment.id,
                                "Mon espace santé / DMP FHIR: Appointment status = fulfilled"
                            )
                            statusMessage = "Appointment verified from health record and auto-closed with badge ✓"
                        } else {
                            statusMessage = "Error: Load sample events first!"
                        }
                    }
                }
            )

            // Step 5: Verification worker auto-close (Lab Test)
            Text("Step 5: Opportunistic Verification (Lab Test)", fontWeight = FontWeight.Bold)
            ElderlyButton(
                text = "5. Simulate FHIR: DiagnosticReport Ready",
                icon = Icons.Default.Science,
                containerColor = HighContrastGreen,
                onClick = {
                    coroutineScope.launch {
                        val commitment = app.commitmentRepository.getCommitmentBySourceId("fhir-lab-003")
                        if (commitment != null) {
                            app.mockFhirSource.markEventCompletedInFhir("fhir-lab-003")
                            app.commitmentEngine.autoCloseFromHealthRecord(
                                commitment.id,
                                "Laboratoire Biogroup: DiagnosticReport published (INR within range)"
                            )
                            statusMessage = "Lab test verified from health record and auto-closed with badge ✓"
                        } else {
                            statusMessage = "Error: Load sample events first!"
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

