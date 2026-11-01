package com.careloop.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.careloop.app.domain.model.AdherenceAction
import com.careloop.app.domain.model.ChannelPreference
import com.careloop.app.domain.model.CommitmentState
import com.careloop.app.domain.model.CommitmentType
import com.careloop.app.domain.model.VerificationMethod

@Entity(tableName = "patients")
data class PatientEntity(
    @PrimaryKey val id: String,
    val name: String,
    val birthDate: String,
    val emergencyNote: String
)

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val dosageForm: String,
    val strength: String,
    val instructions: String
)

@Entity(tableName = "commitments")
data class CommitmentEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val type: CommitmentType,
    val title: String,
    val dueAt: Long,
    val state: CommitmentState,
    val verificationMethod: VerificationMethod,
    val sourceEventId: String?,
    val escalationLevel: Int,
    val completedAt: Long?,
    val createdAt: Long,
    val notes: String,
    val verifiedOnline: Boolean
)

@Entity(tableName = "adherence_logs")
data class AdherenceLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val commitmentId: String,
    val expectedAt: Long,
    val actedAt: Long,
    val action: AdherenceAction,
    val note: String
)

@Entity(tableName = "caregivers")
data class CaregiverEntity(
    @PrimaryKey val id: String,
    val patientId: String,
    val name: String,
    val relation: String,
    val phone: String,
    val email: String,
    val fcmToken: String?,
    val priorityRank: Int,
    val channelPreference: ChannelPreference,
    val consentGrantedAt: Long?,
    val pairingToken: String? = null
)

@Entity(tableName = "escalation_audit_log")
data class EscalationAuditLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val commitmentId: String,
    val whoNotified: String,
    val notifiedAt: Long,
    val channel: String,
    val outcome: String,
    val dataMinimisedMessage: String
)

@Entity(tableName = "health_events_cache")
data class HealthEventCacheEntity(
    @PrimaryKey val id: String,
    val sourceType: String,
    val rawJson: String,
    val receivedAt: Long,
    val processedAt: Long?
)

@Entity(tableName = "settings")
data class SettingEntity(
    @PrimaryKey val key: String,
    val value: String
)

@Entity(tableName = "cached_pharmacies")
data class CachedPharmacyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phone: String?,
    val cachedAt: Long
)

