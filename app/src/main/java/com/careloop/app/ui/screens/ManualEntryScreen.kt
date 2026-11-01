package com.careloop.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.careloop.app.CareLoopApplication
import com.careloop.app.domain.model.CommitmentType
import com.careloop.app.ui.components.ElderlyButton
import com.careloop.app.ui.theme.HighContrastGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualEntryScreen(
    app: CareLoopApplication,
    onNavigateBack: () -> Unit,
    onCommitmentCreated: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedType by remember { mutableStateOf(CommitmentType.MEDICATION) }
    var title by remember { mutableStateOf("") }
    var minutesFromNow by remember { mutableStateOf("5") }
    var notes by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Health Commitment", fontWeight = FontWeight.Bold) },
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Commitment Type",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CommitmentType.values().forEach { type ->
                    val isSelected = selectedType == type
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedType = type },
                        label = {
                            Text(
                                text = type.name.replace("_", " "),
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Text(
                text = "Title / Instruction",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge,
                placeholder = {
                    Text(
                        when (selectedType) {
                            CommitmentType.MEDICATION -> "e.g. Take Tahor 20mg"
                            CommitmentType.APPOINTMENT -> "e.g. Ophthalmology Checkup"
                            CommitmentType.LAB_TEST -> "e.g. Blood Test (Fasting Glucose)"
                        }
                    )
                }
            )

            Text(
                text = "Due in (minutes from now)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = minutesFromNow,
                onValueChange = { minutesFromNow = it.filter { ch -> ch.isDigit() } },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleLarge,
                placeholder = { Text("5") }
            )

            Text(
                text = "Additional Notes (optional)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge,
                placeholder = { Text("e.g. Take with a glass of water") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ElderlyButton(
                text = "Save Commitment ✓",
                icon = Icons.Default.Check,
                containerColor = HighContrastGreen,
                enabled = title.isNotBlank(),
                onClick = {
                    coroutineScope.launch {
                        val minutes = minutesFromNow.toLongOrNull() ?: 5
                        val dueAt = System.currentTimeMillis() + (minutes * 60 * 1000)
                        val event = app.manualEntrySource.createEvent(selectedType, title, dueAt, notes)
                        val commitment = app.commitmentEngine.createCommitmentFromEvent(event)
                        app.alarmNegotiator.scheduleCommitmentReminder(commitment.id, commitment.title, commitment.dueAt)
                        onCommitmentCreated(commitment.id)
                    }
                }
            )
        }
    }
}

