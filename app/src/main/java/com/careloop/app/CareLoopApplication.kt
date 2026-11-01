package com.careloop.app

import android.app.Application
import com.careloop.app.agent.*
import com.careloop.app.data.local.CareLoopDatabase
import com.careloop.app.data.repository.CaregiverRepository
import com.careloop.app.data.repository.CommitmentRepository
import com.careloop.app.data.source.ManualEntrySource
import com.careloop.app.data.source.MockFhirSource
import com.careloop.app.data.source.OcrPrescriptionSource
import com.careloop.app.domain.model.Caregiver
import com.careloop.app.domain.model.ChannelPreference
import com.careloop.app.feature.ocr.PrescriptionOcrParser
import com.careloop.app.feature.pharmacy.OsmPharmacyFinder
import com.careloop.app.service.BackendEscalationClient
import com.careloop.app.service.CareLoopNotificationService
import com.careloop.app.service.TextToSpeechManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CareLoopApplication : Application() {

    lateinit var careLoopDatabase: CareLoopDatabase
        private set

    lateinit var commitmentRepository: CommitmentRepository
        private set

    lateinit var caregiverRepository: CaregiverRepository
        private set

    lateinit var commitmentEngine: CommitmentEngine
        private set

    lateinit var escalationLadderManager: EscalationLadderManager
        private set

    lateinit var alarmNegotiator: AlarmCapabilityNegotiator
        private set

    lateinit var connectivityMonitor: ConnectivityMonitor
        private set

    lateinit var notificationService: CareLoopNotificationService
        private set

    lateinit var ttsManager: TextToSpeechManager
        private set

    lateinit var backendClient: BackendEscalationClient
        private set

    lateinit var mockFhirSource: MockFhirSource
        private set

    lateinit var manualEntrySource: ManualEntrySource
        private set

    lateinit var ocrPrescriptionSource: OcrPrescriptionSource
        private set

    lateinit var ocrParser: PrescriptionOcrParser
        private set

    lateinit var pharmacyFinder: OsmPharmacyFinder
        private set

    override fun onCreate() {
        super.onCreate()

        // 1. Initialize Encrypted Database
        careLoopDatabase = CareLoopDatabase.getInstance(this, useEncryption = true)

        // 2. Initialize Repositories
        commitmentRepository = CommitmentRepository(
            careLoopDatabase.commitmentDao(),
            careLoopDatabase.adherenceLogDao()
        )
        caregiverRepository = CaregiverRepository(careLoopDatabase.caregiverDao())

        // 3. Initialize Services & Monitor
        notificationService = CareLoopNotificationService(this)
        ttsManager = TextToSpeechManager(this)
        backendClient = BackendEscalationClient()
        connectivityMonitor = ConnectivityMonitor(this)
        alarmNegotiator = AlarmCapabilityNegotiator(this)

        // 4. Initialize Agent Core & Ladder
        commitmentEngine = CommitmentEngine(commitmentRepository)
        escalationLadderManager = EscalationLadderManager(
            context = this,
            commitmentRepository = commitmentRepository,
            caregiverRepository = caregiverRepository,
            notificationService = notificationService,
            ttsManager = ttsManager,
            backendClient = backendClient,
            connectivityMonitor = connectivityMonitor
        )

        // 5. Initialize Sources & Features
        mockFhirSource = MockFhirSource(this)
        manualEntrySource = ManualEntrySource()
        ocrPrescriptionSource = OcrPrescriptionSource()
        ocrParser = PrescriptionOcrParser(this)
        pharmacyFinder = OsmPharmacyFinder(careLoopDatabase.pharmacyDao())

        // 6. Ensure default caregiver with consent for out-of-the-box demo if empty
        CoroutineScope(Dispatchers.IO).launch {
            val list = caregiverRepository.getCaregiversList()
            if (list.isEmpty()) {
                caregiverRepository.insertCaregiver(
                    Caregiver(
                        id = "caregiver-1",
                        name = "Sarah (Daughter)",
                        relation = "Daughter",
                        phone = "+33612345678",
                        email = "sarah@example.com",
                        priorityRank = 1,
                        channelPreference = ChannelPreference.SMS,
                        consentGrantedAt = System.currentTimeMillis() // GDPR consent recorded
                    )
                )
            }
        }
    }
}

