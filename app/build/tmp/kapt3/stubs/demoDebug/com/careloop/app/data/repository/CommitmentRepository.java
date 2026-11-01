package com.careloop.app.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0086@\u00a2\u0006\u0002\u0010\tJ\u0012\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000eJ\u0012\u0010\u000f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000b0\u000eJ\u0018\u0010\u0011\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u0018\u0010\u0015\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0016\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\u0014J\u001a\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000b0\u000e2\u0006\u0010\u0018\u001a\u00020\u0013J\u0014\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0086@\u00a2\u0006\u0002\u0010\tJ\"\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000e2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001bJ\u0016\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001e\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\u001fJ\u001c\u0010 \u001a\u00020\b2\f\u0010!\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0086@\u00a2\u0006\u0002\u0010\"J*\u0010#\u001a\u00020\b2\u0006\u0010\u0018\u001a\u00020\u00132\b\b\u0002\u0010$\u001a\u00020%2\b\b\u0002\u0010&\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u0010\'J\u001e\u0010(\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010)\u001a\u00020\u001bH\u0086@\u00a2\u0006\u0002\u0010*J\u001e\u0010+\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010,\u001a\u00020-H\u0086@\u00a2\u0006\u0002\u0010.J&\u0010/\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u00100\u001a\u0002012\u0006\u00102\u001a\u00020\u0013H\u0086@\u00a2\u0006\u0002\u00103J:\u00104\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010,\u001a\u00020-2\u0006\u00105\u001a\u0002062\b\b\u0002\u00102\u001a\u00020\u00132\b\b\u0002\u00107\u001a\u000208H\u0086@\u00a2\u0006\u0002\u00109J\f\u0010:\u001a\u00020\u0010*\u00020;H\u0002J\f\u0010:\u001a\u00020\f*\u00020<H\u0002J\f\u0010=\u001a\u00020<*\u00020\fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006>"}, d2 = {"Lcom/careloop/app/data/repository/CommitmentRepository;", "", "commitmentDao", "Lcom/careloop/app/data/local/dao/CommitmentDao;", "adherenceLogDao", "Lcom/careloop/app/data/local/dao/AdherenceLogDao;", "(Lcom/careloop/app/data/local/dao/CommitmentDao;Lcom/careloop/app/data/local/dao/AdherenceLogDao;)V", "deleteAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllActiveCommitments", "", "Lcom/careloop/app/domain/model/Commitment;", "getAllCommitments", "Lkotlinx/coroutines/flow/Flow;", "getAllLogs", "Lcom/careloop/app/domain/model/AdherenceLog;", "getCommitmentById", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCommitmentBySourceId", "sourceId", "getLogsForCommitment", "commitmentId", "getTodayCommitments", "startOfDay", "", "endOfDay", "insertCommitment", "commitment", "(Lcom/careloop/app/domain/model/Commitment;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertCommitments", "commitments", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markCompleted", "method", "Lcom/careloop/app/domain/model/VerificationMethod;", "notes", "(Ljava/lang/String;Lcom/careloop/app/domain/model/VerificationMethod;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCommitmentDueAt", "newDueAt", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateCommitmentState", "newState", "Lcom/careloop/app/domain/model/CommitmentState;", "(Ljava/lang/String;Lcom/careloop/app/domain/model/CommitmentState;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateEscalationLevel", "level", "", "note", "(Ljava/lang/String;ILjava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateState", "action", "Lcom/careloop/app/domain/model/AdherenceAction;", "verifiedOnline", "", "(Ljava/lang/String;Lcom/careloop/app/domain/model/CommitmentState;Lcom/careloop/app/domain/model/AdherenceAction;Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toDomain", "Lcom/careloop/app/data/local/entity/AdherenceLogEntity;", "Lcom/careloop/app/data/local/entity/CommitmentEntity;", "toEntity", "app_demoDebug"})
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
    public final java.lang.Object getTodayCommitments(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.careloop.app.domain.model.Commitment>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllActiveCommitments(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.careloop.app.domain.model.Commitment>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object markCompleted(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.VerificationMethod method, @org.jetbrains.annotations.NotNull()
    java.lang.String notes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCommitmentDueAt(@org.jetbrains.annotations.NotNull()
    java.lang.String id, long newDueAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateCommitmentState(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.CommitmentState newState, @org.jetbrains.annotations.NotNull()
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