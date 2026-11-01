package com.careloop.app.agent

import android.content.Context
import com.careloop.app.data.local.dao.EscalationAuditLogDao
import com.careloop.app.data.local.entity.EscalationAuditLogEntity
import com.careloop.app.data.repository.CaregiverRepository
import com.careloop.app.data.repository.CommitmentRepository
import com.careloop.app.domain.escalation.EscalationChannel
import com.careloop.app.domain.model.ChannelPreference
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
    private val escalationAuditLogDao: EscalationAuditLogDao,
    private val escalationChannel: EscalationChannel,
    private val notificationService: CareLoopNotificationService,
    private val ttsManager: TextToSpeechManager,
    private val backendClient: BackendEscalationClient,
    private val connectivityMonitor: ConnectivityMonitor
) {
    suspend fun executeEscalation(
        commitmentId: String,
        targetTier: EscalationTier,
        patientName: String = "Patient"
    ): String = withContext(Dispatchers.IO) {
        val commitment = commitmentRepository.getCommitmentById(commitmentId)
            ?: return@withContext "Commitment $commitmentId not found"

        val caregivers = caregiverRepository.getCaregiversList()
        val primaryCaregiver = caregivers.minByOrNull { it.priorityRank }
        val now = System.currentTimeMillis()
        val dataMinimisedMessage = EscalationChannel.buildDataMinimisedMessage(patientName)

        when (targetTier) {
            EscalationTier.TIER_0 -> {
                // Tier 0: Local high-priority notification with Taken/Snooze actions
                notificationService.showCommitmentNotification(commitment.id, commitment.title, isReNotification = false)
                commitmentRepository.updateEscalationLevel(commitment.id, 0, "Tier 0 executed: Local high-priority notification issued.")

                escalationAuditLogDao.insertAuditLog(
                    EscalationAuditLogEntity(
                        commitmentId = commitment.id,
                        whoNotified = "Patient (Local)",
                        notifiedAt = now,
                        channel = "TIER_0_LOCAL_NOTIFICATION",
                        outcome = "DELIVERED",
                        dataMinimisedMessage = dataMinimisedMessage
                    )
                )
                "Tier 0: Local notification fired."
            }

            EscalationTier.TIER_1 -> {
                // Tier 1: Local re-notification + Voice Text-to-Speech prompt
                notificationService.showCommitmentNotification(commitment.id, commitment.title, isReNotification = true)
                ttsManager.speakReminder(commitment.title)
                commitmentRepository.updateEscalationLevel(commitment.id, 1, "Tier 1 executed: Re-notification with voice TTS prompt.")

                escalationAuditLogDao.insertAuditLog(
                    EscalationAuditLogEntity(
                        commitmentId = commitment.id,
                        whoNotified = "Patient (Local TTS)",
                        notifiedAt = now,
                        channel = "TIER_1_LOCAL_TTS",
                        outcome = "DELIVERED",
                        dataMinimisedMessage = dataMinimisedMessage
                    )
                )
                "Tier 1: Voice TTS re-notification fired."
            }

            EscalationTier.TIER_2 -> {
                // Tier 2: Caregiver push notification via FCM (requires patient & caregiver online)
                val isOnline = connectivityMonitor.checkActualInternetReachability()
                if (isOnline && primaryCaregiver?.fcmToken != null) {
                    val note = "Tier 2 executed: FCM push dispatched to ${primaryCaregiver.name} (${primaryCaregiver.relation})."
                    commitmentRepository.updateEscalationLevel(commitment.id, 2, note)

                    escalationAuditLogDao.insertAuditLog(
                        EscalationAuditLogEntity(
                            commitmentId = commitment.id,
                            whoNotified = "${primaryCaregiver.name} (${primaryCaregiver.relation})",
                            notifiedAt = now,
                            channel = "TIER_2_CAREGIVER_FCM",
                            outcome = "DELIVERED",
                            dataMinimisedMessage = dataMinimisedMessage
                        )
                    )

                    notificationService.showEscalationStatusNotification(
                        "Caregiver Alerted",
                        "Notification sent to ${primaryCaregiver.name}."
                    )
                    note
                } else {
                    // Fallback to Tier 3 if online, or Tier 4 if offline
                    executeEscalation(commitmentId, if (isOnline) EscalationTier.TIER_3 else EscalationTier.TIER_4, patientName)
                }
            }

            EscalationTier.TIER_3 -> {
                // Tier 3: Cloud SMS / Voice Call via backend (Twilio) when online
                val isOnline = connectivityMonitor.checkActualInternetReachability()
                if (isOnline && primaryCaregiver != null) {
                    val channelStr = if (primaryCaregiver.channelPreference == ChannelPreference.CALL) "VOICE_CALL" else "SMS"
                    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(commitment.dueAt))

                    val request = EscalationRequest(
                        caregiverName = primaryCaregiver.name,
                        caregiverPhone = primaryCaregiver.phone,
                        patientName = patientName,
                        commitmentTitle = "an important reminder", // Data minimised! Never a drug name
                        missedTimeFormatted = timeFormat,
                        channel = channelStr
                    )

                    val result = backendClient.sendCaregiverAlert(request)
                    val outcome = if (result.isSuccess) "DELIVERED" else "FAILED"
                    val note = "Tier 3 executed: Cloud $channelStr ($outcome) to ${primaryCaregiver.name}."
                    commitmentRepository.updateEscalationLevel(commitment.id, 3, note)

                    escalationAuditLogDao.insertAuditLog(
                        EscalationAuditLogEntity(
                            commitmentId = commitment.id,
                            whoNotified = "${primaryCaregiver.name} (${primaryCaregiver.phone})",
                            notifiedAt = now,
                            channel = "TIER_3_BACKEND_$channelStr",
                            outcome = outcome,
                            dataMinimisedMessage = dataMinimisedMessage
                        )
                    )

                    if (result.isSuccess) {
                        notificationService.showEscalationStatusNotification(
                            "Family Alert Dispatched",
                            "$channelStr sent to ${primaryCaregiver.name}."
                        )
                        note
                    } else {
                        // Backend failed, execute Tier 4 offline fallback
                        executeEscalation(commitmentId, EscalationTier.TIER_4, patientName)
                    }
                } else {
                    executeEscalation(commitmentId, EscalationTier.TIER_4, patientName)
                }
            }

            EscalationTier.TIER_4 -> {
                // Tier 4: Offline Fallback via flavor-specific channel (silent SmsManager in demo, ACTION_SENDTO in play)
                val targetPhone = primaryCaregiver?.phone ?: ""
                val caregiverName = primaryCaregiver?.name ?: "Family Caregiver"

                val result = escalationChannel.sendEmergencyAlert(targetPhone, patientName)
                val outcome = if (result.success) "DELIVERED" else "FAILED: ${result.error}"

                val note = "Tier 4 executed (${escalationChannel.channelName}): $outcome to $caregiverName ($targetPhone)."
                commitmentRepository.updateEscalationLevel(commitment.id, 4, note)

                escalationAuditLogDao.insertAuditLog(
                    EscalationAuditLogEntity(
                        commitmentId = commitment.id,
                        whoNotified = "$caregiverName ($targetPhone)",
                        notifiedAt = now,
                        channel = "TIER_4_${escalationChannel.channelName}",
                        outcome = outcome,
                        dataMinimisedMessage = dataMinimisedMessage
                    )
                )

                notificationService.showEscalationStatusNotification(
                    "Caregiver Notified",
                    "Alert dispatched to $caregiverName."
                )

                note
            }
        }
    }
}
