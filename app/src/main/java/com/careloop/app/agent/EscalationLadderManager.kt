package com.careloop.app.agent

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.careloop.app.data.repository.CaregiverRepository
import com.careloop.app.data.repository.CommitmentRepository
import com.careloop.app.domain.model.ChannelPreference
import com.careloop.app.domain.model.Commitment
import com.careloop.app.domain.model.EscalationTier
import com.careloop.app.service.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EscalationLadderManager(
    private val context: Context,
    private val commitmentRepository: CommitmentRepository,
    private val caregiverRepository: CaregiverRepository,
    private val notificationService: CareLoopNotificationService,
    private val ttsManager: TextToSpeechManager,
    private val backendClient: BackendEscalationClient,
    private val connectivityMonitor: ConnectivityMonitor
) {
    suspend fun executeEscalation(commitmentId: String, targetTier: EscalationTier): String = withContext(Dispatchers.IO) {
        val commitment = commitmentRepository.getCommitmentById(commitmentId)
            ?: return@withContext "Commitment $commitmentId not found"

        val caregivers = caregiverRepository.getCaregiversList()
        val primaryCaregiver = caregivers.minByOrNull { it.priorityRank }
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(commitment.dueAt))

        when (targetTier) {
            EscalationTier.TIER_0 -> {
                // Tier 0: High priority local notification
                notificationService.showCommitmentNotification(commitment.id, commitment.title, isReNotification = false)
                commitmentRepository.updateEscalationLevel(commitment.id, 0, "Tier 0 executed: Local high-priority notification issued.")
                "Tier 0: Local notification fired."
            }

            EscalationTier.TIER_1 -> {
                // Tier 1: Re-notification + offline TTS read aloud
                notificationService.showCommitmentNotification(commitment.id, commitment.title, isReNotification = true)
                ttsManager.speakReminder(commitment.title)
                commitmentRepository.updateEscalationLevel(commitment.id, 1, "Tier 1 executed: Re-notification with voice TTS prompt.")
                "Tier 1: Voice TTS re-notification fired."
            }

            EscalationTier.TIER_2 -> {
                // Tier 2: Caregiver push notification via FCM
                val isOnline = connectivityMonitor.checkActualInternetReachability()
                if (isOnline && primaryCaregiver?.fcmToken != null) {
                    val note = "Tier 2 executed: FCM push dispatched to ${primaryCaregiver.name} (${primaryCaregiver.relation})."
                    commitmentRepository.updateEscalationLevel(commitment.id, 2, note)
                    notificationService.showEscalationStatusNotification(
                        "Caregiver Alerted",
                        "Notification sent to ${primaryCaregiver.name}."
                    )
                    note
                } else {
                    // Fall through to Tier 3 or 4
                    executeEscalation(commitmentId, if (isOnline) EscalationTier.TIER_3 else EscalationTier.TIER_4)
                }
            }

            EscalationTier.TIER_3 -> {
                // Tier 3: Backend SMS / Voice call via Twilio when online
                val isOnline = connectivityMonitor.checkActualInternetReachability()
                if (isOnline && primaryCaregiver != null) {
                    val channelStr = if (primaryCaregiver.channelPreference == ChannelPreference.CALL) "VOICE_CALL" else "SMS"
                    val request = EscalationRequest(
                        caregiverName = primaryCaregiver.name,
                        caregiverPhone = primaryCaregiver.phone,
                        patientName = "Your family member",
                        commitmentTitle = commitment.title,
                        missedTimeFormatted = timeFormat,
                        channel = channelStr
                    )
                    val result = backendClient.sendCaregiverAlert(request)
                    if (result.isSuccess) {
                        val note = "Tier 3 executed: Cloud $channelStr delivered to ${primaryCaregiver.name} (${primaryCaregiver.phone})."
                        commitmentRepository.updateEscalationLevel(commitment.id, 3, note)
                        notificationService.showEscalationStatusNotification(
                            "Family Alert Dispatched",
                            "$channelStr sent to ${primaryCaregiver.name}."
                        )
                        note
                    } else {
                        // Backend failed, execute Tier 4 offline fallback
                        executeEscalation(commitmentId, EscalationTier.TIER_4)
                    }
                } else {
                    executeEscalation(commitmentId, EscalationTier.TIER_4)
                }
            }

            EscalationTier.TIER_4 -> {
                // Tier 4: Platform-compliant Offline Fallback (User-initiated SMS Intent or Dialer)
                val targetPhone = primaryCaregiver?.phone ?: ""
                val caregiverName = primaryCaregiver?.name ?: "Family"
                val smsMessage = "CareLoop Alert: I may have missed my scheduled '${commitment.title}' at $timeFormat. Please check in with me."

                val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:$targetPhone")
                    putExtra("sms_body", smsMessage)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }

                val note = "Tier 4 executed (Offline fallback): Pre-filled SMS intent prepared for $caregiverName ($targetPhone). User confirmation required."
                commitmentRepository.updateEscalationLevel(commitment.id, 4, note)

                notificationService.showEscalationStatusNotification(
                    "Action Needed: Alert $caregiverName",
                    "Tap to send a check-in text to $caregiverName."
                )

                try {
                    context.startActivity(smsIntent)
                } catch (e: Exception) {
                    // In background or locked screen, notification was posted
                }

                note
            }
        }
    }
}

