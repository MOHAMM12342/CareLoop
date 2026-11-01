package com.careloop.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.careloop.app.CareLoopApplication
import com.careloop.app.domain.model.AdherenceLog
import com.careloop.app.domain.model.Commitment
import com.careloop.app.ui.components.VerifiedBadge
import com.careloop.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommitmentDetailScreen(
    app: CareLoopApplication,
    commitmentId: String,
    onNavigateBack: () -> Unit
) {
    var commitment by remember { mutableStateOf<Commitment?>(null) }
    val logs by app.commitmentRepository.getLogsForCommitment(commitmentId).collectAsState(initial = emptyList())

    LaunchedEffect(commitmentId) {
        commitment = app.commitmentRepository.getCommitmentById(commitmentId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Commitment Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        commitment?.let { item ->
            val dueFormat = SimpleDateFormat("EEEE, d MMMM yyyy 'at' HH:mm", Locale.getDefault()).format(Date(item.dueAt))

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                        border = BorderStroke(1.dp, BorderGray)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = item.type.name.replace("_", " "),
                                style = MaterialTheme.typography.bodyMedium,
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Scheduled For: $dueFormat",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Source: ${item.verificationMethod.name.replace("_", " ")}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                            if (item.verifiedOnline) {
                                Spacer(modifier = Modifier.height(10.dp))
                                VerifiedBadge()
                            }
                        }
                    }
                }

                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.History, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Adherence & Escalation Audit Log",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (logs.isEmpty()) {
                    item {
                        Text(
                            text = "No log events recorded yet.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextMuted
                        )
                    }
                } else {
                    items(logs, key = { it.id }) { log ->
                        AuditLogItem(log)
                    }
                }
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun AuditLogItem(log: AdherenceLog) {
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(log.actedAt))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = log.action.name,
                        fontWeight = FontWeight.Bold,
                        color = if (log.action.name == "CONFIRMED") HighContrastGreen else WarningAmber,
                        fontSize = 15.sp
                    )
                    Text(
                        text = timeFormat,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
                if (log.note.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = log.note,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDark
                    )
                }
            }
        }
    }
}

