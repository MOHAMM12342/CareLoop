package com.careloop.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.careloop.app.CareLoopApplication
import com.careloop.app.data.local.entity.EscalationAuditLogEntity
import com.careloop.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverTimelineScreen(
    app: CareLoopApplication,
    onNavigateBack: () -> Unit
) {
    val auditLogs by app.careLoopDatabase.escalationAuditLogDao().getAllAuditLogs().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Caregiver Activity Timeline", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Tamper-Proof Escalation Ledger",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Immutable audit log of all caregiver alerts, delivery channels, and data-minimised messages.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (auditLogs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No escalation events logged yet.", style = MaterialTheme.typography.bodyLarge, color = TextMuted)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(auditLogs, key = { it.id }) { log ->
                        EscalationAuditLogCard(log)
                    }
                }
            }
        }
    }
}

@Composable
fun EscalationAuditLogCard(log: EscalationAuditLogEntity) {
    val dateFormat = SimpleDateFormat("dd MMM HH:mm:ss", Locale.getDefault()).format(Date(log.notifiedAt))
    val isDelivered = log.outcome.startsWith("DELIVERED") || log.outcome == "PREPARED"
    val accentColor = if (isDelivered) PrimaryBlue else HighContrastRed

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.5.dp, accentColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = log.channel.replace("_", " "),
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = dateFormat,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Target: ${log.whoNotified} • Outcome: ${log.outcome}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = Color(0xFFF1F8E9),
                shape = RoundedCornerShape(6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Shield, contentDescription = null, tint = HighContrastGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "\"${log.dataMinimisedMessage}\"",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = HighContrastGreen
                    )
                }
            }
        }
    }
}
