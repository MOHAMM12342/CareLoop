package com.careloop.app.agent.policy

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

class AgentPolicyLayerTest {

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

    private lateinit var commitmentRepository: CommitmentRepository
    private lateinit var caregiverRepository: CaregiverRepository
    private lateinit var policyLayer: AgentPolicyLayer

    @Before
    fun setUp() {
        fakeCommitments.clear()
        fakeAdherenceLogs.clear()
        fakeCaregivers.clear()

        commitmentRepository = CommitmentRepository(fakeCommitmentDao, fakeAdherenceLogDao)
        caregiverRepository = CaregiverRepository(fakeCaregiverDao)
        policyLayer = AgentPolicyLayer(commitmentRepository, caregiverRepository, alarmNegotiator = null)
    }

    @Test
    fun testProposeCommitmentReturnsPendingConfirmationAndDoesNotWriteToDb() = runBlocking {
        val tool = ProposeCommitmentTool(
            type = CommitmentType.MEDICATION,
            title = "Take Kardegic 75mg",
            dueAt = System.currentTimeMillis() + 3600000,
            instructions = "1 sachet in the morning"
        )

        val result = policyLayer.executeTool(tool)

        // Must be PendingConfirmation, not direct success
        assertTrue(result is AgentToolResult.PendingConfirmation)
        val pending = result as AgentToolResult.PendingConfirmation
        assertEquals("Take Kardegic 75mg", pending.proposal.title)

        // Database must remain untouched (human-in-the-loop safety rule)
        val inDb = fakeCommitments.values.any { it.title.contains("Kardegic") }
        assertFalse("Proposed medication must NEVER write directly to DB without patient confirmation", inDb)
    }

    @Test
    fun testConfirmProposedCommitmentWritesToDb() = runBlocking {
        val proposal = ProposedCommitment(
            type = CommitmentType.MEDICATION,
            title = "Take Doliprane 1000mg",
            dueAt = System.currentTimeMillis() + 1800000,
            instructions = "1 tablet with water"
        )

        val saved = policyLayer.confirmProposedCommitment(proposal)

        assertNotNull(saved.id)
        assertEquals("Take Doliprane 1000mg", saved.title)
        assertEquals(CommitmentState.UPCOMING, saved.state)

        val entity = fakeCommitmentDao.getCommitmentById(saved.id)
        assertNotNull("Commitment must be persisted after explicit confirmation", entity)
        assertEquals("Take Doliprane 1000mg", entity?.title)
    }

    @Test
    fun testDraftCaregiverMessageStrictlySanitizesDrugNames() = runBlocking {
        val tool = DraftCaregiverMessageTool(reason = "Patient missed taking Kardegic 75mg sachet")
        val result = policyLayer.executeTool(tool, patientName = "Jean Dupont")

        assertTrue(result is AgentToolResult.Success)
        val draft = (result as AgentToolResult.Success).payload as String

        // Strictly data-minimised: contains standard alert without drug names
        assertEquals("Jean Dupont has not confirmed an important reminder.", draft)
        assertFalse(draft.contains("Kardegic", ignoreCase = true))
        assertFalse(draft.contains("75mg", ignoreCase = true))
        assertFalse(draft.contains("sachet", ignoreCase = true))
    }

    @Test
    fun testSnoozeToolBoundsMinutes() = runBlocking {
        val cmt = CommitmentEntity(
            id = "cmt-test-01",
            patientId = "patient-1",
            type = CommitmentType.MEDICATION,
            title = "Aspirin",
            dueAt = System.currentTimeMillis(),
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

        // Request an excessive snooze of 300 minutes -> should be clamped to 120
        val tool = SnoozeTool(commitmentId = "cmt-test-01", snoozeMinutes = 300)
        val result = policyLayer.executeTool(tool)

        assertTrue(result is AgentToolResult.Success)
        val updated = fakeCommitments["cmt-test-01"]
        assertNotNull(updated)
        // DueAt should be approximately now + 120 minutes (not 300)
        val maxAllowed = System.currentTimeMillis() + (125 * 60 * 1000L)
        assertTrue(updated!!.dueAt <= maxAllowed)
    }

    @Test
    fun testMarkTakenToolCompletesCommitment() = runBlocking {
        val cmt = CommitmentEntity(
            id = "cmt-test-02",
            patientId = "patient-1",
            type = CommitmentType.MEDICATION,
            title = "Blood pressure pill",
            dueAt = System.currentTimeMillis(),
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

        val tool = MarkTakenTool("cmt-test-02")
        val result = policyLayer.executeTool(tool)

        assertTrue(result is AgentToolResult.Success)
        val updated = fakeCommitments["cmt-test-02"]
        assertEquals(CommitmentState.COMPLETED, updated?.state)
        assertTrue(fakeAdherenceLogs.isNotEmpty())
    }
}
