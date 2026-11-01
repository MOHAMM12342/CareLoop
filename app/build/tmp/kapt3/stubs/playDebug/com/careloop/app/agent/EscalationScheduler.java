package com.careloop.app.agent;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u001e\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000e2\u0006\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0013J\u000e\u0010\u0014\u001a\u00020\n2\u0006\u0010\u0015\u001a\u00020\u000fR\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/careloop/app/agent/EscalationScheduler;", "", "context", "Landroid/content/Context;", "alarmNegotiator", "Lcom/careloop/app/agent/AlarmCapabilityNegotiator;", "(Landroid/content/Context;Lcom/careloop/app/agent/AlarmCapabilityNegotiator;)V", "alarmManager", "Landroid/app/AlarmManager;", "cancelAllMilestones", "", "commitmentId", "", "computeMilestones", "", "Lcom/careloop/app/agent/EscalationMilestone;", "commitment", "Lcom/careloop/app/domain/model/Commitment;", "isDemoMode", "", "scheduleMilestone", "milestone", "app_playDebug"})
public final class EscalationScheduler {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final com.careloop.app.agent.AlarmCapabilityNegotiator alarmNegotiator = null;
    @org.jetbrains.annotations.NotNull()
    private final android.app.AlarmManager alarmManager = null;
    
    public EscalationScheduler(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.careloop.app.agent.AlarmCapabilityNegotiator alarmNegotiator) {
        super();
    }
    
    /**
     * Computes the escalation schedule for a commitment.
     * @param isDemoMode If true, intervals are accelerated (seconds instead of minutes) for stage demos.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.careloop.app.agent.EscalationMilestone> computeMilestones(@org.jetbrains.annotations.NotNull()
    com.careloop.app.domain.model.Commitment commitment, boolean isDemoMode) {
        return null;
    }
    
    /**
     * Schedules the next escalation milestone.
     */
    public final void scheduleMilestone(@org.jetbrains.annotations.NotNull()
    com.careloop.app.agent.EscalationMilestone milestone) {
    }
    
    /**
     * Cancels all escalation milestones for a commitment (e.g. when Taken or Cancelled).
     */
    public final void cancelAllMilestones(@org.jetbrains.annotations.NotNull()
    java.lang.String commitmentId) {
    }
}