package com.careloop.app.agent;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001BM\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\u0002\u0010\u0014J(\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00192\b\b\u0002\u0010\u001a\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u001bR\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/careloop/app/agent/EscalationLadderManager;", "", "context", "Landroid/content/Context;", "commitmentRepository", "Lcom/careloop/app/data/repository/CommitmentRepository;", "caregiverRepository", "Lcom/careloop/app/data/repository/CaregiverRepository;", "escalationAuditLogDao", "Lcom/careloop/app/data/local/dao/EscalationAuditLogDao;", "escalationChannel", "Lcom/careloop/app/domain/escalation/EscalationChannel;", "notificationService", "Lcom/careloop/app/service/CareLoopNotificationService;", "ttsManager", "Lcom/careloop/app/service/TextToSpeechManager;", "backendClient", "Lcom/careloop/app/service/BackendEscalationClient;", "connectivityMonitor", "Lcom/careloop/app/agent/ConnectivityMonitor;", "(Landroid/content/Context;Lcom/careloop/app/data/repository/CommitmentRepository;Lcom/careloop/app/data/repository/CaregiverRepository;Lcom/careloop/app/data/local/dao/EscalationAuditLogDao;Lcom/careloop/app/domain/escalation/EscalationChannel;Lcom/careloop/app/service/CareLoopNotificationService;Lcom/careloop/app/service/TextToSpeechManager;Lcom/careloop/app/service/BackendEscalationClient;Lcom/careloop/app/agent/ConnectivityMonitor;)V", "executeEscalation", "", "commitmentId", "targetTier", "Lcom/careloop/app/domain/model/EscalationTier;", "patientName", "(Ljava/lang/String;Lcom/careloop/app/domain/model/EscalationTier;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_demoDebug"})
public final class EscalationLadderManager {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.data.repository.CommitmentRepository commitmentRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.data.repository.CaregiverRepository caregiverRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.data.local.dao.EscalationAuditLogDao escalationAuditLogDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.domain.escalation.EscalationChannel escalationChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.service.CareLoopNotificationService notificationService = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.service.TextToSpeechManager ttsManager = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.service.BackendEscalationClient backendClient = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.agent.ConnectivityMonitor connectivityMonitor = null;
    
    public EscalationLadderManager(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.careloop.app.data.repository.CommitmentRepository commitmentRepository, @org.jetbrains.annotations.NotNull()
    com.careloop.app.data.repository.CaregiverRepository caregiverRepository, @org.jetbrains.annotations.NotNull()
    com.careloop.app.data.local.dao.EscalationAuditLogDao escalationAuditLogDao, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.escalation.EscalationChannel escalationChannel, @org.jetbrains.annotations.NotNull()
    com.careloop.app.service.CareLoopNotificationService notificationService, @org.jetbrains.annotations.NotNull()
    com.careloop.app.service.TextToSpeechManager ttsManager, @org.jetbrains.annotations.NotNull()
    com.careloop.app.service.BackendEscalationClient backendClient, @org.jetbrains.annotations.NotNull()
    com.careloop.app.agent.ConnectivityMonitor connectivityMonitor) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object executeEscalation(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.EscalationTier targetTier, @org.jetbrains.annotations.NotNull()
    java.lang.String patientName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.String> $completion) {
        return null;
    }
}