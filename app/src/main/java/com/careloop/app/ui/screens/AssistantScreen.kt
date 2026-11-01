package com.careloop.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import com.careloop.app.agent.policy.ProposedCommitment
import com.careloop.app.ui.components.DisclaimerBanner
import com.careloop.app.ui.components.ElderlyButton
import com.careloop.app.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatMessage(
    val sender: String, // "user" or "agent"
    val text: String,
    val pendingProposal: ProposedCommitment? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssistantScreen(
    app: CareLoopApplication,
    onNavigateBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    var inputText by remember { mutableStateOf("") }
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                sender = "agent",
                text = "Hello! I am your CareLoop assistant. You can speak or type to check your medicines, mark pills taken, delay a reminder, or schedule a doctor visit."
            )
        )
    }

    val modelStatus = app.careLoopAgentEngine.currentModelStatus

    fun sendMessage(query: String) {
        if (query.isBlank()) return
        val userMsg = ChatMessage(sender = "user", text = query)
        messages.add(userMsg)
        inputText = ""

        coroutineScope.launch {
            val response = app.careLoopAgentEngine.processUserInput(query, patientName = "Jean Dupont")
            messages.add(
                ChatMessage(
                    sender = "agent",
                    text = response.replyText,
                    pendingProposal = response.pendingProposal
                )
            )
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("CareLoop Agent", fontWeight = FontWeight.Bold)
                        Text(
                            text = modelStatus.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = HighContrastGreen
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(HighContrastGreen, shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Offline Safe", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = HighContrastGreen)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Quick senior prompt chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AssistChip(
                    onClick = { sendMessage("What is my schedule for today?") },
                    label = { Text("📋 Today", fontWeight = FontWeight.SemiBold) }
                )
                AssistChip(
                    onClick = { sendMessage("I took my medication") },
                    label = { Text("💊 Took Pill", fontWeight = FontWeight.SemiBold) }
                )
                AssistChip(
                    onClick = { sendMessage("Snooze for 15 minutes") },
                    label = { Text("⏰ Snooze", fontWeight = FontWeight.SemiBold) }
                )
                AssistChip(
                    onClick = { sendMessage("Send update to caregiver") },
                    label = { Text("✉️ Family", fontWeight = FontWeight.SemiBold) }
                )
            }

            Divider(color = Color(0xFFEEEEEE))

            // Conversation Messages List
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    ChatBubbleItem(
                        message = msg,
                        onConfirmProposal = { proposal ->
                            coroutineScope.launch {
                                app.agentPolicyLayer.confirmProposedCommitment(proposal)
                                messages.add(
                                    ChatMessage(
                                        sender = "agent",
                                        text = "✓ Confirmed & Saved: \"${proposal.title}\" is now added to your schedule."
                                    )
                                )
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        },
                        onDiscardProposal = { proposal ->
                            messages.add(
                                ChatMessage(
                                    sender = "agent",
                                    text = "Cancelled proposal for \"${proposal.title}\"."
                                )
                            )
                        }
                    )
                }
            }

            // Input Bar
            Surface(
                color = SoftBackground,
                shadowElevation = 8.dp
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { inputText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Speak or type a reminder...", fontSize = 16.sp) },
                            textStyle = MaterialTheme.typography.bodyLarge,
                            shape = RoundedCornerShape(24.dp),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (inputText.isNotBlank()) {
                                    sendMessage(inputText)
                                } else {
                                    // Simulated microphone trigger for voice-first senior demo
                                    sendMessage("I took my morning pill")
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(PrimaryBlue, CircleShape)
                        ) {
                            Icon(
                                imageVector = if (inputText.isNotBlank()) Icons.Default.Send else Icons.Default.Mic,
                                contentDescription = "Send or Speak",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    DisclaimerBanner()
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(
    message: ChatMessage,
    onConfirmProposal: (ProposedCommitment) -> Unit,
    onDiscardProposal: (ProposedCommitment) -> Unit
) {
    val isUser = message.sender == "user"

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isUser) PrimaryBlue else Color(0xFFF0F4F8),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isUser) Color.White else TextDark,
                    lineHeight = 22.sp
                )

                // Human-in-the-Loop Medical Gate: Proposed Commitment Card
                message.pendingProposal?.let { proposal ->
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFFFFF9C4),
                        border = BorderStroke(1.dp, WarningAmber),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.HealthAndSafety, contentDescription = null, tint = WarningAmber)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Patient Safety Confirmation",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = TextDark
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Item: ${proposal.title}",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = TextDark
                            )
                            val timeStr = SimpleDateFormat("EEEE, HH:mm", Locale.getDefault()).format(Date(proposal.dueAt))
                            Text(
                                text = "Scheduled: $timeStr",
                                fontSize = 14.sp,
                                color = TextDark.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { onConfirmProposal(proposal) },
                                    colors = ButtonDefaults.buttonColors(containerColor = HighContrastGreen),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Confirm ✓", fontWeight = FontWeight.Bold)
                                }
                                OutlinedButton(
                                    onClick = { onDiscardProposal(proposal) },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Discard ✗", color = HighContrastRed)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
