package com.careloop.app.domain.escalation;

/**
 * Play flavor channel: User-initiated pre-filled SMS intent (Intent.ACTION_SENDTO).
 * Strictly compliant with Google Play Store SMS/Call log restrictions.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\rR\u0014\u0010\u0005\u001a\u00020\u0006X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000e"}, d2 = {"Lcom/careloop/app/domain/escalation/PlaySmsChannel;", "Lcom/careloop/app/domain/escalation/EscalationChannel;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "channelName", "", "getChannelName", "()Ljava/lang/String;", "sendEmergencyAlert", "Lcom/careloop/app/domain/escalation/EscalationResult;", "caregiverPhone", "patientName", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_demoDebug"})
public final class PlaySmsChannel implements com.careloop.app.domain.escalation.EscalationChannel {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String channelName = "PLAY_SMS_INTENT";
    
    public PlaySmsChannel(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getChannelName() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object sendEmergencyAlert(@org.jetbrains.annotations.NotNull()
    java.lang.String caregiverPhone, @org.jetbrains.annotations.NotNull()
    java.lang.String patientName, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.careloop.app.domain.escalation.EscalationResult> $completion) {
        return null;
    }
}