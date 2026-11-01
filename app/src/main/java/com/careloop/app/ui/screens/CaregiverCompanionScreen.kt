package com.careloop.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.careloop.app.CareLoopApplication
import com.careloop.app.data.local.entity.EscalationAuditLogEntity
import com.careloop.app.domain.model.AdherenceAction
import com.careloop.app.domain.model.CommitmentState
import com.careloop.app.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverCompanionScreen(
    app: CareLoopApplication,
    pairingToken: String = "PAIR-7X9B-4K21",
    patientPhone: String = "+33698765432",
    patientName: String = "Jean (Father)",
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val auditLogs by app.careLoopDatabase.escalationAuditLogDao().getAllAuditLogs().collectAsState(initial = emptyList())
    var isHandled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CareLoop Family Companion", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepNavy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Patient Status Overview Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(2.dp, if (isHandled) HighContrastGreen else WarningAmber),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = patientName,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrimaryBlue.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "Token: $pairingToken",
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (isHandled) {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = HighContrastGreen)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Alert marked as handled. The patient app and family ledger have been notified.",
                                    color = HighContrastGreen,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        Text(
                            text = "Status: $patientName has not confirmed an important scheduled reminder.",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = WarningAmber
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action 1: Call Patient
                    Button(
                        onClick = {
                            val callIntent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:$patientPhone")
                            }
                            context.startActivity(callIntent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 60.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Call Patient ($patientPhone)", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action 2: Mark Handled
                    OutlinedButton(
                        onClick = {
                            isHandled = true
                            coroutineScope.launch {
                                app.careLoopDatabase.escalationAuditLogDao().insertAuditLog(
                                    EscalationAuditLogEntity(
                                        commitmentId = "active",
                                        whoNotified = "Caregiver via Companion App",
                                        notifiedAt = System.currentTimeMillis(),
                                        channel = "COMPANION_APP",
                                        outcome = "HANDLED_BY_FAMILY",
                                        dataMinimisedMessage = "Family member marked alert as handled."
                                    )
                                )
                            }
                        },
                        enabled = !isHandled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 54.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(2.dp, HighContrastGreen)
                    ) {
                        Icon(Icons.Default.TaskAlt, contentDescription = null, tint = HighContrastGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Mark Handled", color = HighContrastGreen, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Ledger / Activity Section
            Text(
                text = "Recent Family Escalation Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            if (auditLogs.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("No activity recorded.", color = TextMuted)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(auditLogs.take(5), key = { it.id }) { log ->
                        val date = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault()).format(Date(log.notifiedAt))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                            border = BorderStroke(1.dp, BorderGray)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = log.channel.replace("_", " "), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(text = log.dataMinimisedMessage, color = TextMuted, fontSize = 12.sp)
                                }
                                Text(text = date, color = TextMuted, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

