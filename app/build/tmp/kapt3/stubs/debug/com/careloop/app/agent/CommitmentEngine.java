package com.careloop.app.agent;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\nJ \u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0013J \u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\u0015\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u0017R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/careloop/app/agent/CommitmentEngine;", "", "commitmentRepository", "Lcom/careloop/app/data/repository/CommitmentRepository;", "(Lcom/careloop/app/data/repository/CommitmentRepository;)V", "autoCloseFromHealthRecord", "", "commitmentId", "", "evidenceDetail", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "confirmCommitmentTaken", "note", "createCommitmentFromEvent", "Lcom/careloop/app/domain/model/Commitment;", "event", "Lcom/careloop/app/domain/model/HealthEvent;", "(Lcom/careloop/app/domain/model/HealthEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markMissed", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "snoozeCommitment", "snoozeMinutes", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class CommitmentEngine {
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.data.repository.CommitmentRepository commitmentRepository = null;
    
    public CommitmentEngine(@org.jetbrains.annotations.NotNull()
    com.careloop.app.data.repository.CommitmentRepository commitmentRepository) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createCommitmentFromEvent(@org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.HealthEvent event, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.careloop.app.domain.model.Commitment> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object confirmCommitmentTaken(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId, @org.jetbrains.annotations.NotNull()
    java.lang.String note, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object snoozeCommitment(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId, int snoozeMinutes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object markMissed(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object autoCloseFromHealthRecord(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId, @org.jetbrains.annotations.NotNull()
    java.lang.String evidenceDetail, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}