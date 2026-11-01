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
import com.careloop.app.feature.ocr.ParsedPrescriptionResult
import com.careloop.app.ui.components.DisclaimerBanner
import com.careloop.app.ui.components.ElderlyButton
import com.careloop.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OcrPrescriptionScreen(
    app: CareLoopApplication,
    onNavigateBack: () -> Unit,
    onCommitmentCreated: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var rawInputText by remember {
        mutableStateOf(
            "Dr. Laurent\nPrendre Kardegic 75mg sachet\n1 sachet par jour le matin pendant 30 jours\nAprès le petit-déjeuner"
        )
    }

    var parsedResult by remember { mutableStateOf<ParsedPrescriptionResult?>(null) }

    // Editable confirmation fields (Human-in-the-loop safety gate)
    var confirmedMedName by remember { mutableStateOf("") }
    var confirmedStrength by remember { mutableStateOf("") }
    var confirmedForm by remember { mutableStateOf("") }
    var confirmedFrequency by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val result = app.ocrParser.parsePrescriptionText(rawInputText)
        parsedResult = result
        confirmedMedName = result.medicationName
        confirmedStrength = result.strength
        confirmedForm = result.dosageForm
        confirmedFrequency = result.frequency
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prescription Review", fontWeight = FontWeight.Bold) },
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Safety banner
            Surface(
                color = Color(0xFFFFF8E1),
                border = BorderStroke(1.dp, WarningAmber),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Safety Verification Gate: Check every field below before adding to your medication schedule.",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                }
            }

            parsedResult?.let { result ->
                if (result.confidenceScore < 0.7f) {
                    Surface(
                        color = Color(0xFFFFEBEE),
                        border = BorderStroke(1.dp, HighContrastRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = HighContrastRed)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Low OCR confidence: Please verify the medication name manually.",
                                color = HighContrastRed,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Text(
                text = "Medication Name",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = confirmedMedName,
                onValueChange = { confirmedMedName = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge,
                placeholder = { Text("e.g. Kardegic") },
                singleLine = true
            )

            Text(
                text = "Dosage Strength",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = confirmedStrength,
                onValueChange = { confirmedStrength = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge,
                placeholder = { Text("e.g. 75mg") },
                singleLine = true
            )

            Text(
                text = "Form & Schedule",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = confirmedFrequency,
                onValueChange = { confirmedFrequency = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = { Text("e.g. Once daily at 08:00") },
                singleLine = false
            )

            Spacer(modifier = Modifier.height(10.dp))

            ElderlyButton(
                text = "Confirm & Save Commitment ✓",
                icon = Icons.Default.CheckCircle,
                containerColor = HighContrastGreen,
                enabled = confirmedMedName.isNotBlank(),
                onClick = {
                    coroutineScope.launch {
                        val title = "Take $confirmedMedName $confirmedStrength"
                        val event = app.ocrPrescriptionSource.createEventFromOcr(
                            title = title,
                            dueAt = System.currentTimeMillis() + (60 * 1000), // Due in 1 minute for immediate test
                            instructions = "$confirmedForm - $confirmedFrequency"
                        )
                        val commitment = app.commitmentEngine.createCommitmentFromEvent(event)
                        app.alarmNegotiator.scheduleCommitmentReminder(commitment.id, commitment.title, commitment.dueAt)
                        onCommitmentCreated(commitment.id)
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))
            DisclaimerBanner()
        }
    }
}

