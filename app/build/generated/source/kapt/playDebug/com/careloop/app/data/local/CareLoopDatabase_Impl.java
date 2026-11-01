package com.careloop.app.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.careloop.app.data.local.dao.AdherenceLogDao;
import com.careloop.app.data.local.dao.AdherenceLogDao_Impl;
import com.careloop.app.data.local.dao.CaregiverDao;
import com.careloop.app.data.local.dao.CaregiverDao_Impl;
import com.careloop.app.data.local.dao.CommitmentDao;
import com.careloop.app.data.local.dao.CommitmentDao_Impl;
import com.careloop.app.data.local.dao.EscalationAuditLogDao;
import com.careloop.app.data.local.dao.EscalationAuditLogDao_Impl;
import com.careloop.app.data.local.dao.PharmacyDao;
import com.careloop.app.data.local.dao.PharmacyDao_Impl;
import com.careloop.app.data.local.dao.SettingDao;
import com.careloop.app.data.local.dao.SettingDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CareLoopDatabase_Impl extends CareLoopDatabase {
  private volatile CommitmentDao _commitmentDao;

  private volatile CaregiverDao _caregiverDao;

  private volatile AdherenceLogDao _adherenceLogDao;

  private volatile SettingDao _settingDao;

  private volatile PharmacyDao _pharmacyDao;

  private volatile EscalationAuditLogDao _escalationAuditLogDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `patients` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `birthDate` TEXT NOT NULL, `emergencyNote` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `medications` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `dosageForm` TEXT NOT NULL, `strength` TEXT NOT NULL, `instructions` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `commitments` (`id` TEXT NOT NULL, `patientId` TEXT NOT NULL, `type` TEXT NOT NULL, `title` TEXT NOT NULL, `dueAt` INTEGER NOT NULL, `state` TEXT NOT NULL, `verificationMethod` TEXT NOT NULL, `sourceEventId` TEXT, `escalationLevel` INTEGER NOT NULL, `completedAt` INTEGER, `createdAt` INTEGER NOT NULL, `notes` TEXT NOT NULL, `verifiedOnline` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `adherence_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `commitmentId` TEXT NOT NULL, `expectedAt` INTEGER NOT NULL, `actedAt` INTEGER NOT NULL, `action` TEXT NOT NULL, `note` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `caregivers` (`id` TEXT NOT NULL, `patientId` TEXT NOT NULL, `name` TEXT NOT NULL, `relation` TEXT NOT NULL, `phone` TEXT NOT NULL, `email` TEXT NOT NULL, `fcmToken` TEXT, `priorityRank` INTEGER NOT NULL, `channelPreference` TEXT NOT NULL, `consentGrantedAt` INTEGER, `pairingToken` TEXT, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `health_events_cache` (`id` TEXT NOT NULL, `sourceType` TEXT NOT NULL, `rawJson` TEXT NOT NULL, `receivedAt` INTEGER NOT NULL, `processedAt` INTEGER, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `settings` (`key` TEXT NOT NULL, `value` TEXT NOT NULL, PRIMARY KEY(`key`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `cached_pharmacies` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `address` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `phone` TEXT, `cachedAt` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `escalation_audit_log` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `commitmentId` TEXT NOT NULL, `whoNotified` TEXT NOT NULL, `notifiedAt` INTEGER NOT NULL, `channel` TEXT NOT NULL, `outcome` TEXT NOT NULL, `dataMinimisedMessage` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '72ac754ed0f8e795b6e74de461b5b921')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `patients`");
        db.execSQL("DROP TABLE IF EXISTS `medications`");
        db.execSQL("DROP TABLE IF EXISTS `commitments`");
        db.execSQL("DROP TABLE IF EXISTS `adherence_logs`");
        db.execSQL("DROP TABLE IF EXISTS `caregivers`");
        db.execSQL("DROP TABLE IF EXISTS `health_events_cache`");
        db.execSQL("DROP TABLE IF EXISTS `settings`");
        db.execSQL("DROP TABLE IF EXISTS `cached_pharmacies`");
        db.execSQL("DROP TABLE IF EXISTS `escalation_audit_log`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsPatients = new HashMap<String, TableInfo.Column>(4);
        _columnsPatients.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("birthDate", new TableInfo.Column("birthDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPatients.put("emergencyNote", new TableInfo.Column("emergencyNote", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPatients = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPatients = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPatients = new TableInfo("patients", _columnsPatients, _foreignKeysPatients, _indicesPatients);
        final TableInfo _existingPatients = TableInfo.read(db, "patients");
        if (!_infoPatients.equals(_existingPatients)) {
          return new RoomOpenHelper.ValidationResult(false, "patients(com.careloop.app.data.local.entity.PatientEntity).\n"
                  + " Expected:\n" + _infoPatients + "\n"
                  + " Found:\n" + _existingPatients);
        }
        final HashMap<String, TableInfo.Column> _columnsMedications = new HashMap<String, TableInfo.Column>(5);
        _columnsMedications.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("dosageForm", new TableInfo.Column("dosageForm", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("strength", new TableInfo.Column("strength", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMedications.put("instructions", new TableInfo.Column("instructions", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMedications = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMedications = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMedications = new TableInfo("medications", _columnsMedications, _foreignKeysMedications, _indicesMedications);
        final TableInfo _existingMedications = TableInfo.read(db, "medications");
        if (!_infoMedications.equals(_existingMedications)) {
          return new RoomOpenHelper.ValidationResult(false, "medications(com.careloop.app.data.local.entity.MedicationEntity).\n"
                  + " Expected:\n" + _infoMedications + "\n"
                  + " Found:\n" + _existingMedications);
        }
        final HashMap<String, TableInfo.Column> _columnsCommitments = new HashMap<String, TableInfo.Column>(13);
        _columnsCommitments.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("patientId", new TableInfo.Column("patientId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("type", new TableInfo.Column("type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("dueAt", new TableInfo.Column("dueAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("state", new TableInfo.Column("state", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("verificationMethod", new TableInfo.Column("verificationMethod", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("sourceEventId", new TableInfo.Column("sourceEventId", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("escalationLevel", new TableInfo.Column("escalationLevel", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("completedAt", new TableInfo.Column("completedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("notes", new TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCommitments.put("verifiedOnline", new TableInfo.Column("verifiedOnline", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCommitments = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCommitments = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCommitments = new TableInfo("commitments", _columnsCommitments, _foreignKeysCommitments, _indicesCommitments);
        final TableInfo _existingCommitments = TableInfo.read(db, "commitments");
        if (!_infoCommitments.equals(_existingCommitments)) {
          return new RoomOpenHelper.ValidationResult(false, "commitments(com.careloop.app.data.local.entity.CommitmentEntity).\n"
                  + " Expected:\n" + _infoCommitments + "\n"
                  + " Found:\n" + _existingCommitments);
        }
        final HashMap<String, TableInfo.Column> _columnsAdherenceLogs = new HashMap<String, TableInfo.Column>(6);
        _columnsAdherenceLogs.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdherenceLogs.put("commitmentId", new TableInfo.Column("commitmentId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdherenceLogs.put("expectedAt", new TableInfo.Column("expectedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdherenceLogs.put("actedAt", new TableInfo.Column("actedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdherenceLogs.put("action", new TableInfo.Column("action", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAdherenceLogs.put("note", new TableInfo.Column("note", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAdherenceLogs = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAdherenceLogs = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAdherenceLogs = new TableInfo("adherence_logs", _columnsAdherenceLogs, _foreignKeysAdherenceLogs, _indicesAdherenceLogs);
        final TableInfo _existingAdherenceLogs = TableInfo.read(db, "adherence_logs");
        if (!_infoAdherenceLogs.equals(_existingAdherenceLogs)) {
          return new RoomOpenHelper.ValidationResult(false, "adherence_logs(com.careloop.app.data.local.entity.AdherenceLogEntity).\n"
                  + " Expected:\n" + _infoAdherenceLogs + "\n"
                  + " Found:\n" + _existingAdherenceLogs);
        }
        final HashMap<String, TableInfo.Column> _columnsCaregivers = new HashMap<String, TableInfo.Column>(11);
        _columnsCaregivers.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("patientId", new TableInfo.Column("patientId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("relation", new TableInfo.Column("relation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("phone", new TableInfo.Column("phone", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("email", new TableInfo.Column("email", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("fcmToken", new TableInfo.Column("fcmToken", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("priorityRank", new TableInfo.Column("priorityRank", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("channelPreference", new TableInfo.Column("channelPreference", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("consentGrantedAt", new TableInfo.Column("consentGrantedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCaregivers.put("pairingToken", new TableInfo.Column("pairingToken", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCaregivers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCaregivers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCaregivers = new TableInfo("caregivers", _columnsCaregivers, _foreignKeysCaregivers, _indicesCaregivers);
        final TableInfo _existingCaregivers = TableInfo.read(db, "caregivers");
        if (!_infoCaregivers.equals(_existingCaregivers)) {
          return new RoomOpenHelper.ValidationResult(false, "caregivers(com.careloop.app.data.local.entity.CaregiverEntity).\n"
                  + " Expected:\n" + _infoCaregivers + "\n"
                  + " Found:\n" + _existingCaregivers);
        }
        final HashMap<String, TableInfo.Column> _columnsHealthEventsCache = new HashMap<String, TableInfo.Column>(5);
        _columnsHealthEventsCache.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEventsCache.put("sourceType", new TableInfo.Column("sourceType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEventsCache.put("rawJson", new TableInfo.Column("rawJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEventsCache.put("receivedAt", new TableInfo.Column("receivedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsHealthEventsCache.put("processedAt", new TableInfo.Column("processedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysHealthEventsCache = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesHealthEventsCache = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoHealthEventsCache = new TableInfo("health_events_cache", _columnsHealthEventsCache, _foreignKeysHealthEventsCache, _indicesHealthEventsCache);
        final TableInfo _existingHealthEventsCache = TableInfo.read(db, "health_events_cache");
        if (!_infoHealthEventsCache.equals(_existingHealthEventsCache)) {
          return new RoomOpenHelper.ValidationResult(false, "health_events_cache(com.careloop.app.data.local.entity.HealthEventCacheEntity).\n"
                  + " Expected:\n" + _infoHealthEventsCache + "\n"
                  + " Found:\n" + _existingHealthEventsCache);
        }
        final HashMap<String, TableInfo.Column> _columnsSettings = new HashMap<String, TableInfo.Column>(2);
        _columnsSettings.put("key", new TableInfo.Column("key", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSettings.put("value", new TableInfo.Column("value", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSettings = new TableInfo("settings", _columnsSettings, _foreignKeysSettings, _indicesSettings);
        final TableInfo _existingSettings = TableInfo.read(db, "settings");
        if (!_infoSettings.equals(_existingSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "settings(com.careloop.app.data.local.entity.SettingEntity).\n"
                  + " Expected:\n" + _infoSettings + "\n"
                  + " Found:\n" + _existingSettings);
        }
        final HashMap<String, TableInfo.Column> _columnsCachedPharmacies = new HashMap<String, TableInfo.Column>(7);
        _columnsCachedPharmacies.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPharmacies.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPharmacies.put("address", new TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPharmacies.put("latitude", new TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPharmacies.put("longitude", new TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPharmacies.put("phone", new TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCachedPharmacies.put("cachedAt", new TableInfo.Column("cachedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCachedPharmacies = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCachedPharmacies = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCachedPharmacies = new TableInfo("cached_pharmacies", _columnsCachedPharmacies, _foreignKeysCachedPharmacies, _indicesCachedPharmacies);
        final TableInfo _existingCachedPharmacies = TableInfo.read(db, "cached_pharmacies");
        if (!_infoCachedPharmacies.equals(_existingCachedPharmacies)) {
          return new RoomOpenHelper.ValidationResult(false, "cached_pharmacies(com.careloop.app.data.local.entity.CachedPharmacyEntity).\n"
                  + " Expected:\n" + _infoCachedPharmacies + "\n"
                  + " Found:\n" + _existingCachedPharmacies);
        }
        final HashMap<String, TableInfo.Column> _columnsEscalationAuditLog = new HashMap<String, TableInfo.Column>(7);
        _columnsEscalationAuditLog.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEscalationAuditLog.put("commitmentId", new TableInfo.Column("commitmentId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEscalationAuditLog.put("whoNotified", new TableInfo.Column("whoNotified", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEscalationAuditLog.put("notifiedAt", new TableInfo.Column("notifiedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEscalationAuditLog.put("channel", new TableInfo.Column("channel", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEscalationAuditLog.put("outcome", new TableInfo.Column("outcome", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEscalationAuditLog.put("dataMinimisedMessage", new TableInfo.Column("dataMinimisedMessage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEscalationAuditLog = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEscalationAuditLog = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEscalationAuditLog = new TableInfo("escalation_audit_log", _columnsEscalationAuditLog, _foreignKeysEscalationAuditLog, _indicesEscalationAuditLog);
        final TableInfo _existingEscalationAuditLog = TableInfo.read(db, "escalation_audit_log");
        if (!_infoEscalationAuditLog.equals(_existingEscalationAuditLog)) {
          return new RoomOpenHelper.ValidationResult(false, "escalation_audit_log(com.careloop.app.data.local.entity.EscalationAuditLogEntity).\n"
                  + " Expected:\n" + _infoEscalationAuditLog + "\n"
                  + " Found:\n" + _existingEscalationAuditLog);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "72ac754ed0f8e795b6e74de461b5b921", "8dad0442fea200720d6af3a1a4125f3d");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "patients","medications","commitments","adherence_logs","caregivers","health_events_cache","settings","cached_pharmacies","escalation_audit_log");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `patients`");
      _db.execSQL("DELETE FROM `medications`");
      _db.execSQL("DELETE FROM `commitments`");
      _db.execSQL("DELETE FROM `adherence_logs`");
      _db.execSQL("DELETE FROM `caregivers`");
      _db.execSQL("DELETE FROM `health_events_cache`");
      _db.execSQL("DELETE FROM `settings`");
      _db.execSQL("DELETE FROM `cached_pharmacies`");
      _db.execSQL("DELETE FROM `escalation_audit_log`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(CommitmentDao.class, CommitmentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CaregiverDao.class, CaregiverDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AdherenceLogDao.class, AdherenceLogDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SettingDao.class, SettingDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PharmacyDao.class, PharmacyDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(EscalationAuditLogDao.class, EscalationAuditLogDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public CommitmentDao commitmentDao() {
    if (_commitmentDao != null) {
      return _commitmentDao;
    } else {
      synchronized(this) {
        if(_commitmentDao == null) {
          _commitmentDao = new CommitmentDao_Impl(this);
        }
        return _commitmentDao;
      }
    }
  }

  @Override
  public CaregiverDao caregiverDao() {
    if (_caregiverDao != null) {
      return _caregiverDao;
    } else {
      synchronized(this) {
        if(_caregiverDao == null) {
          _caregiverDao = new CaregiverDao_Impl(this);
        }
        return _caregiverDao;
      }
    }
  }

  @Override
  public AdherenceLogDao adherenceLogDao() {
    if (_adherenceLogDao != null) {
      return _adherenceLogDao;
    } else {
      synchronized(this) {
        if(_adherenceLogDao == null) {
          _adherenceLogDao = new AdherenceLogDao_Impl(this);
        }
        return _adherenceLogDao;
      }
    }
  }

  @Override
  public SettingDao settingDao() {
    if (_settingDao != null) {
      return _settingDao;
    } else {
      synchronized(this) {
        if(_settingDao == null) {
          _settingDao = new SettingDao_Impl(this);
        }
        return _settingDao;
      }
    }
  }

  @Override
  public PharmacyDao pharmacyDao() {
    if (_pharmacyDao != null) {
      return _pharmacyDao;
    } else {
      synchronized(this) {
        if(_pharmacyDao == null) {
          _pharmacyDao = new PharmacyDao_Impl(this);
        }
        return _pharmacyDao;
      }
    }
  }

  @Override
  public EscalationAuditLogDao escalationAuditLogDao() {
    if (_escalationAuditLogDao != null) {
      return _escalationAuditLogDao;
    } else {
      synchronized(this) {
        if(_escalationAuditLogDao == null) {
          _escalationAuditLogDao = new EscalationAuditLogDao_Impl(this);
        }
        return _escalationAuditLogDao;
      }
    }
  }
}
