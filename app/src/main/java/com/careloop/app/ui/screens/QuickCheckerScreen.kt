package com.careloop.app.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.careloop.app.CareLoopApplication
import com.careloop.app.domain.model.Commitment
import com.careloop.app.domain.model.CommitmentState
import com.careloop.app.ui.components.VerifiedBadge
import com.careloop.app.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickCheckerScreen(
    app: CareLoopApplication,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var dueCommitment by remember { mutableStateOf<Commitment?>(null) }
    var isConfirmed by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    fun triggerHapticFeedback() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(150)
            }
        }
    }

    LaunchedEffect(Unit) {
        // Query commitments to find currently DUE, SNOOZED, or UPCOMING within the day
        val startOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
        val endOfDay = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis

        // Find the first commitment needing attention
        val allToday = app.careLoopDatabase.commitmentDao().getCommitmentsInStates(
            listOf(CommitmentState.DUE, CommitmentState.SNOOZED, CommitmentState.MISSED, CommitmentState.UPCOMING)
        ).map { entity ->
            Commitment(
                id = entity.id,
                patientId = entity.patientId,
                type = entity.type,
                title = entity.title,
                dueAt = entity.dueAt,
                state = entity.state,
                verificationMethod = entity.verificationMethod,
                sourceEventId = entity.sourceEventId,
                escalationLevel = entity.escalationLevel,
                completedAt = entity.completedAt,
                createdAt = entity.createdAt,
                notes = entity.notes,
                verifiedOnline = entity.verifiedOnline
            )
        }

        // Prioritize DUE > MISSED > SNOOZED > nearest UPCOMING
        dueCommitment = allToday.firstOrNull { it.state == CommitmentState.DUE }
            ?: allToday.firstOrNull { it.state == CommitmentState.MISSED }
            ?: allToday.firstOrNull { it.state == CommitmentState.SNOOZED }
            ?: allToday.minByOrNull { it.dueAt }

        isLoading = false

        // Speak prompt via TTS for low-vision/elderly assistance
        dueCommitment?.let {
            app.ttsManager.speakReminder("Quick Check: Did you take ${it.title}?")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quick Commitment Check", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(32.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepNavy,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = SoftBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(48.dp))
            } else if (isConfirmed) {
                // Success confirmation screen
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = HighContrastGreen,
                        modifier = Modifier.size(110.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(64.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Recorded Successfully!",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = HighContrastGreen,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Your dose is confirmed in your encrypted ledger and your family has been updated.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = TextDark
                    )
                }
            } else if (dueCommitment != null) {
                val item = dueCommitment!!
                val dueTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(item.dueAt))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = PrimaryBlue.copy(alpha = 0.12f),
                        border = BorderStroke(2.dp, PrimaryBlue)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.TouchApp, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Triple-Tap Quick Confirmation",
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Text(
                        text = "Scheduled for $dueTime",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextMuted,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 34.sp,
                            lineHeight = 42.sp
                        ),
                        textAlign = TextAlign.Center,
                        color = TextDark
                    )

                    if (item.verifiedOnline) {
                        VerifiedBadge()
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Giant Single-Action Button for elderly ease (130dp height)
                    Button(
                        onClick = {
                            triggerHapticFeedback()
                            coroutineScope.launch {
                                app.commitmentEngine.confirmCommitmentTaken(item.id, "Confirmed via Quick-Tap Screen Checker")
                                app.notificationService.dismissNotification(item.id)
                                app.alarmNegotiator.cancelCommitmentReminder(item.id)
                                app.ttsManager.speakReminder("Dose confirmed! Thank you.")
                                isConfirmed = true
                                delay(2200)
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HighContrastGreen,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "YES, I TOOK IT ✓",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 26.sp
                                )
                            )
                        }
                    }

                    // Secondary Snooze button
                    OutlinedButton(
                        onClick = {
                            triggerHapticFeedback()
                            coroutineScope.launch {
                                app.commitmentEngine.snoozeCommitment(item.id, 15)
                                app.notificationService.dismissNotification(item.id)
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, BorderGray)
                    ) {
                        Icon(Icons.Default.Snooze, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Snooze 15 Minutes",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
            } else {
                // No commitment due right now
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = null,
                        tint = HighContrastGreen,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "All Clear!",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = HighContrastGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "You have no pending or overdue health commitments right now.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.height(64.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("Back to App", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

