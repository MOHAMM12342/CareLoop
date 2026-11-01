package com.careloop.app.domain.escalation;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\bf\u0018\u0000 \u000b2\u00020\u0001:\u0001\u000bJ\u001e\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0003H\u00a6@\u00a2\u0006\u0002\u0010\nR\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u00a8\u0006\f"}, d2 = {"Lcom/careloop/app/domain/escalation/EscalationChannel;", "", "channelName", "", "getChannelName", "()Ljava/lang/String;", "sendEmergencyAlert", "Lcom/careloop/app/domain/escalation/EscalationResult;", "caregiverPhone", "patientName", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_demoDebug"})
public abstract interface EscalationChannel {
    @org.jetbrains.annotations.NotNull()
    public static final com.careloop.app.domain.escalation.EscalationChannel.Companion Companion = null;
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String getChannelName();
    
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object sendEmergencyAlert(@org.jetbrains.annotations.NotNull()
    java.lang.String caregiverPhone, @org.jetbrains.annotations.NotNull()
    java.lang.String patientName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.careloop.app.domain.escalation.EscalationResult> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004\u00a8\u0006\u0006"}, d2 = {"Lcom/careloop/app/domain/escalation/EscalationChannel$Companion;", "", "()V", "buildDataMinimisedMessage", "", "patientName", "app_demoDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Strictly data-minimised message required by privacy specifications.
         * NEVER contains medication names, dosages, or diagnostic info.
         */
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String buildDataMinimisedMessage(@org.jetbrains.annotations.NotNull()
        java.lang.String patientName) {
            return null;
        }
    }
}