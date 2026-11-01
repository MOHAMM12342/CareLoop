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
import com.careloop.app.data.local.entity.EscalationAuditLogEntity;
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
public final class EscalationAuditLogDao_Impl implements EscalationAuditLogDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EscalationAuditLogEntity> __insertionAdapterOfEscalationAuditLogEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public EscalationAuditLogDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEscalationAuditLogEntity = new EntityInsertionAdapter<EscalationAuditLogEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `escalation_audit_log` (`id`,`commitmentId`,`whoNotified`,`notifiedAt`,`channel`,`outcome`,`dataMinimisedMessage`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final EscalationAuditLogEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getCommitmentId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getCommitmentId());
        }
        if (entity.getWhoNotified() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getWhoNotified());
        }
        statement.bindLong(4, entity.getNotifiedAt());
        if (entity.getChannel() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getChannel());
        }
        if (entity.getOutcome() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getOutcome());
        }
        if (entity.getDataMinimisedMessage() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getDataMinimisedMessage());
        }
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM escalation_audit_log";
        return _query;
      }
    };
  }

  @Override
  public Object insertAuditLog(final EscalationAuditLogEntity log,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfEscalationAuditLogEntity.insert(log);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
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
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<EscalationAuditLogEntity>> getAllAuditLogs() {
    final String _sql = "SELECT * FROM escalation_audit_log ORDER BY notifiedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"escalation_audit_log"}, new Callable<List<EscalationAuditLogEntity>>() {
      @Override
      @NonNull
      public List<EscalationAuditLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCommitmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "commitmentId");
          final int _cursorIndexOfWhoNotified = CursorUtil.getColumnIndexOrThrow(_cursor, "whoNotified");
          final int _cursorIndexOfNotifiedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "notifiedAt");
          final int _cursorIndexOfChannel = CursorUtil.getColumnIndexOrThrow(_cursor, "channel");
          final int _cursorIndexOfOutcome = CursorUtil.getColumnIndexOrThrow(_cursor, "outcome");
          final int _cursorIndexOfDataMinimisedMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "dataMinimisedMessage");
          final List<EscalationAuditLogEntity> _result = new ArrayList<EscalationAuditLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EscalationAuditLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCommitmentId;
            if (_cursor.isNull(_cursorIndexOfCommitmentId)) {
              _tmpCommitmentId = null;
            } else {
              _tmpCommitmentId = _cursor.getString(_cursorIndexOfCommitmentId);
            }
            final String _tmpWhoNotified;
            if (_cursor.isNull(_cursorIndexOfWhoNotified)) {
              _tmpWhoNotified = null;
            } else {
              _tmpWhoNotified = _cursor.getString(_cursorIndexOfWhoNotified);
            }
            final long _tmpNotifiedAt;
            _tmpNotifiedAt = _cursor.getLong(_cursorIndexOfNotifiedAt);
            final String _tmpChannel;
            if (_cursor.isNull(_cursorIndexOfChannel)) {
              _tmpChannel = null;
            } else {
              _tmpChannel = _cursor.getString(_cursorIndexOfChannel);
            }
            final String _tmpOutcome;
            if (_cursor.isNull(_cursorIndexOfOutcome)) {
              _tmpOutcome = null;
            } else {
              _tmpOutcome = _cursor.getString(_cursorIndexOfOutcome);
            }
            final String _tmpDataMinimisedMessage;
            if (_cursor.isNull(_cursorIndexOfDataMinimisedMessage)) {
              _tmpDataMinimisedMessage = null;
            } else {
              _tmpDataMinimisedMessage = _cursor.getString(_cursorIndexOfDataMinimisedMessage);
            }
            _item = new EscalationAuditLogEntity(_tmpId,_tmpCommitmentId,_tmpWhoNotified,_tmpNotifiedAt,_tmpChannel,_tmpOutcome,_tmpDataMinimisedMessage);
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
  public Flow<List<EscalationAuditLogEntity>> getAuditLogsForCommitment(final String commitmentId) {
    final String _sql = "SELECT * FROM escalation_audit_log WHERE commitmentId = ? ORDER BY notifiedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (commitmentId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, commitmentId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"escalation_audit_log"}, new Callable<List<EscalationAuditLogEntity>>() {
      @Override
      @NonNull
      public List<EscalationAuditLogEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCommitmentId = CursorUtil.getColumnIndexOrThrow(_cursor, "commitmentId");
          final int _cursorIndexOfWhoNotified = CursorUtil.getColumnIndexOrThrow(_cursor, "whoNotified");
          final int _cursorIndexOfNotifiedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "notifiedAt");
          final int _cursorIndexOfChannel = CursorUtil.getColumnIndexOrThrow(_cursor, "channel");
          final int _cursorIndexOfOutcome = CursorUtil.getColumnIndexOrThrow(_cursor, "outcome");
          final int _cursorIndexOfDataMinimisedMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "dataMinimisedMessage");
          final List<EscalationAuditLogEntity> _result = new ArrayList<EscalationAuditLogEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final EscalationAuditLogEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpCommitmentId;
            if (_cursor.isNull(_cursorIndexOfCommitmentId)) {
              _tmpCommitmentId = null;
            } else {
              _tmpCommitmentId = _cursor.getString(_cursorIndexOfCommitmentId);
            }
            final String _tmpWhoNotified;
            if (_cursor.isNull(_cursorIndexOfWhoNotified)) {
              _tmpWhoNotified = null;
            } else {
              _tmpWhoNotified = _cursor.getString(_cursorIndexOfWhoNotified);
            }
            final long _tmpNotifiedAt;
            _tmpNotifiedAt = _cursor.getLong(_cursorIndexOfNotifiedAt);
            final String _tmpChannel;
            if (_cursor.isNull(_cursorIndexOfChannel)) {
              _tmpChannel = null;
            } else {
              _tmpChannel = _cursor.getString(_cursorIndexOfChannel);
            }
            final String _tmpOutcome;
            if (_cursor.isNull(_cursorIndexOfOutcome)) {
              _tmpOutcome = null;
            } else {
              _tmpOutcome = _cursor.getString(_cursorIndexOfOutcome);
            }
            final String _tmpDataMinimisedMessage;
            if (_cursor.isNull(_cursorIndexOfDataMinimisedMessage)) {
              _tmpDataMinimisedMessage = null;
            } else {
              _tmpDataMinimisedMessage = _cursor.getString(_cursorIndexOfDataMinimisedMessage);
            }
            _item = new EscalationAuditLogEntity(_tmpId,_tmpCommitmentId,_tmpWhoNotified,_tmpNotifiedAt,_tmpChannel,_tmpOutcome,_tmpDataMinimisedMessage);
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
