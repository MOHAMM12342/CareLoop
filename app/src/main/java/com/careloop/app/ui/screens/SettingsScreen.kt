package com.careloop.app.ui.screens

import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.careloop.app.CareLoopApplication
import com.careloop.app.ui.components.DisclaimerBanner
import com.careloop.app.ui.components.ElderlyButton
import com.careloop.app.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    app: CareLoopApplication,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var canScheduleExact by remember { mutableStateOf(app.alarmNegotiator.canScheduleExact()) }
    var biometricEnabled by remember { mutableStateOf(true) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings & Privacy", fontWeight = FontWeight.Bold) },
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
            // Alarm Capability Negotiation Section
            Text(
                text = "Alarm Capabilities (Reliability)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Exact Alarm Permission",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (canScheduleExact) HighContrastGreen.copy(alpha = 0.15f) else HighContrastRed.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = if (canScheduleExact) "ACTIVE" else "DENIED",
                                color = if (canScheduleExact) HighContrastGreen else HighContrastRed,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (canScheduleExact) {
                            "Reminders fire precisely on time, waking the device even from deep sleep."
                        } else {
                            "Exact alarms denied: Reminders will use WorkManager fallback (±15 min window). Enable exact alarms for life-critical timing."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )

                    if (!canScheduleExact) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                app.alarmNegotiator.createExactAlarmSettingsIntent()?.let {
                                    context.startActivity(it)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) {
                            Text("Open System Settings to Allow")
                        }
                    }
                }
            }

            // Quick-Tap Screen Extension Section
            Text(
                text = "Fast Confirmation Extension",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Triple-Tap Screen Shortcut",
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Tap the screen 3 times rapidly to launch the Giant Commitment Checker directly without logging in or navigating menus.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = true,
                            onCheckedChange = { /* toggle multi-tap shortcut */ }
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.TouchApp, contentDescription = null, tint = PrimaryBlue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Also available via Android Quick Settings Tile in your notification shade.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextDark
                        )
                    }
                }
            }

            // Privacy & Encryption Section
            Text(
                text = "Security & Encryption",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = HighContrastGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SQLCipher AES-256 Active",
                            fontWeight = FontWeight.Bold,
                            color = HighContrastGreen,
                            fontSize = 16.sp
                        )
                    }
                    Text(
                        text = "Your prescriptions, medical schedule, and family phone numbers are 100% encrypted on this phone. There is no cloud database or analytics tracker.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDark
                    )

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Biometric Lock (Fingerprint / Face)", fontWeight = FontWeight.SemiBold)
                        Switch(
                            checked = biometricEnabled,
                            onCheckedChange = { biometricEnabled = it }
                        )
                    }
                }
            }

            // Danger Zone / GDPR Data Rights
            Text(
                text = "Data Management (GDPR)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedButton(
                onClick = { /* In production: write encrypted SQLite backup / JSON export */ },
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export My Health Data (JSON)", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showDeleteConfirmDialog = true },
                modifier = Modifier.fillMaxWidth().heightIn(min = 54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HighContrastRed)
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete All Data", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))
            DisclaimerBanner()
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete All Data?", fontWeight = FontWeight.Bold) },
            text = {
                Text("This permanently deletes all health commitments, reminders, and caregiver contacts stored in the encrypted database.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            app.commitmentRepository.deleteAll()
                            app.caregiverRepository.deleteAll()
                            showDeleteConfirmDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = HighContrastRed)
                ) {
                    Text("Delete Permanently")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

