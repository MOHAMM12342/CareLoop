package com.careloop.app.agent;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007J\b\u0010\u0005\u001a\u00020\u0004H\u0007J\b\u0010\u0006\u001a\u00020\u0004H\u0007\u00a8\u0006\b"}, d2 = {"Lcom/careloop/app/agent/EscalationLadderAndAuditTest;", "", "()V", "testDataMinimisedMessageFormat", "", "testEscalationSchedulerDemoAcceleration", "testEscalationSchedulerMedicationMilestones", "EscalationSchedulerStub", "app_demoDebugUnitTest"})
public final class EscalationLadderAndAuditTest {
    
    public EscalationLadderAndAuditTest() {
        super();
    }
    
    @org.junit.Test()
    public final void testDataMinimisedMessageFormat() {
    }
    
    @org.junit.Test()
    public final void testEscalationSchedulerMedicationMilestones() {
    }
    
    @org.junit.Test()
    public final void testEscalationSchedulerDemoAcceleration() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001c\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t\u00a8\u0006\n"}, d2 = {"Lcom/careloop/app/agent/EscalationLadderAndAuditTest$EscalationSchedulerStub;", "", "()V", "computeMilestones", "", "Lcom/careloop/app/agent/EscalationMilestone;", "commitment", "Lcom/careloop/app/domain/model/Commitment;", "isDemoMode", "", "app_demoDebugUnitTest"})
    static final class EscalationSchedulerStub {
        
        public EscalationSchedulerStub() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.careloop.app.agent.EscalationMilestone> computeMilestones(@org.jetbrains.annotations.NotNull()
        com.careloop.app.domain.model.Commitment commitment, boolean isDemoMode) {
            return null;
        }
    }
}