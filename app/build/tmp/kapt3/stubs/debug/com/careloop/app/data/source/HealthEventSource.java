package com.careloop.app.data.source;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0016\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\tJ\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u00a6@\u00a2\u0006\u0002\u0010\rR\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\u000e"}, d2 = {"Lcom/careloop/app/data/source/HealthEventSource;", "", "sourceType", "", "getSourceType", "()Ljava/lang/String;", "checkEventCompletion", "", "sourceEventId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fetchEvents", "", "Lcom/careloop/app/domain/model/HealthEvent;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface HealthEventSource {
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getSourceType();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object fetchEvents(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.careloop.app.domain.model.HealthEvent>> $completion);
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object checkEventCompletion(@org.jetbrains.annotations.NotNull()
    java.lang.String sourceEventId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion);
}