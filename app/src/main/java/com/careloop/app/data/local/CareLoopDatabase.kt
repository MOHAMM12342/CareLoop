package com.careloop.app.data.local

import android.content.Context
import androidx.room.*
import com.careloop.app.data.local.dao.*
import com.careloop.app.data.local.entity.*
import com.careloop.app.domain.model.*
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

class Converters {
    @TypeConverter
    fun fromCommitmentType(value: CommitmentType): String = value.name

    @TypeConverter
    fun toCommitmentType(value: String): CommitmentType = CommitmentType.valueOf(value)

    @TypeConverter
    fun fromCommitmentState(value: CommitmentState): String = value.name

    @TypeConverter
    fun toCommitmentState(value: String): CommitmentState = CommitmentState.valueOf(value)

    @TypeConverter
    fun fromVerificationMethod(value: VerificationMethod): String = value.name

    @TypeConverter
    fun toVerificationMethod(value: String): VerificationMethod = VerificationMethod.valueOf(value)

    @TypeConverter
    fun fromAdherenceAction(value: AdherenceAction): String = value.name

    @TypeConverter
    fun toAdherenceAction(value: String): AdherenceAction = AdherenceAction.valueOf(value)

    @TypeConverter
    fun fromChannelPreference(value: ChannelPreference): String = value.name

    @TypeConverter
    fun toChannelPreference(value: String): ChannelPreference = ChannelPreference.valueOf(value)
}

@Database(
    entities = [
        PatientEntity::class,
        MedicationEntity::class,
        CommitmentEntity::class,
        AdherenceLogEntity::class,
        CaregiverEntity::class,
        HealthEventCacheEntity::class,
        SettingEntity::class,
        CachedPharmacyEntity::class,
        EscalationAuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CareLoopDatabase : RoomDatabase() {
    abstract fun commitmentDao(): CommitmentDao
    abstract fun caregiverDao(): CaregiverDao
    abstract fun adherenceLogDao(): AdherenceLogDao
    abstract fun settingDao(): SettingDao
    abstract fun pharmacyDao(): PharmacyDao
    abstract fun escalationAuditLogDao(): EscalationAuditLogDao

    companion object {
        @Volatile
        private var INSTANCE: CareLoopDatabase? = null

        fun getInstance(context: Context, useEncryption: Boolean = true): CareLoopDatabase {
            return INSTANCE ?: synchronized(this) {
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    CareLoopDatabase::class.java,
                    "careloop_encrypted.db"
                ).fallbackToDestructiveMigration()

                if (useEncryption) {
                    val passphrase = SecurityKeyManager.getOrCreatePassphrase(context)
                    val factory = SupportFactory(passphrase)
                    builder.openHelperFactory(factory)
                }

                val instance = builder.build()
                INSTANCE = instance
                instance
            }
        }
    }
}

