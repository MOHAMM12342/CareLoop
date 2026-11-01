package com.careloop.app.agent.worker

import android.content.Context
import androidx.work.*
import com.careloop.app.CareLoopApplication
import com.careloop.app.data.local.entity.EscalationAuditLogEntity
import com.careloop.app.domain.model.CommitmentState
import com.careloop.app.domain.model.EscalationTier
import com.careloop.app.domain.model.VerificationMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class FallbackReminderWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val commitmentId = inputData.getString("EXTRA_COMMITMENT_ID") ?: return@withContext Result.failure()
        val app = applicationContext as CareLoopApplication

        val commitment = app.commitmentRepository.getCommitmentById(commitmentId)
        if (commitment != null && commitment.state != CommitmentState.COMPLETED && commitment.state != CommitmentState.CANCELLED) {
            app.escalationLadderManager.executeEscalation(commitmentId, EscalationTier.TIER_0)
        }

        Result.success()
    }
}

class VerificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val app = applicationContext as CareLoopApplication
        val isOnline = app.connectivityMonitor.checkActualInternetReachability()

        if (!isOnline) {
            return@withContext Result.retry()
        }

        val activeCommitments = app.careLoopDatabase.commitmentDao().getCommitmentsInStates(
            listOf(CommitmentState.UPCOMING, CommitmentState.DUE, CommitmentState.SNOOZED, CommitmentState.MISSED)
        )

        var autoClosedCount = 0

        for (entity in activeCommitments) {
            if (entity.verificationMethod == VerificationMethod.FHIR_HEALTH_RECORD && entity.sourceEventId != null) {
                val isCompletedInRecord = app.mockFhirSource.checkEventCompletion(entity.sourceEventId)
                if (isCompletedInRecord) {
                    val detail = when (entity.type.name) {
                        "APPOINTMENT" -> "DMP FHIR: Appointment.status = fulfilled"
                        "LAB_TEST" -> "DMP FHIR: DiagnosticReport.status = final"
                        else -> "DMP FHIR: MedicationDispense verified"
                    }

                    app.commitmentEngine.autoCloseFromHealthRecord(entity.id, detail)
                    autoClosedCount++

                    app.careLoopDatabase.escalationAuditLogDao().insertAuditLog(
                        EscalationAuditLogEntity(
                            commitmentId = entity.id,
                            whoNotified = "Mon espace santé / DMP",
                            notifiedAt = System.currentTimeMillis(),
                            channel = "FHIR_VERIFICATION_WORKER",
                            outcome = "AUTO_CLOSED",
                            dataMinimisedMessage = "Commitment verified from health record: $detail"
                        )
                    )
                }
            }
        }

        Result.success()
    }

    companion object {
        private const val WORK_NAME = "careloop_fhir_verification_periodic"

        fun enqueuePeriodic(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<VerificationWorker>(15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }

        fun runOnce(context: Context) {
            val request = OneTimeWorkRequestBuilder<VerificationWorker>()
                .build()
            WorkManager.getInstance(context).enqueue(request)
        }
    }
}
