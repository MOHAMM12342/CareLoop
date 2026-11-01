package com.careloop.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.careloop.app.CareLoopApplication
import com.careloop.app.domain.model.CommitmentState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            val app = context.applicationContext as CareLoopApplication
            CoroutineScope(Dispatchers.IO).launch {
                val now = System.currentTimeMillis()
                val activeCommitments = app.careLoopDatabase.commitmentDao().getCommitmentsInStates(
                    listOf(CommitmentState.UPCOMING, CommitmentState.DUE, CommitmentState.SNOOZED)
                )

                for (entity in activeCommitments) {
                    val commitment = com.careloop.app.domain.model.Commitment(
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

                    if (entity.dueAt > now) {
                        app.alarmNegotiator.scheduleCommitmentReminder(entity.id, entity.title, entity.dueAt)
                    }

                    // Reschedule escalation milestones so timers survive reboot
                    val milestones = app.escalationScheduler.computeMilestones(commitment, isDemoMode = false)
                    for (milestone in milestones) {
                        if (milestone.triggerAt > now) {
                            app.escalationScheduler.scheduleMilestone(milestone)
                        }
                    }
                }
            }
        }
    }
}

