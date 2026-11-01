package com.careloop.app.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.careloop.app.data.local.Converters;
import com.careloop.app.data.local.entity.AdherenceLogEntity;
import com.careloop.app.domain.model.AdherenceAction;
import java.lang.Class;
import java.lang.Exception;
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
public final class AdherenceLogDao_Impl implements AdherenceLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AdherenceLogEntity> __insertionAdapterOfAdherenceLogEntity;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllLogs;

  public AdherenceLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAdherenceLogEntity = new EntityInsertionAdapter<AdherenceLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `adherence_logs` (`id`,`commitmentId`,`expectedAt`,`actedAt`,`action`,`note`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AdherenceLogEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCommitmentId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCommitmentId());
        }
        statement.bindLong(3, entity.getExpectedAt());
        statement.bindLong(4, entity.getActedAt());
        final String _tmp = __converters.fromAdherenceAction(entity.getAction());
        if (_tmp == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp);
        }
        if (entity.getNote() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getNote());
        }
      }
    };
    this.__preparedStmtOfDeleteAllLogs = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM adherence_logs";
        return _query;
      }
    };
  }

  @Override
  public Object insertLog(final AdherenceLogEntity log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAdherenceLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllLogs(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllLogs.acquire();
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
          __preparedStmtOfDeleteAllLogs.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AdherenceLogEntity>> getAllLogs() {
    final String _sql = "SELECT * FROM adherence_logs ORDER BY actedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"adherence_logs"}, new Callable<List<AdherenceLogEntity>>() {
      @Override
      @NonNull
      public List<AdherenceLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCommitmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "commitmentId");
          final int _cursorIndexOfExpectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedAt");
          final int _cursorIndexOfActedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "actedAt");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<AdherenceLogEntity> _result = new ArrayList<AdherenceLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AdherenceLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCommitmentId;
            if (_cursor.isNull(_cursorIndexOfCommitmentId)) {
              _tmpCommitmentId = null;
            } else {
              _tmpCommitmentId = _cursor.getString(_cursorIndexOfCommitmentId);
            }
            final long _tmpExpectedAt;
            _tmpExpectedAt = _cursor.getLong(_cursorIndexOfExpectedAt);
            final long _tmpActedAt;
            _tmpActedAt = _cursor.getLong(_cursorIndexOfActedAt);
            final AdherenceAction _tmpAction;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfAction)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfAction);
            }
            _tmpAction = __converters.toAdherenceAction(_tmp);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item = new AdherenceLogEntity(_tmpId,_tmpCommitmentId,_tmpExpectedAt,_tmpActedAt,_tmpAction,_tmpNote);
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
  public Flow<List<AdherenceLogEntity>> getLogsForCommitment(final String commitmentId) {
    final String _sql = "SELECT * FROM adherence_logs WHERE commitmentId = ? ORDER BY actedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (commitmentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, commitmentId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"adherence_logs"}, new Callable<List<AdherenceLogEntity>>() {
      @Override
      @NonNull
      public List<AdherenceLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCommitmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "commitmentId");
          final int _cursorIndexOfExpectedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "expectedAt");
          final int _cursorIndexOfActedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "actedAt");
          final int _cursorIndexOfAction = CursorUtil.getColumnIndexOrThrow(_cursor, "action");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final List<AdherenceLogEntity> _result = new ArrayList<AdherenceLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AdherenceLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCommitmentId;
            if (_cursor.isNull(_cursorIndexOfCommitmentId)) {
              _tmpCommitmentId = null;
            } else {
              _tmpCommitmentId = _cursor.getString(_cursorIndexOfCommitmentId);
            }
            final long _tmpExpectedAt;
            _tmpExpectedAt = _cursor.getLong(_cursorIndexOfExpectedAt);
            final long _tmpActedAt;
            _tmpActedAt = _cursor.getLong(_cursorIndexOfActedAt);
            final AdherenceAction _tmpAction;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfAction)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfAction);
            }
            _tmpAction = __converters.toAdherenceAction(_tmp);
            final String _tmpNote;
            if (_cursor.isNull(_cursorIndexOfNote)) {
              _tmpNote = null;
            } else {
              _tmpNote = _cursor.getString(_cursorIndexOfNote);
            }
            _item = new AdherenceLogEntity(_tmpId,_tmpCommitmentId,_tmpExpectedAt,_tmpActedAt,_tmpAction,_tmpNote);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
