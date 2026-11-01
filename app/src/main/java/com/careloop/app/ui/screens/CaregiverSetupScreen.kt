package com.careloop.app.ui.screens

import androidx.compose.foundation.BorderStroke
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
import com.careloop.app.domain.model.Caregiver
import com.careloop.app.domain.model.ChannelPreference
import com.careloop.app.ui.components.ElderlyButton
import com.careloop.app.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverSetupScreen(
    app: CareLoopApplication,
    onNavigateBack: () -> Unit,
    onNavigateToTimeline: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val caregivers by app.caregiverRepository.getAllCaregivers().collectAsState(initial = emptyList())

    var showAddDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var relation by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var channel by remember { mutableStateOf(ChannelPreference.SMS) }
    var consentChecked by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Family & Caregivers", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToTimeline) {
                        Icon(Icons.Default.Timeline, contentDescription = "Caregiver Timeline", tint = PrimaryBlue)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = "Add Caregiver")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                color = Color(0xFFE8F5E9),
                border = BorderStroke(1.dp, HighContrastGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = HighContrastGreen, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "GDPR-Compliant Family Escalation: Contacts receive alerts only when commitments slip and consent has been logged.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDark
                    )
                }
            }

            if (caregivers.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No caregivers configured yet. Tap + to add a family member.")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(caregivers, key = { it.id }) { caregiver ->
                        CaregiverCard(
                            caregiver = caregiver,
                            onDelete = {
                                coroutineScope.launch {
                                    app.caregiverRepository.deleteCaregiver(caregiver)
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Family Member / Caregiver", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = relation,
                        onValueChange = { relation = it },
                        label = { Text("Relation (e.g. Sibling, Daughter)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = consentChecked,
                            onCheckedChange = { consentChecked = it }
                        )
                        Text(
                            text = "Consent granted by caregiver to receive escalation alerts.",
                            fontSize = 13.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank() && consentChecked) {
                            coroutineScope.launch {
                                val nextRank = (caregivers.maxOfOrNull { it.priorityRank } ?: 0) + 1
                                app.caregiverRepository.insertCaregiver(
                                    Caregiver(
                                        id = "cg-${System.currentTimeMillis()}",
                                        name = name,
                                        relation = relation,
                                        phone = phone,
                                        priorityRank = nextRank,
                                        channelPreference = channel,
                                        consentGrantedAt = System.currentTimeMillis()
                                    )
                                )
                                showAddDialog = false
                                name = ""
                                relation = ""
                                phone = ""
                                consentChecked = false
                            }
                        }
                    },
                    enabled = name.isNotBlank() && phone.isNotBlank() && consentChecked
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun CaregiverCard(
    caregiver: Caregiver,
    onDelete: () -> Unit
) {
    val consentDate = caregiver.consentGrantedAt?.let {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it))
    } ?: "Pending"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceWhite),
        border = BorderStroke(1.dp, BorderGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = PrimaryBlue.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Priority #${caregiver.priorityRank}",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${caregiver.name} (${caregiver.relation})",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Phone: ${caregiver.phone} • Mode: ${caregiver.channelPreference.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Consent Verified: $consentDate",
                    fontSize = 12.sp,
                    color = HighContrastGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = HighContrastRed)
            }
        }
    }
}

