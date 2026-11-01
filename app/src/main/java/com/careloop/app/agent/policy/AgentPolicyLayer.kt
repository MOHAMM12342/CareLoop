package com.careloop.app.agent.policy

import com.careloop.app.agent.AlarmCapabilityNegotiator
import com.careloop.app.data.repository.CaregiverRepository
import com.careloop.app.data.repository.CommitmentRepository
import com.careloop.app.domain.escalation.EscalationChannel
import com.careloop.app.domain.model.Commitment
import com.careloop.app.domain.model.CommitmentState
import com.careloop.app.domain.model.CommitmentType
import com.careloop.app.domain.model.VerificationMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * Deterministic Policy Layer for CareLoop Agent.
 * 
 * Rules Decide, an LLM Proposes:
 * 1. Propose Commitment: Medical data (drug, dose, schedule) is NEVER written directly to SQLite.
 *    It returns a PendingConfirmation result requiring explicit human confirmation.
 * 2. Snooze: Clamps snooze times between 5 and 120 minutes.
 * 3. Caregiver Drafts: Strict data-minimisation enforcement. Never leaks drug or clinical terms.
 * 4. Escalation Invariance: The LLM cannot alter escalation ladders, bypass audit logs, or silence alerts.
 */
class AgentPolicyLayer(
    private val commitmentRepository: CommitmentRepository,
    private val caregiverRepository: CaregiverRepository,
    private val alarmNegotiator: AlarmCapabilityNegotiator? = null
) {

    private val forbiddenMedicalWords = listOf(
        "kardegic", "aspirin", "doliprane", "paracetamol", "metformin",
        "insulin", "atorvastatin", "mg", "mcg", "tablet", "sachet",
        "capsule", "comprimé", "gélule", "cancer", "chemo", "dose", "prescription"
    )

    suspend fun executeTool(
        tool: AgentTool,
        patientName: String = "Patient"
    ): AgentToolResult = withContext(Dispatchers.IO) {
        when (tool) {
            is GetTodayCommitmentsTool -> {
                val today = commitmentRepository.getTodayCommitments()
                AgentToolResult.Success(
                    message = "Retrieved ${today.size} commitments scheduled for today.",
                    payload = today
                )
            }

            is GetAllCommitmentsTool -> {
                val all = commitmentRepository.getAllActiveCommitments()
                AgentToolResult.Success(
                    message = "Retrieved ${all.size} active commitments.",
                    payload = all
                )
            }

            is GetMedicationsTool -> {
                val meds = commitmentRepository.getAllActiveCommitments().filter { it.type == CommitmentType.MEDICATION }
                AgentToolResult.Success(
                    message = "Retrieved ${meds.size} medication commitments.",
                    payload = meds
                )
            }

            is MarkTakenTool -> {
                val commitment = commitmentRepository.getCommitmentById(tool.commitmentId)
                    ?: return@withContext AgentToolResult.Failure("Commitment ${tool.commitmentId} not found.")

                commitmentRepository.markCompleted(
                    commitmentId = commitment.id,
                    method = VerificationMethod.PATIENT_CONFIRMATION,
                    notes = "Marked completed via CareLoop Agent."
                )
                alarmNegotiator?.cancelCommitmentReminder(commitment.id)

                AgentToolResult.Success(
                    message = "Successfully marked \"${commitment.title}\" as completed.",
                    payload = commitment.id
                )
            }

            is SnoozeTool -> {
                val commitment = commitmentRepository.getCommitmentById(tool.commitmentId)
                    ?: return@withContext AgentToolResult.Failure("Commitment ${tool.commitmentId} not found.")

                // Policy: Snooze must be strictly bounded between 5 and 120 minutes
                val safeMinutes = tool.snoozeMinutes.coerceIn(5, 120)
                val newDueAt = System.currentTimeMillis() + (safeMinutes * 60 * 1000L)

                commitmentRepository.updateCommitmentDueAt(commitment.id, newDueAt)
                alarmNegotiator?.scheduleCommitmentReminder(commitment.id, commitment.title, newDueAt)

                AgentToolResult.Success(
                    message = "Snoozed \"${commitment.title}\" for $safeMinutes minutes. New reminder set.",
                    payload = newDueAt
                )
            }

            is CancelCommitmentTool -> {
                val commitment = commitmentRepository.getCommitmentById(tool.commitmentId)
                    ?: return@withContext AgentToolResult.Failure("Commitment ${tool.commitmentId} not found.")

                commitmentRepository.updateCommitmentState(commitment.id, CommitmentState.CANCELLED)
                alarmNegotiator?.cancelCommitmentReminder(commitment.id)

                AgentToolResult.Success(
                    message = "Cancelled commitment \"${commitment.title}\".",
                    payload = commitment.id
                )
            }

            is ProposeCommitmentTool -> {
                // Policy Rule: Medical safety gate. Never save directly to DB!
                if (tool.title.isBlank()) {
                    return@withContext AgentToolResult.Rejected("Cannot propose commitment with empty title.")
                }

                val proposal = ProposedCommitment(
                    type = tool.type,
                    title = tool.title.trim(),
                    dueAt = if (tool.dueAt > 0) tool.dueAt else System.currentTimeMillis() + (60 * 60 * 1000L),
                    instructions = tool.instructions
                )

                AgentToolResult.PendingConfirmation(
                    proposal = proposal,
                    message = "Proposed ${tool.type.name.lowercase()}: \"${proposal.title}\". Awaiting patient confirmation."
                )
            }

            is DraftCaregiverMessageTool -> {
                // Policy Rule: Strict data-minimisation. Never leak drug names.
                val base = EscalationChannel.buildDataMinimisedMessage(patientName)
                val reasonLower = tool.reason.lowercase()
                
                val containsForbidden = forbiddenMedicalWords.any { reasonLower.contains(it) }
                val sanitizedDraft = if (containsForbidden) {
                    base // Mask with compliant data-minimised standard
                } else {
                    "$base Status: ${tool.reason.trim()}."
                }

                AgentToolResult.Success(
                    message = "Drafted data-minimised caregiver message.",
                    payload = sanitizedDraft
                )
            }
        }
    }

    /**
     * Executes the explicit human confirmation of a proposed commitment.
     * This is ONLY called when the human user taps "Confirm & Save" on screen.
     */
    suspend fun confirmProposedCommitment(proposal: ProposedCommitment): Commitment = withContext(Dispatchers.IO) {
        val commitment = Commitment(
            id = "cmt-${UUID.randomUUID()}",
            patientId = "patient-1",
            type = proposal.type,
            title = proposal.title,
            dueAt = proposal.dueAt,
            state = CommitmentState.UPCOMING,
            verificationMethod = VerificationMethod.PATIENT_CONFIRMATION,
            escalationLevel = 0,
            sourceEventId = "agent-proposal-${System.currentTimeMillis()}",
            createdAt = System.currentTimeMillis(),
            notes = proposal.instructions ?: "Proposed by CareLoop Agent and confirmed by patient."
        )

        commitmentRepository.insertCommitment(commitment)
        alarmNegotiator?.scheduleCommitmentReminder(commitment.id, commitment.title, commitment.dueAt)
        commitment
    }
}

