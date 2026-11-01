package com.careloop.app.data.local.entity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b+\b\u0087\b\u0018\u00002\u00020\u0001Bq\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\b\u0010\u0011\u001a\u0004\u0018\u00010\t\u0012\u0006\u0010\u0012\u001a\u00020\t\u0012\u0006\u0010\u0013\u001a\u00020\u0003\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\u0002\u0010\u0016J\t\u0010-\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010.\u001a\u0004\u0018\u00010\tH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0018J\t\u0010/\u001a\u00020\tH\u00c6\u0003J\t\u00100\u001a\u00020\u0003H\u00c6\u0003J\t\u00101\u001a\u00020\u0015H\u00c6\u0003J\t\u00102\u001a\u00020\u0003H\u00c6\u0003J\t\u00103\u001a\u00020\u0006H\u00c6\u0003J\t\u00104\u001a\u00020\u0003H\u00c6\u0003J\t\u00105\u001a\u00020\tH\u00c6\u0003J\t\u00106\u001a\u00020\u000bH\u00c6\u0003J\t\u00107\u001a\u00020\rH\u00c6\u0003J\u000b\u00108\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u00109\u001a\u00020\u0010H\u00c6\u0003J\u0094\u0001\u0010:\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00062\b\b\u0002\u0010\u0007\u001a\u00020\u00032\b\b\u0002\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\r2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\t2\b\b\u0002\u0010\u0012\u001a\u00020\t2\b\b\u0002\u0010\u0013\u001a\u00020\u00032\b\b\u0002\u0010\u0014\u001a\u00020\u0015H\u00c6\u0001\u00a2\u0006\u0002\u0010;J\u0013\u0010<\u001a\u00020\u00152\b\u0010=\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010>\u001a\u00020\u0010H\u00d6\u0001J\t\u0010?\u001a\u00020\u0003H\u00d6\u0001R\u0015\u0010\u0011\u001a\u0004\u0018\u00010\t\u00a2\u0006\n\n\u0002\u0010\u0019\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0012\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0011\u0010\b\u001a\u00020\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001bR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001eR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0011\u0010\u0013\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b!\u0010 R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010 R\u0013\u0010\u000e\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b#\u0010 R\u0011\u0010\n\u001a\u00020\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b&\u0010 R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u0011\u0010\u0014\u001a\u00020\u0015\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010,\u00a8\u0006@"}, d2 = {"Lcom/careloop/app/data/local/entity/CommitmentEntity;", "", "id", "", "patientId", "type", "Lcom/careloop/app/domain/model/CommitmentType;", "title", "dueAt", "", "state", "Lcom/careloop/app/domain/model/CommitmentState;", "verificationMethod", "Lcom/careloop/app/domain/model/VerificationMethod;", "sourceEventId", "escalationLevel", "", "completedAt", "createdAt", "notes", "verifiedOnline", "", "(Ljava/lang/String;Ljava/lang/String;Lcom/careloop/app/domain/model/CommitmentType;Ljava/lang/String;JLcom/careloop/app/domain/model/CommitmentState;Lcom/careloop/app/domain/model/VerificationMethod;Ljava/lang/String;ILjava/lang/Long;JLjava/lang/String;Z)V", "getCompletedAt", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getCreatedAt", "()J", "getDueAt", "getEscalationLevel", "()I", "getId", "()Ljava/lang/String;", "getNotes", "getPatientId", "getSourceEventId", "getState", "()Lcom/careloop/app/domain/model/CommitmentState;", "getTitle", "getType", "()Lcom/careloop/app/domain/model/CommitmentType;", "getVerificationMethod", "()Lcom/careloop/app/domain/model/VerificationMethod;", "getVerifiedOnline", "()Z", "component1", "component10", "component11", "component12", "component13", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(Ljava/lang/String;Ljava/lang/String;Lcom/careloop/app/domain/model/CommitmentType;Ljava/lang/String;JLcom/careloop/app/domain/model/CommitmentState;Lcom/careloop/app/domain/model/VerificationMethod;Ljava/lang/String;ILjava/lang/Long;JLjava/lang/String;Z)Lcom/careloop/app/data/local/entity/CommitmentEntity;", "equals", "other", "hashCode", "toString", "app_debug"})
@androidx.room.Entity(tableName = "commitments")
public final class CommitmentEntity {
    @androidx.room.PrimaryKey()
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String patientId = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.domain.model.CommitmentType type = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String title = null;
    private final long dueAt = 0L;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.domain.model.CommitmentState state = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.domain.model.VerificationMethod verificationMethod = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String sourceEventId = null;
    private final int escalationLevel = 0;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long completedAt = null;
    private final long createdAt = 0L;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String notes = null;
    private final boolean verifiedOnline = false;
    
    public CommitmentEntity(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String patientId, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.CommitmentType type, @org.jetbrains.annotations.NotNull()
    java.lang.String title, long dueAt, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.CommitmentState state, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.VerificationMethod verificationMethod, @org.jetbrains.annotations.Nullable()
    java.lang.String sourceEventId, int escalationLevel, @org.jetbrains.annotations.Nullable()
    java.lang.Long completedAt, long createdAt, @org.jetbrains.annotations.NotNull()
    java.lang.String notes, boolean verifiedOnline) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getPatientId() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.careloop.app.domain.model.CommitmentType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    public final long getDueAt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.careloop.app.domain.model.CommitmentState getState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.careloop.app.domain.model.VerificationMethod getVerificationMethod() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSourceEventId() {
        return null;
    }
    
    public final int getEscalationLevel() {
        return 0;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getCompletedAt() {
        return null;
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getNotes() {
        return null;
    }
    
    public final boolean getVerifiedOnline() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component10() {
        return null;
    }
    
    public final long component11() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component12() {
        return null;
    }
    
    public final boolean component13() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.careloop.app.domain.model.CommitmentType component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component4() {
        return null;
    }
    
    public final long component5() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.careloop.app.domain.model.CommitmentState component6() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.careloop.app.domain.model.VerificationMethod component7() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component8() {
        return null;
    }
    
    public final int component9() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.careloop.app.data.local.entity.CommitmentEntity copy(@org.jetbrains.annotations.NotNull()
    java.lang.String id, @org.jetbrains.annotations.NotNull()
    java.lang.String patientId, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.CommitmentType type, @org.jetbrains.annotations.NotNull()
    java.lang.String title, long dueAt, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.CommitmentState state, @org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.VerificationMethod verificationMethod, @org.jetbrains.annotations.Nullable()
    java.lang.String sourceEventId, int escalationLevel, @org.jetbrains.annotations.Nullable()
    java.lang.Long completedAt, long createdAt, @org.jetbrains.annotations.NotNull()
    java.lang.String notes, boolean verifiedOnline) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}