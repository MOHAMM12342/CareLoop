package com.careloop.app.data.repository

import com.careloop.app.data.local.dao.AdherenceLogDao
import com.careloop.app.data.local.dao.CommitmentDao
import com.careloop.app.data.local.entity.AdherenceLogEntity
import com.careloop.app.data.local.entity.CommitmentEntity
import com.careloop.app.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CommitmentRepository(
    private val commitmentDao: CommitmentDao,
    private val adherenceLogDao: AdherenceLogDao
) {
    fun getAllCommitments(): Flow<List<Commitment>> {
        return commitmentDao.getAllCommitments().map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getTodayCommitments(startOfDay: Long, endOfDay: Long): Flow<List<Commitment>> {
        return commitmentDao.getTodayCommitments(startOfDay, endOfDay).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getCommitmentById(id: String): Commitment? {
        return commitmentDao.getCommitmentById(id)?.toDomain()
    }

    suspend fun getCommitmentBySourceId(sourceId: String): Commitment? {
        return commitmentDao.getCommitmentBySourceId(sourceId)?.toDomain()
    }

    suspend fun insertCommitment(commitment: Commitment) {
        commitmentDao.insertCommitment(commitment.toEntity())
    }

    suspend fun insertCommitments(commitments: List<Commitment>) {
        commitmentDao.insertCommitments(commitments.map { it.toEntity() })
    }

    suspend fun updateState(
        id: String,
        newState: CommitmentState,
        action: AdherenceAction,
        note: String = "",
        verifiedOnline: Boolean = false
    ) {
        val commitment = commitmentDao.getCommitmentById(id) ?: return
        val completedAt = if (newState == CommitmentState.COMPLETED) System.currentTimeMillis() else null

        commitmentDao.updateState(id, newState, completedAt, verifiedOnline)
        adherenceLogDao.insertLog(
            AdherenceLogEntity(
                commitmentId = id,
                expectedAt = commitment.dueAt,
                actedAt = System.currentTimeMillis(),
                action = action,
                note = note
            )
        )
    }

    suspend fun updateEscalationLevel(id: String, level: Int, note: String) {
        val commitment = commitmentDao.getCommitmentById(id) ?: return
        val updated = commitment.copy(escalationLevel = level, state = CommitmentState.ESCALATED)
        commitmentDao.updateCommitment(updated)

        adherenceLogDao.insertLog(
            AdherenceLogEntity(
                commitmentId = id,
                expectedAt = commitment.dueAt,
                actedAt = System.currentTimeMillis(),
                action = AdherenceAction.ESCALATED,
                note = note
            )
        )
    }

    fun getLogsForCommitment(commitmentId: String): Flow<List<AdherenceLog>> {
        return adherenceLogDao.getLogsForCommitment(commitmentId).map { list ->
            list.map { it.toDomain() }
        }
    }

    fun getAllLogs(): Flow<List<AdherenceLog>> {
        return adherenceLogDao.getAllLogs().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun deleteAll() {
        commitmentDao.deleteAllCommitments()
        adherenceLogDao.deleteAllLogs()
    }

    private fun CommitmentEntity.toDomain() = Commitment(
        id = id,
        patientId = patientId,
        type = type,
        title = title,
        dueAt = dueAt,
        state = state,
        verificationMethod = verificationMethod,
        sourceEventId = sourceEventId,
        escalationLevel = escalationLevel,
        completedAt = completedAt,
        createdAt = createdAt,
        notes = notes,
        verifiedOnline = verifiedOnline
    )

    private fun Commitment.toEntity() = CommitmentEntity(
        id = id,
        patientId = patientId,
        type = type,
        title = title,
        dueAt = dueAt,
        state = state,
        verificationMethod = verificationMethod,
        sourceEventId = sourceEventId,
        escalationLevel = escalationLevel,
        completedAt = completedAt,
        createdAt = createdAt,
        notes = notes,
        verifiedOnline = verifiedOnline
    )

    private fun AdherenceLogEntity.toDomain() = AdherenceLog(
        id = id,
        commitmentId = commitmentId,
        expectedAt = expectedAt,
        actedAt = actedAt,
        action = action,
        note = note
    )
}

