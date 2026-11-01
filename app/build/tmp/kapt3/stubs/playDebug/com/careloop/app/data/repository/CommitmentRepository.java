package com.careloop.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0012\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000bJ\u0012\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\f0\u000bJ\u0018\u0010\u0010\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0018\u0010\u0014\u001a\u0004\u0018\u00010\r2\u0006\u0010\u0015\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u001a\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\f0\u000b2\u0006\u0010\u0017\u001a\u00020\u0012J\"\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\f0\u000b2\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aJ\u0016\u0010\u001c\u001a\u00020\b2\u0006\u0010\u001d\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u001c\u0010\u001f\u001a\u00020\b2\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0086@\u00a2\u0006\u0002\u0010!J&\u0010\"\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010&J:\u0010\'\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+2\b\b\u0002\u0010%\u001a\u00020\u00122\b\b\u0002\u0010,\u001a\u00020-H\u0086@\u00a2\u0006\u0002\u0010.J\f\u0010/\u001a\u00020\u000f*\u000200H\u0002J\f\u0010/\u001a\u00020\r*\u000201H\u0002J\f\u00102\u001a\u000201*\u00020\rH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2 = {"Lcom/careloop/app/data/repository/CommitmentRepository;", "", "commitmentDao", "Lcom/careloop/app/data/local/dao/CommitmentDao;", "adherenceLogDao", "Lcom/careloop/app/data/local/dao/AdherenceLogDao;", "(Lcom/careloop/app/data/local/dao/CommitmentDao;Lcom/careloop/app/data/local/dao/AdherenceLogDao;)V", "deleteAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllCommitments", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/careloop/app/domain/model/Commitment;", "getAllLogs", "Lcom/careloop/app/domain/model/AdherenceLog;", "getCommitmentById", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCommitmentBySourceId", "sourceId", "getLogsForCommitment", "commitmentId", "getTodayCommitments", "startOfDay", "", "endOfDay", "insertCommitment", "commitment", "(Lcom/careloop/app/domain/model/Commitment;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertCommitments", "commitments", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEscalationLevel", "level", "", "note", "(Ljava/lang/String;ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateState", "newState", "Lcom/careloop/app/domain/model/CommitmentState;", "action", "Lcom/careloop/app/domain/model/AdherenceAction;", "verifiedOnline", "", "(Ljava/lang/String;Lcom/careloop/app/domain/model/CommitmentState;Lcom/careloop/app/domain/model/AdherenceAction;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toDomain", "Lcom/careloop/app/data/local/entity/AdherenceLogEntity;", "Lcom/careloop/app/data/local/entity/CommitmentEntity;", "toEntity", "app_playDebug"})
public final class CommitmentRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.data.local.dao.CommitmentDao commitmentDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.data.local.dao.AdherenceLogDao adherenceLogDao = null;
    
    public CommitmentRepository(@org.jetbrains.annotations.NotNull()
    com.careloop.app.data.local.dao.CommitmentDao commitmentDao, @org.jetbrains.annotations.NotNull()
    com.careloop.app.data.local.dao.AdherenceLogDao adherenceLogDao) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.careloop.app.domain.model.Commitment>> getAllCommitments() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.careloop.app.domain.model.Commitment>> getTodayCommitments(long startOfDay, long endOfDay) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCommitmentById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.careloop.app.domain.model.Commitment> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getCommitmentBySourceId(@org.jetbrains.annotations.NotNull()
    java.lang.String sourceId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.careloop.app.domain.model.Commitment> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertCommitment(@org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.Commitment commitment, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertCommitments(@org.jetbrains.annotations.NotNull()
    java.util.List<com.careloop.app.domain.model.Commitment> commitments, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateState(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.CommitmentState newState, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.AdherenceAction action, @org.jetbrains.annotations.NotNull()
    java.lang.String note, boolean verifiedOnline, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateEscalationLevel(@org.jetbrains.annotations.NotNull()
    java.lang.String id, int level, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.careloop.app.domain.model.AdherenceLog>> getLogsForCommitment(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.careloop.app.domain.model.AdherenceLog>> getAllLogs() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final com.careloop.app.domain.model.Commitment toDomain(com.careloop.app.data.local.entity.CommitmentEntity $this$toDomain) {
        return null;
    }
    
    private final com.careloop.app.data.local.entity.CommitmentEntity toEntity(com.careloop.app.domain.model.Commitment $this$toEntity) {
        return null;
    }
    
    private final com.careloop.app.domain.model.AdherenceLog toDomain(com.careloop.app.data.local.entity.AdherenceLogEntity $this$toDomain) {
        return null;
    }
}