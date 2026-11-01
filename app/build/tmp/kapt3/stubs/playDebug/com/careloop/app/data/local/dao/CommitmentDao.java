package com.careloop.app.data.local.dao;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u000e\u0010\u0002\u001a\u00020\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0004J\u0014\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u0006H\'J\u0018\u0010\t\u001a\u0004\u0018\u00010\b2\u0006\u0010\n\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\u0018\u0010\r\u001a\u0004\u0018\u00010\b2\u0006\u0010\u000e\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\fJ\"\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\b0\u00072\f\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u0012J$\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\b0\u00070\u00062\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0015H\'J\u0016\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u0019J\u001c\u0010\u001a\u001a\u00020\u00032\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u00a7@\u00a2\u0006\u0002\u0010\u0012J\u0016\u0010\u001c\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\bH\u00a7@\u00a2\u0006\u0002\u0010\u0019J0\u0010\u001d\u001a\u00020\u00032\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u001e\u001a\u00020\u00112\b\u0010\u001f\u001a\u0004\u0018\u00010\u00152\u0006\u0010 \u001a\u00020!H\u00a7@\u00a2\u0006\u0002\u0010\"\u00a8\u0006#"}, d2 = {"Lcom/careloop/app/data/local/dao/CommitmentDao;", "", "deleteAllCommitments", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllCommitments", "Lkotlinx/coroutines/flow/Flow;", "", "Lcom/careloop/app/data/local/entity/CommitmentEntity;", "getCommitmentById", "id", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCommitmentBySourceId", "sourceEventId", "getCommitmentsInStates", "states", "Lcom/careloop/app/domain/model/CommitmentState;", "(Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTodayCommitments", "startOfDay", "", "endOfDay", "insertCommitment", "commitment", "(Lcom/careloop/app/data/local/entity/CommitmentEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "insertCommitments", "commitments", "updateCommitment", "updateState", "newState", "completedAt", "verifiedOnline", "", "(Ljava/lang/String;Lcom/careloop/app/domain/model/CommitmentState;Ljava/lang/Long;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_playDebug"})
@androidx.room.Dao()
public abstract interface CommitmentDao {
    
    @androidx.room.Query(value = "SELECT * FROM commitments ORDER BY dueAt ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.careloop.app.data.local.entity.CommitmentEntity>> getAllCommitments();
    
    @androidx.room.Query(value = "SELECT * FROM commitments WHERE dueAt BETWEEN :startOfDay AND :endOfDay ORDER BY dueAt ASC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.careloop.app.data.local.entity.CommitmentEntity>> getTodayCommitments(long startOfDay, long endOfDay);
    
    @androidx.room.Query(value = "SELECT * FROM commitments WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCommitmentById(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.careloop.app.data.local.entity.CommitmentEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM commitments WHERE sourceEventId = :sourceEventId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCommitmentBySourceId(@org.jetbrains.annotations.NotNull()
    java.lang.String sourceEventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.careloop.app.data.local.entity.CommitmentEntity> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM commitments WHERE state IN (:states)")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCommitmentsInStates(@org.jetbrains.annotations.NotNull()
    java.util.List<? extends com.careloop.app.domain.model.CommitmentState> states, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.careloop.app.data.local.entity.CommitmentEntity>> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertCommitment(@org.jetbrains.annotations.NotNull()
    com.careloop.app.data.local.entity.CommitmentEntity commitment, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insertCommitments(@org.jetbrains.annotations.NotNull()
    java.util.List<com.careloop.app.data.local.entity.CommitmentEntity> commitments, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateCommitment(@org.jetbrains.annotations.NotNull()
    com.careloop.app.data.local.entity.CommitmentEntity commitment, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE commitments SET state = :newState, completedAt = :completedAt, verifiedOnline = :verifiedOnline WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateState(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.CommitmentState newState, @org.jetbrains.annotations.Nullable()
    java.lang.Long completedAt, boolean verifiedOnline, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "DELETE FROM commitments")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteAllCommitments(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}