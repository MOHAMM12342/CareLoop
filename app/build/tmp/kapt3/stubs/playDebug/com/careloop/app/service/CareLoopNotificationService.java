package com.careloop.app.service;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u0000 \u00122\u00020\u0001:\u0001\u0012B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0002J\u000e\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000bJ \u0010\f\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\b\b\u0002\u0010\u000e\u001a\u00020\u000fJ\u0016\u0010\u0010\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2 = {"Lcom/careloop/app/service/CareLoopNotificationService;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "notificationManager", "Landroid/app/NotificationManager;", "createNotificationChannels", "", "dismissNotification", "commitmentId", "", "showCommitmentNotification", "title", "isReNotification", "", "showEscalationStatusNotification", "message", "Companion", "app_playDebug"})
public final class CareLoopNotificationService {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.NotificationManager notificationManager = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_REMINDERS = "careloop_reminders_channel";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String CHANNEL_ESCALATIONS = "careloop_escalations_channel";
    @org.jetbrains.annotations.NotNull()
    public static final com.careloop.app.service.CareLoopNotificationService.Companion Companion = null;
    
    public CareLoopNotificationService(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    private final void createNotificationChannels() {
    }
    
    public final void showCommitmentNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId, @org.jetbrains.annotations.NotNull()
    java.lang.String title, boolean isReNotification) {
    }
    
    public final void dismissNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId) {
    }
    
    public final void showEscalationStatusNotification(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String message) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0006"}, d2 = {"Lcom/careloop/app/service/CareLoopNotificationService$Companion;", "", "()V", "CHANNEL_ESCALATIONS", "", "CHANNEL_REMINDERS", "app_playDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}