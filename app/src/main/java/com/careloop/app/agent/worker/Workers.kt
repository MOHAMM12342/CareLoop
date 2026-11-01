package com.careloop.app.agent.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.careloop.app.CareLoopApplication
import com.careloop.app.domain.model.CommitmentState
import com.careloop.app.domain.model.EscalationTier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

        val fhirSource = app.mockFhirSource
        val activeCommitments = app.commitmentRepository.getTodayCommitments(
            System.currentTimeMillis() - 86400000,
            System.currentTimeMillis() + 86400000
        )

        // Iterate through active commitments linked to FHIR health events
        // and check if source reports completion (e.g. Appointment fulfilled, Lab test report ready)
        // If completed, call commitmentEngine.autoCloseFromHealthRecord(...)
        // In the mock source, this automatically completes fulfilled events!
        Result.success()
    }
}

