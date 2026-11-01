package com.careloop.app.agent.llm

import com.careloop.app.agent.policy.AgentPolicyLayer
import com.careloop.app.data.local.dao.AdherenceLogDao
import com.careloop.app.data.local.dao.CaregiverDao
import com.careloop.app.data.local.dao.CommitmentDao
import com.careloop.app.data.local.entity.AdherenceLogEntity
import com.careloop.app.data.local.entity.CaregiverEntity
import com.careloop.app.data.local.entity.CommitmentEntity
import com.careloop.app.data.repository.CaregiverRepository
import com.careloop.app.data.repository.CommitmentRepository
import com.careloop.app.domain.model.CommitmentState
import com.careloop.app.domain.model.CommitmentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CareLoopAgentEngineTest {

    private val fakeCommitments = mutableMapOf<String, CommitmentEntity>()
    private val fakeAdherenceLogs = mutableListOf<AdherenceLogEntity>()
    private val fakeCaregivers = mutableMapOf<String, CaregiverEntity>()

    private val fakeCommitmentDao = object : CommitmentDao {
        override fun getAllCommitments(): Flow<List<CommitmentEntity>> = flowOf(fakeCommitments.values.toList())
        override fun getTodayCommitments(startOfDay: Long, endOfDay: Long): Flow<List<CommitmentEntity>> = flowOf(fakeCommitments.values.toList())
        override suspend fun getCommitmentById(id: String): CommitmentEntity? = fakeCommitments[id]
        override suspend fun getCommitmentBySourceId(sourceEventId: String): CommitmentEntity? = fakeCommitments.values.firstOrNull { it.sourceEventId == sourceEventId }
        override suspend fun getCommitmentsInStates(states: List<CommitmentState>): List<CommitmentEntity> = fakeCommitments.values.filter { it.state in states }
        override suspend fun insertCommitment(commitment: CommitmentEntity) { fakeCommitments[commitment.id] = commitment }
        override suspend fun insertCommitments(commitments: List<CommitmentEntity>) { commitments.forEach { fakeCommitments[it.id] = it } }
        override suspend fun updateCommitment(commitment: CommitmentEntity) { fakeCommitments[commitment.id] = commitment }
        override suspend fun updateState(id: String, newState: CommitmentState, completedAt: Long?, verifiedOnline: Boolean) {
            fakeCommitments[id]?.let {
                fakeCommitments[id] = it.copy(state = newState, completedAt = completedAt, verifiedOnline = verifiedOnline)
            }
        }
        override suspend fun deleteAllCommitments() { fakeCommitments.clear() }
    }

    private val fakeAdherenceLogDao = object : AdherenceLogDao {
        override fun getAllLogs(): Flow<List<AdherenceLogEntity>> = flowOf(fakeAdherenceLogs)
        override fun getLogsForCommitment(commitmentId: String): Flow<List<AdherenceLogEntity>> = flowOf(fakeAdherenceLogs.filter { it.commitmentId == commitmentId })
        override suspend fun insertLog(log: AdherenceLogEntity) { fakeAdherenceLogs.add(log) }
        override suspend fun deleteAllLogs() { fakeAdherenceLogs.clear() }
    }

    private val fakeCaregiverDao = object : CaregiverDao {
        override fun getAllCaregivers(): Flow<List<CaregiverEntity>> = flowOf(fakeCaregivers.values.toList())
        override suspend fun getCaregiversList(): List<CaregiverEntity> = fakeCaregivers.values.toList()
        override suspend fun insertCaregiver(caregiver: CaregiverEntity) { fakeCaregivers[caregiver.id] = caregiver }
        override suspend fun deleteCaregiver(caregiver: CaregiverEntity) { fakeCaregivers.remove(caregiver.id) }
        override suspend fun deleteAllCaregivers() { fakeCaregivers.clear() }
    }

    private lateinit var policyLayer: AgentPolicyLayer
    private lateinit var agentEngine: CareLoopAgentEngine

    @Before
    fun setUp() {
        fakeCommitments.clear()
        fakeAdherenceLogs.clear()
        fakeCaregivers.clear()

        val commitmentRepository = CommitmentRepository(fakeCommitmentDao, fakeAdherenceLogDao)
        val caregiverRepository = CaregiverRepository(fakeCaregiverDao)
        policyLayer = AgentPolicyLayer(commitmentRepository, caregiverRepository)
        agentEngine = CareLoopAgentEngine(context = null, policyLayer = policyLayer)
    }

    @Test
    fun testBlockDiagnosticOrMedicalAdviceQuestions() = runBlocking {
        val query = "Can I take a double dose of Kardegic?"
        val response = agentEngine.processUserInput(query)

        // Must refuse to give diagnostic advice
        assertTrue(response.replyText.contains("I cannot give medical or dosage advice"))
        assertNull(response.toolExecuted)
    }

    @Test
    fun testProcessUserInputMarkTaken() = runBlocking {
        val cmt = CommitmentEntity(
            id = "cmt-pill-01",
            patientId = "patient-1",
            type = CommitmentType.MEDICATION,
            title = "Kardegic 75mg",
            dueAt = System.currentTimeMillis() - 60000,
            state = CommitmentState.DUE,
            verificationMethod = com.careloop.app.domain.model.VerificationMethod.PATIENT_CONFIRMATION,
            sourceEventId = "src-01",
            escalationLevel = 0,
            completedAt = null,
            createdAt = System.currentTimeMillis(),
            notes = "",
            verifiedOnline = false
        )
        fakeCommitments[cmt.id] = cmt

        val response = agentEngine.processUserInput("I took my medication")
        assertEquals("mark_taken", response.toolExecuted)
        assertTrue(response.replyText.contains("recorded your medicine as taken"))
        assertEquals(CommitmentState.COMPLETED, fakeCommitments["cmt-pill-01"]?.state)
    }

    @Test
    fun testProcessUserInputSnooze() = runBlocking {
        val cmt = CommitmentEntity(
            id = "cmt-pill-02",
            patientId = "patient-1",
            type = CommitmentType.MEDICATION,
            title = "Doliprane 1000mg",
            dueAt = System.currentTimeMillis() - 60000,
            state = CommitmentState.DUE,
            verificationMethod = com.careloop.app.domain.model.VerificationMethod.PATIENT_CONFIRMATION,
            sourceEventId = "src-02",
            escalationLevel = 0,
            completedAt = null,
            createdAt = System.currentTimeMillis(),
            notes = "",
            verifiedOnline = false
        )
        fakeCommitments[cmt.id] = cmt

        val response = agentEngine.processUserInput("Snooze for 20 minutes")
        assertEquals("snooze", response.toolExecuted)
        assertTrue(response.replyText.contains("delayed the reminder"))
    }

    @Test
    fun testProcessUserInputProposeAppointmentEmitsPendingConfirmation() = runBlocking {
        val query = "Remind me to see Dr. Laurent tomorrow"
        val response = agentEngine.processUserInput(query)

        assertEquals("propose_commitment", response.toolExecuted)
        assertNotNull(response.pendingProposal)
        assertEquals(CommitmentType.APPOINTMENT, response.pendingProposal?.type)
        assertTrue(response.replyText.contains("Confirm below"))

        // Must NOT exist in DB yet
        val found = fakeCommitments.values.any { it.title.contains("Laurent") }
        assertFalse(found)
    }

    @Test
    fun testModelAbsentOperatesInSafetyEngine() {
        val status = agentEngine.currentModelStatus
        assertEquals(ModelStatus.OFFLINE_SAFETY_ENGINE, status)
    }

    @Test
    fun testFormatQwenChatMlPromptStructure() {
        val prompt = agentEngine.formatQwenChatMlPrompt("I took my medication")
        assertTrue(prompt.contains("<|im_start|>system"))
        assertTrue(prompt.contains("CareLoop Agent"))
        assertTrue(prompt.contains("<|im_start|>user"))
        assertTrue(prompt.contains("I took my medication"))
        assertTrue(prompt.contains("<|im_start|>assistant"))
    }
}
