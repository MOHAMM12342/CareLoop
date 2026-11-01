package com.careloop.app.data.repository

import com.careloop.app.data.local.dao.CaregiverDao
import com.careloop.app.data.local.entity.CaregiverEntity
import com.careloop.app.domain.model.Caregiver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CaregiverRepository(private val caregiverDao: CaregiverDao) {

    fun getAllCaregivers(): Flow<List<Caregiver>> {
        return caregiverDao.getAllCaregivers().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getCaregiversList(): List<Caregiver> {
        return caregiverDao.getCaregiversList().map { it.toDomain() }
    }

    suspend fun insertCaregiver(caregiver: Caregiver) {
        caregiverDao.insertCaregiver(caregiver.toEntity())
    }

    suspend fun deleteCaregiver(caregiver: Caregiver) {
        caregiverDao.deleteCaregiver(caregiver.toEntity())
    }

    suspend fun deleteAll() {
        caregiverDao.deleteAllCaregivers()
    }

    private fun CaregiverEntity.toDomain() = Caregiver(
        id = id,
        patientId = patientId,
        name = name,
        relation = relation,
        phone = phone,
        email = email,
        fcmToken = fcmToken,
        priorityRank = priorityRank,
        channelPreference = channelPreference,
        consentGrantedAt = consentGrantedAt,
        pairingToken = pairingToken
    )

    private fun Caregiver.toEntity() = CaregiverEntity(
        id = id,
        patientId = patientId,
        name = name,
        relation = relation,
        phone = phone,
        email = email,
        fcmToken = fcmToken,
        priorityRank = priorityRank,
        channelPreference = channelPreference,
        consentGrantedAt = consentGrantedAt,
        pairingToken = pairingToken
    )
}

