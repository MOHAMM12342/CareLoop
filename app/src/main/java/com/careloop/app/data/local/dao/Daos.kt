package com.careloop.app.data.local.dao

import androidx.room.*
import com.careloop.app.data.local.entity.*
import com.careloop.app.domain.model.CommitmentState
import kotlinx.coroutines.flow.Flow

@Dao
interface CommitmentDao {
    @Query("SELECT * FROM commitments ORDER BY dueAt ASC")
    fun getAllCommitments(): Flow<List<CommitmentEntity>>

    @Query("SELECT * FROM commitments WHERE dueAt BETWEEN :startOfDay AND :endOfDay ORDER BY dueAt ASC")
    fun getTodayCommitments(startOfDay: Long, endOfDay: Long): Flow<List<CommitmentEntity>>

    @Query("SELECT * FROM commitments WHERE id = :id")
    suspend fun getCommitmentById(id: String): CommitmentEntity?

    @Query("SELECT * FROM commitments WHERE sourceEventId = :sourceEventId")
    suspend fun getCommitmentBySourceId(sourceEventId: String): CommitmentEntity?

    @Query("SELECT * FROM commitments WHERE state IN (:states)")
    suspend fun getCommitmentsInStates(states: List<CommitmentState>): List<CommitmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommitment(commitment: CommitmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommitments(commitments: List<CommitmentEntity>)

    @Update
    suspend fun updateCommitment(commitment: CommitmentEntity)

    @Query("UPDATE commitments SET state = :newState, completedAt = :completedAt, verifiedOnline = :verifiedOnline WHERE id = :id")
    suspend fun updateState(id: String, newState: CommitmentState, completedAt: Long?, verifiedOnline: Boolean)

    @Query("DELETE FROM commitments")
    suspend fun deleteAllCommitments()
}

@Dao
interface CaregiverDao {
    @Query("SELECT * FROM caregivers ORDER BY priorityRank ASC")
    fun getAllCaregivers(): Flow<List<CaregiverEntity>>

    @Query("SELECT * FROM caregivers ORDER BY priorityRank ASC")
    suspend fun getCaregiversList(): List<CaregiverEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCaregiver(caregiver: CaregiverEntity)

    @Delete
    suspend fun deleteCaregiver(caregiver: CaregiverEntity)

    @Query("DELETE FROM caregivers")
    suspend fun deleteAllCaregivers()
}

@Dao
interface AdherenceLogDao {
    @Query("SELECT * FROM adherence_logs ORDER BY actedAt DESC")
    fun getAllLogs(): Flow<List<AdherenceLogEntity>>

    @Query("SELECT * FROM adherence_logs WHERE commitmentId = :commitmentId ORDER BY actedAt DESC")
    fun getLogsForCommitment(commitmentId: String): Flow<List<AdherenceLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: AdherenceLogEntity)

    @Query("DELETE FROM adherence_logs")
    suspend fun deleteAllLogs()
}

@Dao
interface SettingDao {
    @Query("SELECT value FROM settings WHERE `key` = :key")
    suspend fun getSetting(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setSetting(setting: SettingEntity)

    @Query("DELETE FROM settings")
    suspend fun deleteAllSettings()
}

@Dao
interface PharmacyDao {
    @Query("SELECT * FROM cached_pharmacies ORDER BY cachedAt DESC LIMIT 20")
    suspend fun getCachedPharmacies(): List<CachedPharmacyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPharmacies(pharmacies: List<CachedPharmacyEntity>)

    @Query("DELETE FROM cached_pharmacies")
    suspend fun clearCache()
}

@Dao
interface EscalationAuditLogDao {
    @Query("SELECT * FROM escalation_audit_log ORDER BY notifiedAt DESC")
    fun getAllAuditLogs(): Flow<List<EscalationAuditLogEntity>>

    @Query("SELECT * FROM escalation_audit_log WHERE commitmentId = :commitmentId ORDER BY notifiedAt DESC")
    fun getAuditLogsForCommitment(commitmentId: String): Flow<List<EscalationAuditLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLog(log: EscalationAuditLogEntity)

    @Query("DELETE FROM escalation_audit_log")
    suspend fun deleteAll()
}

