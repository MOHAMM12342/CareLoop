package com.careloop.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.careloop.app.domain.model.*
import com.careloop.app.ui.components.*
import com.careloop.app.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayTimelineScreen(
    app: CareLoopApplication,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToQuickChecker: () -> Unit,
    onNavigateToManual: () -> Unit,
    onNavigateToOcr: () -> Unit,
    onNavigateToCaregivers: () -> Unit,
    onNavigateToPharmacies: () -> Unit,
    onNavigateToDemo: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToAssistant: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val isOnline by app.connectivityMonitor.isInternetReachable.collectAsState()

    val startOfDay = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }.timeInMillis
    }
    val endOfDay = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis
    }

    val commitments by app.commitmentRepository.getTodayCommitments(startOfDay, endOfDay)
        .collectAsState(initial = emptyList())

    val context = androidx.compose.ui.platform.LocalContext.current
    var showExactAlarmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val alreadyShown = app.careLoopDatabase.settingDao().getSetting("exact_alarm_dialog_shown") == "true"
        if (!app.alarmNegotiator.canScheduleExact() && !alreadyShown) {
            showExactAlarmDialog = true
        }
    }

    if (showExactAlarmDialog) {
        AlertDialog(
            onDismissRequest = {
                showExactAlarmDialog = false
                coroutineScope.launch {
                    app.careLoopDatabase.settingDao().setSetting(
                        com.careloop.app.data.local.entity.SettingEntity("exact_alarm_dialog_shown", "true")
                    )
                }
            },
            title = {
                Text(
                    text = "Precise Reminders Needed",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "CareLoop needs permission to schedule exact reminders so your medications are reminded precisely on time, even while your phone is sleeping. Without this permission, Android may delay reminders by up to 15 minutes.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextDark
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExactAlarmDialog = false
                        coroutineScope.launch {
                            app.careLoopDatabase.settingDao().setSetting(
                                com.careloop.app.data.local.entity.SettingEntity("exact_alarm_dialog_shown", "true")
                            )
                        }
                        app.alarmNegotiator.createExactAlarmSettingsIntent()?.let {
                            context.startActivity(it)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Enable in Settings", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showExactAlarmDialog = false
                        coroutineScope.launch {
                            app.careLoopDatabase.settingDao().setSetting(
                                com.careloop.app.data.local.entity.SettingEntity("exact_alarm_dialog_shown", "true")
                            )
                        }
                    }
                ) {
                    Text("Continue with Delayed Reminders")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CareLoop",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToAssistant) {
                        Icon(Icons.Default.SmartToy, contentDescription = "CareLoop Agent", tint = HighContrastGreen)
                    }
                    IconButton(onClick = onNavigateToQuickChecker) {
                        Icon(Icons.Default.TouchApp, contentDescription = "Quick Check", tint = HighContrastGreen)
                    }
                    IconButton(onClick = onNavigateToDemo) {
                        Icon(Icons.Default.PlayCircle, contentDescription = "Demo Mode", tint = PrimaryBlue)
                    }
                    IconButton(onClick = onNavigateToPharmacies) {
                        Icon(Icons.Default.LocalPharmacy, contentDescription = "Pharmacies")
                    }
                    IconButton(onClick = onNavigateToCaregivers) {
                        Icon(Icons.Default.People, contentDescription = "Caregivers")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FloatingActionButton(
                    onClick = onNavigateToOcr,
                    containerColor = DeepNavy,
                    contentColor = Color.White
                ) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Scan Prescription")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scan", fontWeight = FontWeight.Bold)
                    }
                }
                FloatingActionButton(
                    onClick = onNavigateToManual,
                    containerColor = PrimaryBlue,
                    contentColor = Color.White
                ) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OfflineStatusBanner(isOnline = isOnline)

            if (commitments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.EventAvailable,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No commitments for today",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Add one manually, scan a prescription, or launch Demo Mode.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        ElderlyButton(
                            text = "Load Sample Health Events",
                            icon = Icons.Default.CloudDownload,
                            onClick = {
                                coroutineScope.launch {
                                    val events = app.mockFhirSource.fetchEvents()
                                    for (event in events) {
                                        app.commitmentEngine.createCommitmentFromEvent(event)
                                    }
                                }
                            }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Text(
                            text = "Today's Schedule",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(commitments, key = { it.id }) { commitment ->
                        CommitmentCard(
                            commitment = commitment,
                            onCardClick = { onNavigateToDetail(commitment.id) },
                            onMarkDone = {
                                coroutineScope.launch {
                                    app.commitmentEngine.confirmCommitmentTaken(commitment.id)
                                }
                            },
                            onSnooze = {
                                coroutineScope.launch {
                                    app.commitmentEngine.snoozeCommitment(commitment.id, 15)
                                }
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
                    }
                }
            }
        }
    }
}

@Composable
fun CommitmentCard(
    commitment: Commitment,
    onCardClick: () -> Unit,
    onMarkDone: () -> Unit,
    onSnooze: () -> Unit
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(commitment.dueAt))

    val statusColor = when (commitment.state) {
        CommitmentState.COMPLETED -> HighContrastGreen
        CommitmentState.ESCALATED -> HighContrastRed
        CommitmentState.MISSED -> HighContrastRed
        CommitmentState.DUE -> WarningAmber
        CommitmentState.SNOOZED -> WarningAmber
        CommitmentState.UPCOMING -> PrimaryBlue
        CommitmentState.CANCELLED -> Color.Gray
    }

    val typeIcon = when (commitment.type) {
        CommitmentType.MEDICATION -> Icons.Default.Medication
        CommitmentType.APPOINTMENT -> Icons.Default.CalendarToday
        CommitmentType.LAB_TEST -> Icons.Default.Science
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(2.dp, statusColor.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = typeIcon,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = timeFormat,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = commitment.state.name,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = commitment.title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )

            if (commitment.verifiedOnline) {
                Spacer(modifier = Modifier.height(8.dp))
                VerifiedBadge()
            }

            if (commitment.state != CommitmentState.COMPLETED && commitment.state != CommitmentState.CANCELLED) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onMarkDone,
                        modifier = Modifier
                            .weight(1.8f)
                            .heightIn(min = 64.dp), // ≥ 64dp
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = HighContrastGreen)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Done ✓", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
                    }

                    OutlinedButton(
                        onClick = onSnooze,
                        modifier = Modifier
                            .weight(1.2f)
                            .heightIn(min = 64.dp), // ≥ 64dp
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(2.dp, BorderGray)
                    ) {
                        Text("Snooze", style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold))
                    }
                }
            }
        }
    }
}

