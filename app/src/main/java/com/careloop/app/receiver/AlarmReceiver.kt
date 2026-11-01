package com.careloop.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.careloop.app.CareLoopApplication
import com.careloop.app.domain.model.CommitmentState
import com.careloop.app.domain.model.EscalationTier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val app = context.applicationContext as CareLoopApplication
        val commitmentId = intent.getStringExtra(EXTRA_COMMITMENT_ID) ?: return

        CoroutineScope(Dispatchers.IO).launch {
            when (intent.action) {
                ACTION_TRIGGER_COMMITMENT_ALARM -> {
                    val commitment = app.commitmentRepository.getCommitmentById(commitmentId)
                    if (commitment != null && commitment.state != CommitmentState.COMPLETED && commitment.state != CommitmentState.CANCELLED) {
                        app.escalationLadderManager.executeEscalation(commitmentId, EscalationTier.TIER_0)
                    }
                }

                ACTION_COMMITMENT_TAKEN -> {
                    app.notificationService.dismissNotification(commitmentId)
                    app.commitmentEngine.confirmCommitmentTaken(commitmentId, "Confirmed via notification action")
                }

                ACTION_COMMITMENT_SNOOZE -> {
                    app.notificationService.dismissNotification(commitmentId)
                    app.commitmentEngine.snoozeCommitment(commitmentId, 15)
                }

                ACTION_COMMITMENT_ESCALATE -> {
                    val level = intent.getIntExtra(EXTRA_ESCALATION_LEVEL, 1)
                    val tier = EscalationTier.values().firstOrNull { it.level == level } ?: EscalationTier.TIER_1
                    app.escalationLadderManager.executeEscalation(commitmentId, tier)
                }
            }
        }
    }

    companion object {
        const val ACTION_TRIGGER_COMMITMENT_ALARM = "com.careloop.app.ACTION_TRIGGER_COMMITMENT_ALARM"
        const val ACTION_COMMITMENT_TAKEN = "com.careloop.app.ACTION_COMMITMENT_TAKEN"
        const val ACTION_COMMITMENT_SNOOZE = "com.careloop.app.ACTION_COMMITMENT_SNOOZE"
        const val ACTION_COMMITMENT_ESCALATE = "com.careloop.app.ACTION_COMMITMENT_ESCALATE"

        const val EXTRA_COMMITMENT_ID = "EXTRA_COMMITMENT_ID"
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_ESCALATION_LEVEL = "EXTRA_ESCALATION_LEVEL"
    }
}

