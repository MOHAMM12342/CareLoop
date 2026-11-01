package com.careloop.app.agent;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\nJ \u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\f\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\nJ \u0010\r\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\u000e\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0016\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0015J\u0016\u0010\u0016\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086@\u00a2\u0006\u0002\u0010\u0015J \u0010\u0017\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\u0018\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u001aR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/careloop/app/agent/CommitmentEngine;", "", "commitmentRepository", "Lcom/careloop/app/data/repository/CommitmentRepository;", "(Lcom/careloop/app/data/repository/CommitmentRepository;)V", "autoCloseFromHealthRecord", "", "commitmentId", "", "evidenceDetail", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "cancelCommitment", "reason", "confirmCommitmentTaken", "note", "createCommitmentFromEvent", "Lcom/careloop/app/domain/model/Commitment;", "event", "Lcom/careloop/app/domain/model/HealthEvent;", "(Lcom/careloop/app/domain/model/HealthEvent;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markDue", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markMissed", "snoozeCommitment", "snoozeMinutes", "", "(Ljava/lang/String;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_demoDebug"})
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
    public final java.lang.Object markDue(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object cancelCommitment(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId, @org.jetbrains.annotations.NotNull()
    java.lang.String reason, @org.jetbrains.annotations.NotNull()
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