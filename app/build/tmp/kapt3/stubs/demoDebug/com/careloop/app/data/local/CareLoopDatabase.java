package com.careloop.app.data.local;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&J\b\u0010\u0007\u001a\u00020\bH&J\b\u0010\t\u001a\u00020\nH&J\b\u0010\u000b\u001a\u00020\fH&J\b\u0010\r\u001a\u00020\u000eH&\u00a8\u0006\u0010"}, d2 = {"Lcom/careloop/app/data/local/CareLoopDatabase;", "Landroidx/room/RoomDatabase;", "()V", "adherenceLogDao", "Lcom/careloop/app/data/local/dao/AdherenceLogDao;", "caregiverDao", "Lcom/careloop/app/data/local/dao/CaregiverDao;", "commitmentDao", "Lcom/careloop/app/data/local/dao/CommitmentDao;", "escalationAuditLogDao", "Lcom/careloop/app/data/local/dao/EscalationAuditLogDao;", "pharmacyDao", "Lcom/careloop/app/data/local/dao/PharmacyDao;", "settingDao", "Lcom/careloop/app/data/local/dao/SettingDao;", "Companion", "app_demoDebug"})
@androidx.room.Database(entities = {com.careloop.app.data.local.entity.PatientEntity.class, com.careloop.app.data.local.entity.MedicationEntity.class, com.careloop.app.data.local.entity.CommitmentEntity.class, com.careloop.app.data.local.entity.AdherenceLogEntity.class, com.careloop.app.data.local.entity.CaregiverEntity.class, com.careloop.app.data.local.entity.HealthEventCacheEntity.class, com.careloop.app.data.local.entity.SettingEntity.class, com.careloop.app.data.local.entity.CachedPharmacyEntity.class, com.careloop.app.data.local.entity.EscalationAuditLogEntity.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters(value = {com.careloop.app.data.local.Converters.class})
public abstract class CareLoopDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.careloop.app.data.local.CareLoopDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.careloop.app.data.local.CareLoopDatabase.Companion Companion = null;
    
    public CareLoopDatabase() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.careloop.app.data.local.dao.CommitmentDao commitmentDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.careloop.app.data.local.dao.CaregiverDao caregiverDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.careloop.app.data.local.dao.AdherenceLogDao adherenceLogDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.careloop.app.data.local.dao.SettingDao settingDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.careloop.app.data.local.dao.PharmacyDao pharmacyDao();
    
    @org.jetbrains.annotations.NotNull()
    public abstract com.careloop.app.data.local.dao.EscalationAuditLogDao escalationAuditLogDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\tR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/careloop/app/data/local/CareLoopDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/careloop/app/data/local/CareLoopDatabase;", "getInstance", "context", "Landroid/content/Context;", "useEncryption", "", "app_demoDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.careloop.app.data.local.CareLoopDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context, boolean useEncryption) {
            return null;
        }
    }
}