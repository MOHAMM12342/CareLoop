package com.careloop.app.data.source;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0004H\u0096@\u00a2\u0006\u0002\u0010\nJ&\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0004J\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\f0\u0014H\u0096@\u00a2\u0006\u0002\u0010\u0015R\u0014\u0010\u0003\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0016"}, d2 = {"Lcom/careloop/app/data/source/ManualEntrySource;", "Lcom/careloop/app/data/source/HealthEventSource;", "()V", "sourceType", "", "getSourceType", "()Ljava/lang/String;", "checkEventCompletion", "", "sourceEventId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createEvent", "Lcom/careloop/app/domain/model/HealthEvent;", "type", "Lcom/careloop/app/domain/model/CommitmentType;", "title", "dueAt", "", "notes", "fetchEvents", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_playDebug"})
public final class ManualEntrySource implements com.careloop.app.data.source.HealthEventSource {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String sourceType = "MANUAL";
    
    public ManualEntrySource() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getSourceType() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object fetchEvents(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.careloop.app.domain.model.HealthEvent>> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object checkEventCompletion(@org.jetbrains.annotations.NotNull()
    java.lang.String sourceEventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.careloop.app.domain.model.HealthEvent createEvent(@org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.CommitmentType type, @org.jetbrains.annotations.NotNull()
    java.lang.String title, long dueAt, @org.jetbrains.annotations.NotNull()
    java.lang.String notes) {
        return null;
    }
}