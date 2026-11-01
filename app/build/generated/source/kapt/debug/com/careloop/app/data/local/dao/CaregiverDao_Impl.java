package com.careloop.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.careloop.app.data.local.Converters;
import com.careloop.app.data.local.entity.CaregiverEntity;
import com.careloop.app.domain.model.ChannelPreference;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CaregiverDao_Impl implements CaregiverDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CaregiverEntity> __insertionAdapterOfCaregiverEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<CaregiverEntity> __deletionAdapterOfCaregiverEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllCaregivers;

  public CaregiverDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCaregiverEntity = new EntityInsertionAdapter<CaregiverEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `caregivers` (`id`,`patientId`,`name`,`relation`,`phone`,`email`,`fcmToken`,`priorityRank`,`channelPreference`,`consentGrantedAt`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CaregiverEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
        if (entity.getPatientId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getPatientId());
        }
        if (entity.getName() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getName());
        }
        if (entity.getRelation() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getRelation());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getPhone());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getEmail());
        }
        if (entity.getFcmToken() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getFcmToken());
        }
        statement.bindLong(8, entity.getPriorityRank());
        final String _tmp = __converters.fromChannelPreference(entity.getChannelPreference());
        if (_tmp == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp);
        }
        if (entity.getConsentGrantedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getConsentGrantedAt());
        }
      }
    };
    this.__deletionAdapterOfCaregiverEntity = new EntityDeletionOrUpdateAdapter<CaregiverEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `caregivers` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CaregiverEntity entity) {
        if (entity.getId() == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.getId());
        }
      }
    };
    this.__preparedStmtOfDeleteAllCaregivers = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM caregivers";
        return _query;
      }
    };
  }

  @Override
  public Object insertCaregiver(final CaregiverEntity caregiver,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCaregiverEntity.insert(caregiver);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCaregiver(final CaregiverEntity caregiver,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfCaregiverEntity.handle(caregiver);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllCaregivers(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllCaregivers.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllCaregivers.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CaregiverEntity>> getAllCaregivers() {
    final String _sql = "SELECT * FROM caregivers ORDER BY priorityRank ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"caregivers"}, new Callable<List<CaregiverEntity>>() {
      @Override
      @NonNull
      public List<CaregiverEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patientId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRelation = CursorUtil.getColumnIndexOrThrow(_cursor, "relation");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfFcmToken = CursorUtil.getColumnIndexOrThrow(_cursor, "fcmToken");
          final int _cursorIndexOfPriorityRank = CursorUtil.getColumnIndexOrThrow(_cursor, "priorityRank");
          final int _cursorIndexOfChannelPreference = CursorUtil.getColumnIndexOrThrow(_cursor, "channelPreference");
          final int _cursorIndexOfConsentGrantedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "consentGrantedAt");
          final List<CaregiverEntity> _result = new ArrayList<CaregiverEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CaregiverEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpRelation;
            if (_cursor.isNull(_cursorIndexOfRelation)) {
              _tmpRelation = null;
            } else {
              _tmpRelation = _cursor.getString(_cursorIndexOfRelation);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpFcmToken;
            if (_cursor.isNull(_cursorIndexOfFcmToken)) {
              _tmpFcmToken = null;
            } else {
              _tmpFcmToken = _cursor.getString(_cursorIndexOfFcmToken);
            }
            final int _tmpPriorityRank;
            _tmpPriorityRank = _cursor.getInt(_cursorIndexOfPriorityRank);
            final ChannelPreference _tmpChannelPreference;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfChannelPreference)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfChannelPreference);
            }
            _tmpChannelPreference = __converters.toChannelPreference(_tmp);
            final Long _tmpConsentGrantedAt;
            if (_cursor.isNull(_cursorIndexOfConsentGrantedAt)) {
              _tmpConsentGrantedAt = null;
            } else {
              _tmpConsentGrantedAt = _cursor.getLong(_cursorIndexOfConsentGrantedAt);
            }
            _item = new CaregiverEntity(_tmpId,_tmpPatientId,_tmpName,_tmpRelation,_tmpPhone,_tmpEmail,_tmpFcmToken,_tmpPriorityRank,_tmpChannelPreference,_tmpConsentGrantedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getCaregiversList(final Continuation<? super List<CaregiverEntity>> $completion) {
    final String _sql = "SELECT * FROM caregivers ORDER BY priorityRank ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CaregiverEntity>>() {
      @Override
      @NonNull
      public List<CaregiverEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patientId");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfRelation = CursorUtil.getColumnIndexOrThrow(_cursor, "relation");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfFcmToken = CursorUtil.getColumnIndexOrThrow(_cursor, "fcmToken");
          final int _cursorIndexOfPriorityRank = CursorUtil.getColumnIndexOrThrow(_cursor, "priorityRank");
          final int _cursorIndexOfChannelPreference = CursorUtil.getColumnIndexOrThrow(_cursor, "channelPreference");
          final int _cursorIndexOfConsentGrantedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "consentGrantedAt");
          final List<CaregiverEntity> _result = new ArrayList<CaregiverEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CaregiverEntity _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpPatientId;
            if (_cursor.isNull(_cursorIndexOfPatientId)) {
              _tmpPatientId = null;
            } else {
              _tmpPatientId = _cursor.getString(_cursorIndexOfPatientId);
            }
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpRelation;
            if (_cursor.isNull(_cursorIndexOfRelation)) {
              _tmpRelation = null;
            } else {
              _tmpRelation = _cursor.getString(_cursorIndexOfRelation);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpEmail;
            if (_cursor.isNull(_cursorIndexOfEmail)) {
              _tmpEmail = null;
            } else {
              _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            }
            final String _tmpFcmToken;
            if (_cursor.isNull(_cursorIndexOfFcmToken)) {
              _tmpFcmToken = null;
            } else {
              _tmpFcmToken = _cursor.getString(_cursorIndexOfFcmToken);
            }
            final int _tmpPriorityRank;
            _tmpPriorityRank = _cursor.getInt(_cursorIndexOfPriorityRank);
            final ChannelPreference _tmpChannelPreference;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfChannelPreference)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfChannelPreference);
            }
            _tmpChannelPreference = __converters.toChannelPreference(_tmp);
            final Long _tmpConsentGrantedAt;
            if (_cursor.isNull(_cursorIndexOfConsentGrantedAt)) {
              _tmpConsentGrantedAt = null;
            } else {
              _tmpConsentGrantedAt = _cursor.getLong(_cursorIndexOfConsentGrantedAt);
            }
            _item = new CaregiverEntity(_tmpId,_tmpPatientId,_tmpName,_tmpRelation,_tmpPhone,_tmpEmail,_tmpFcmToken,_tmpPriorityRank,_tmpChannelPreference,_tmpConsentGrantedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
