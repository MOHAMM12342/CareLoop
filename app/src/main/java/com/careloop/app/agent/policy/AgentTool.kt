package com.careloop.app.agent.policy

import com.careloop.app.domain.model.CommitmentType
import java.util.UUID

/**
 * Typed tool definitions for CareLoop Agent.
 * Non-negotiable rule: Rules decide, an LLM proposes.
 * All actions pass through the AgentPolicyLayer before state changes or executions occur.
 */
sealed interface AgentTool {
    val toolName: String
}

/** Queries today's commitments */
data object GetTodayCommitmentsTool : AgentTool {
    override val toolName: String = "get_today"
}

/** Queries all active commitments */
data object GetAllCommitmentsTool : AgentTool {
    override val toolName: String = "get_all_commitments"
}

/** Queries registered medications */
data object GetMedicationsTool : AgentTool {
    override val toolName: String = "get_medications"
}

/** Marks a commitment as taken / completed */
data class MarkTakenTool(
    val commitmentId: String
) : AgentTool {
    override val toolName: String = "mark_taken"
}

/** Snoozes a commitment reminder */
data class SnoozeTool(
    val commitmentId: String,
    val snoozeMinutes: Int = 15
) : AgentTool {
    override val toolName: String = "snooze"
}

/** Cancels a commitment */
data class CancelCommitmentTool(
    val commitmentId: String,
    val reason: String? = null
) : AgentTool {
    override val toolName: String = "cancel_commitment"
}

/**
 * Proposes a commitment.
 * Medical Safety Gate: Any proposed commitment NEVER writes directly to the DB.
 * It enters a PendingConfirmation state requiring explicit patient confirmation.
 */
data class ProposeCommitmentTool(
    val type: CommitmentType,
    val title: String,
    val dueAt: Long,
    val instructions: String? = null
) : AgentTool {
    override val toolName: String = "propose_commitment"
}

/**
 * Drafts an outbound message to a caregiver.
 * Privacy & Data-Minimisation Gate: Never contains drug or clinical names.
 */
data class DraftCaregiverMessageTool(
    val reason: String
) : AgentTool {
    override val toolName: String = "draft_caregiver_message"
}

data class ProposedCommitment(
    val id: String = "prop-${UUID.randomUUID()}",
    val type: CommitmentType,
    val title: String,
    val dueAt: Long,
    val instructions: String? = null
)

sealed class AgentToolResult {
    data class Success(
        val message: String,
        val payload: Any? = null
    ) : AgentToolResult()

    data class PendingConfirmation(
        val proposal: ProposedCommitment,
        val message: String
    ) : AgentToolResult()

    data class Rejected(
        val reason: String
    ) : AgentToolResult()

    data class Failure(
        val error: String
    ) : AgentToolResult()
}

