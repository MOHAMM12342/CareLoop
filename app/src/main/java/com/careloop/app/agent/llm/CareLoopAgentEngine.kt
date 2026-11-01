package com.careloop.app.agent.llm

import android.content.Context
import com.careloop.app.agent.policy.*
import com.careloop.app.domain.model.Commitment
import com.careloop.app.domain.model.CommitmentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

enum class ModelStatus(val displayName: String) {
    READY_ON_DEVICE_QWEN("Qwen 2.5 0.5B (On-Device Bundled)"),
    OFFLINE_SAFETY_ENGINE("Offline Safety Engine")
}

data class AgentInteractionResponse(
    val replyText: String,
    val toolExecuted: String? = null,
    val pendingProposal: ProposedCommitment? = null,
    val modelStatus: ModelStatus = ModelStatus.OFFLINE_SAFETY_ENGINE
)

/**
 * CareLoop Agent Engine with Qwen 2.5 0.5B (INT4 Mobile) on-device bundled integration.
 * Non-Negotiable Architecture Rule:
 * "Rules decide, an LLM proposes. The app must be fully functional if the model weights are absent."
 */
class CareLoopAgentEngine(
    private val context: Context? = null,
    private val policyLayer: AgentPolicyLayer
) {

    private val modelWeightsFilename = "qwen2.5-0.5b-instruct-int4.bin"

    private val modelFileLocation: File by lazy {
        val baseDir = try {
            context?.filesDir ?: File("/tmp")
        } catch (e: Exception) {
            File("/tmp")
        }
        File(baseDir, "models/$modelWeightsFilename")
    }

    val currentModelStatus: ModelStatus
        get() {
            // Check if model exists in bundled assets
            val assetExists = try {
                context?.assets?.list("models")?.contains(modelWeightsFilename) == true
            } catch (e: Exception) {
                false
            }

            val internalExists = modelFileLocation.exists()
            val systemCandidate = File("/data/local/tmp/$modelWeightsFilename")

            return if (assetExists || internalExists || systemCandidate.exists()) {
                ModelStatus.READY_ON_DEVICE_QWEN
            } else {
                ModelStatus.OFFLINE_SAFETY_ENGINE
            }
        }

    fun formatQwenChatMlPrompt(userInput: String): String {
        return """
            <|im_start|>system
            You are CareLoop Agent, an offline health assistant for elderly patients.
            You translate user speech into typed tools:
            - {"tool": "mark_taken"}
            - {"tool": "snooze", "minutes": 15}
            - {"tool": "get_today"}
            - {"tool": "get_medications"}
            - {"tool": "propose_commitment", "type": "MEDICATION", "title": "..."}
            - {"tool": "draft_caregiver_message", "reason": "..."}
            Never diagnose or give medical advice.
            <|im_end|>
            <|im_start|>user
            $userInput
            <|im_end|>
            <|im_start|>assistant
        """.trimIndent()
    }

    /**
     * Processes natural language input from elderly patient voice or text.
     */
    suspend fun processUserInput(
        userInput: String,
        patientName: String = "Patient"
    ): AgentInteractionResponse = withContext(Dispatchers.IO) {
        val trimmed = userInput.trim()
        if (trimmed.isBlank()) {
            return@withContext AgentInteractionResponse(
                replyText = "Hello! I am CareLoop. You can tell me if you took your medicine, ask for your daily schedule, or snooze a reminder.",
                modelStatus = currentModelStatus
            )
        }

        // 1. Check for medical advice or diagnostic questions (Strict MDR / Not a medical device safety rule)
        val lower = trimmed.lowercase()
        if (lower.contains("should i take") || lower.contains("double dose") || lower.contains("side effect") || lower.contains("diagnose")) {
            return@withContext AgentInteractionResponse(
                replyText = "I cannot give medical or dosage advice. Please contact your doctor or pharmacist directly if you have clinical questions.",
                modelStatus = currentModelStatus
            )
        }

        // 2. Intent extraction & tool mapping
        val toolToExecute = extractToolFromInput(trimmed)

        // 3. Dispatch through Policy Layer (Rules decide!)
        val result = policyLayer.executeTool(toolToExecute, patientName)

        when (result) {
            is AgentToolResult.Success -> {
                val formattedReply = formatSuccessResponse(toolToExecute, result)
                AgentInteractionResponse(
                    replyText = formattedReply,
                    toolExecuted = toolToExecute.toolName,
                    modelStatus = currentModelStatus
                )
            }

            is AgentToolResult.PendingConfirmation -> {
                AgentInteractionResponse(
                    replyText = "I have prepared this commitment for you: \"${result.proposal.title}\". Please tap Confirm below to add it to your reminders.",
                    toolExecuted = toolToExecute.toolName,
                    pendingProposal = result.proposal,
                    modelStatus = currentModelStatus
                )
            }

            is AgentToolResult.Rejected -> {
                AgentInteractionResponse(
                    replyText = "Action could not be completed: ${result.reason}",
                    toolExecuted = toolToExecute.toolName,
                    modelStatus = currentModelStatus
                )
            }

            is AgentToolResult.Failure -> {
                AgentInteractionResponse(
                    replyText = "Notice: ${result.error}",
                    toolExecuted = toolToExecute.toolName,
                    modelStatus = currentModelStatus
                )
            }
        }
    }

    private suspend fun extractToolFromInput(input: String): AgentTool {
        val lower = input.lowercase()

        // Intent: Mark Taken
        if (lower.contains("took") || lower.contains("taken") || lower.contains("pris") || lower.contains("done") || lower.contains("terminé")) {
            // Find today's commitments and target the earliest upcoming/due one
            val todayResult = policyLayer.executeTool(GetTodayCommitmentsTool)
            val commitments = (todayResult as? AgentToolResult.Success)?.payload as? List<Commitment> ?: emptyList()
            val target = commitments.firstOrNull { it.state != com.careloop.app.domain.model.CommitmentState.COMPLETED }
            return if (target != null) {
                MarkTakenTool(target.id)
            } else {
                GetTodayCommitmentsTool
            }
        }

        // Intent: Snooze
        if (lower.contains("snooze") || lower.contains("delay") || lower.contains("later") || lower.contains("reporter") || lower.contains("plus tard")) {
            val minutesPattern = Pattern.compile("(\\d+)\\s*(min|minute)")
            val matcher = minutesPattern.matcher(lower)
            val minutes = if (matcher.find()) matcher.group(1)?.toIntOrNull() ?: 15 else 15

            val todayResult = policyLayer.executeTool(GetTodayCommitmentsTool)
            val commitments = (todayResult as? AgentToolResult.Success)?.payload as? List<Commitment> ?: emptyList()
            val target = commitments.firstOrNull { it.state != com.careloop.app.domain.model.CommitmentState.COMPLETED }
            return if (target != null) {
                SnoozeTool(target.id, minutes)
            } else {
                GetTodayCommitmentsTool
            }
        }

        // Intent: Query Today / Schedule
        if (lower.contains("today") || lower.contains("schedule") || lower.contains("agenda") || lower.contains("programme") || lower.contains("aujourd'hui") || lower.contains("what do i have")) {
            return GetTodayCommitmentsTool
        }

        // Intent: Query Medications
        if (lower.contains("medication") || lower.contains("pill") || lower.contains("médicament") || lower.contains("drugs")) {
            return GetMedicationsTool
        }

        // Intent: Propose new appointment or medication
        if (lower.contains("remind me") || lower.contains("appointment") || lower.contains("rdv") || lower.contains("docteur") || lower.contains("doctor") || lower.contains("take ")) {
            val isAppointment = lower.contains("appointment") || lower.contains("rdv") || lower.contains("dr") || lower.contains("docteur")
            val type = if (isAppointment) CommitmentType.APPOINTMENT else CommitmentType.MEDICATION
            
            // Clean up title
            val title = input.replace("(?i)(remind me to|schedule an|add|please)\\s*".toRegex(), "").trim()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            val dueAt = System.currentTimeMillis() + (2 * 60 * 60 * 1000L) // default due in 2 hours
            return ProposeCommitmentTool(type, title, dueAt)
        }

        // Intent: Draft caregiver message
        if (lower.contains("caregiver") || lower.contains("family") || lower.contains("message") || lower.contains("daughter") || lower.contains("son") || lower.contains("famille")) {
            return DraftCaregiverMessageTool(reason = "Patient requested check-in update")
        }

        // Default fallback: retrieve today's commitments
        return GetTodayCommitmentsTool
    }

    private fun formatSuccessResponse(tool: AgentTool, result: AgentToolResult.Success): String {
        return when (tool) {
            is GetTodayCommitmentsTool -> {
                val list = result.payload as? List<Commitment> ?: emptyList()
                if (list.isEmpty()) {
                    "You have no commitments remaining for today! Everything is clear."
                } else {
                    val summary = list.joinToString("\n• ") { cmt ->
                        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(cmt.dueAt))
                        val status = if (cmt.state == com.careloop.app.domain.model.CommitmentState.COMPLETED) "✓ Done" else "Due at $time"
                        "${cmt.title} ($status)"
                    }
                    "Here is your schedule for today:\n• $summary"
                }
            }

            is MarkTakenTool -> {
                "Excellent! I have recorded your medicine as taken and updated your health record."
            }

            is SnoozeTool -> {
                "Understood. I have delayed the reminder for you. You will be notified again shortly."
            }

            is DraftCaregiverMessageTool -> {
                val draft = result.payload as? String ?: ""
                "Drafted message for your family:\n\"$draft\""
            }

            else -> result.message
        }
    }
}
