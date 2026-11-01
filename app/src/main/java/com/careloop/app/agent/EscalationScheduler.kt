package com.careloop.app.agent

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.careloop.app.domain.model.Commitment
import com.careloop.app.domain.model.CommitmentType
import com.careloop.app.domain.model.EscalationTier
import com.careloop.app.receiver.AlarmReceiver

data class EscalationMilestone(
    val commitmentId: String,
    val targetTier: EscalationTier,
    val triggerAt: Long,
    val description: String
)

class EscalationScheduler(
    private val context: Context,
    private val alarmNegotiator: AlarmCapabilityNegotiator
) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Computes the escalation schedule for a commitment.
     * @param isDemoMode If true, intervals are accelerated (seconds instead of minutes) for stage demos.
     */
    fun computeMilestones(commitment: Commitment, isDemoMode: Boolean = false): List<EscalationMilestone> {
        val t = commitment.dueAt
        val minuteMs = if (isDemoMode) 1000L else 60_000L // 1s per minute in demo mode!
        val hourMs = minuteMs * 60

        return when (commitment.type) {
            CommitmentType.MEDICATION -> {
                listOf(
                    EscalationMilestone(
                        commitmentId = commitment.id,
                        targetTier = EscalationTier.TIER_0,
                        triggerAt = t,
                        description = "Initial alarm & local notification"
                    ),
                    EscalationMilestone(
                        commitmentId = commitment.id,
                        targetTier = EscalationTier.TIER_1,
                        triggerAt = t + (15 * minuteMs),
                        description = "T+15m: Re-notification with voice TTS prompt"
                    ),
                    EscalationMilestone(
                        commitmentId = commitment.id,
                        targetTier = EscalationTier.TIER_2,
                        triggerAt = t + (30 * minuteMs),
                        description = "T+30m: First caregiver notification (FCM online / SMS fallback)"
                    ),
                    EscalationMilestone(
                        commitmentId = commitment.id,
                        targetTier = EscalationTier.TIER_3,
                        triggerAt = t + (45 * minuteMs),
                        description = "T+45m: Second caregiver escalation (Cloud Voice Call if online / SMS fallback)"
                    )
                )
            }

            CommitmentType.APPOINTMENT -> {
                val now = System.currentTimeMillis()
                val list = mutableListOf<EscalationMilestone>()

                val tMinus24h = t - (24 * hourMs)
                if (tMinus24h > now) {
                    list.add(
                        EscalationMilestone(
                            commitmentId = commitment.id,
                            targetTier = EscalationTier.TIER_0,
                            triggerAt = tMinus24h,
                            description = "T-24h: Advance appointment reminder"
                        )
                    )
                }

                val tMinus1h = t - hourMs
                if (tMinus1h > now) {
                    list.add(
                        EscalationMilestone(
                            commitmentId = commitment.id,
                            targetTier = EscalationTier.TIER_0,
                            triggerAt = tMinus1h,
                            description = "T-1h: Upcoming appointment departure alert"
                        )
                    )
                }

                list.add(
                    EscalationMilestone(
                        commitmentId = commitment.id,
                        targetTier = EscalationTier.TIER_1,
                        triggerAt = t + (2 * hourMs),
                        description = "T+2h: Attendance check ('Did you attend?') followed by escalation"
                    )
                )
                list
            }

            CommitmentType.LAB_TEST -> {
                listOf(
                    EscalationMilestone(
                        commitmentId = commitment.id,
                        targetTier = EscalationTier.TIER_0,
                        triggerAt = t,
                        description = "Lab test due notification"
                    ),
                    EscalationMilestone(
                        commitmentId = commitment.id,
                        targetTier = EscalationTier.TIER_1,
                        triggerAt = t + (24 * hourMs),
                        description = "T+24h: Follow-up reminder for pending lab test"
                    )
                )
            }
        }
    }

    /**
     * Schedules the next escalation milestone.
     */
    fun scheduleMilestone(milestone: EscalationMilestone) {
        val now = System.currentTimeMillis()
        if (milestone.triggerAt <= now) return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = AlarmReceiver.ACTION_COMMITMENT_ESCALATE
            putExtra(AlarmReceiver.EXTRA_COMMITMENT_ID, milestone.commitmentId)
            putExtra(AlarmReceiver.EXTRA_ESCALATION_LEVEL, milestone.targetTier.level)
        }

        val requestCode = (milestone.commitmentId + "_tier_" + milestone.targetTier.level).hashCode()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (alarmNegotiator.canScheduleExact()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, milestone.triggerAt, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, milestone.triggerAt, pendingIntent)
            }
        }
    }

    /**
     * Cancels all escalation milestones for a commitment (e.g. when Taken or Cancelled).
     */
    fun cancelAllMilestones(commitmentId: String) {
        for (tier in EscalationTier.values()) {
            val intent = Intent(context, AlarmReceiver::class.java).apply {
                action = AlarmReceiver.ACTION_COMMITMENT_ESCALATE
            }
            val requestCode = (commitmentId + "_tier_" + tier.level).hashCode()
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
            }
        }
    }
}

