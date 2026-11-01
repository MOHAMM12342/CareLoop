package com.careloop.app.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.careloop.app.data.local.Converters;
import com.careloop.app.data.local.entity.CommitmentEntity;
import com.careloop.app.domain.model.CommitmentState;
import com.careloop.app.domain.model.CommitmentType;
import com.careloop.app.domain.model.VerificationMethod;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
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
public final class CommitmentDao_Impl implements CommitmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CommitmentEntity> __insertionAdapterOfCommitmentEntity;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<CommitmentEntity> __updateAdapterOfCommitmentEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateState;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllCommitments;

  public CommitmentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCommitmentEntity = new EntityInsertionAdapter<CommitmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `commitments` (`id`,`patientId`,`type`,`title`,`dueAt`,`state`,`verificationMethod`,`sourceEventId`,`escalationLevel`,`completedAt`,`createdAt`,`notes`,`verifiedOnline`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CommitmentEntity entity) {
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
        final String _tmp = __converters.fromCommitmentType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        if (entity.getTitle() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTitle());
        }
        statement.bindLong(5, entity.getDueAt());
        final String _tmp_1 = __converters.fromCommitmentState(entity.getState());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_1);
        }
        final String _tmp_2 = __converters.fromVerificationMethod(entity.getVerificationMethod());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_2);
        }
        if (entity.getSourceEventId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSourceEventId());
        }
        statement.bindLong(9, entity.getEscalationLevel());
        if (entity.getCompletedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getCompletedAt());
        }
        statement.bindLong(11, entity.getCreatedAt());
        if (entity.getNotes() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getNotes());
        }
        final int _tmp_3 = entity.getVerifiedOnline() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
      }
    };
    this.__updateAdapterOfCommitmentEntity = new EntityDeletionOrUpdateAdapter<CommitmentEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `commitments` SET `id` = ?,`patientId` = ?,`type` = ?,`title` = ?,`dueAt` = ?,`state` = ?,`verificationMethod` = ?,`sourceEventId` = ?,`escalationLevel` = ?,`completedAt` = ?,`createdAt` = ?,`notes` = ?,`verifiedOnline` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CommitmentEntity entity) {
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
        final String _tmp = __converters.fromCommitmentType(entity.getType());
        if (_tmp == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, _tmp);
        }
        if (entity.getTitle() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getTitle());
        }
        statement.bindLong(5, entity.getDueAt());
        final String _tmp_1 = __converters.fromCommitmentState(entity.getState());
        if (_tmp_1 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_1);
        }
        final String _tmp_2 = __converters.fromVerificationMethod(entity.getVerificationMethod());
        if (_tmp_2 == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, _tmp_2);
        }
        if (entity.getSourceEventId() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getSourceEventId());
        }
        statement.bindLong(9, entity.getEscalationLevel());
        if (entity.getCompletedAt() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getCompletedAt());
        }
        statement.bindLong(11, entity.getCreatedAt());
        if (entity.getNotes() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getNotes());
        }
        final int _tmp_3 = entity.getVerifiedOnline() ? 1 : 0;
        statement.bindLong(13, _tmp_3);
        if (entity.getId() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getId());
        }
      }
    };
    this.__preparedStmtOfUpdateState = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE commitments SET state = ?, completedAt = ?, verifiedOnline = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllCommitments = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM commitments";
        return _query;
      }
    };
  }

  @Override
  public Object insertCommitment(final CommitmentEntity commitment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCommitmentEntity.insert(commitment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCommitments(final List<CommitmentEntity> commitments,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCommitmentEntity.insert(commitments);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCommitment(final CommitmentEntity commitment,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfCommitmentEntity.handle(commitment);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateState(final String id, final CommitmentState newState, final Long completedAt,
      final boolean verifiedOnline, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateState.acquire();
        int _argIndex = 1;
        final String _tmp = __converters.fromCommitmentState(newState);
        if (_tmp == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, _tmp);
        }
        _argIndex = 2;
        if (completedAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, completedAt);
        }
        _argIndex = 3;
        final int _tmp_1 = verifiedOnline ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp_1);
        _argIndex = 4;
        if (id == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, id);
        }
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
          __preparedStmtOfUpdateState.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllCommitments(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllCommitments.acquire();
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
          __preparedStmtOfDeleteAllCommitments.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CommitmentEntity>> getAllCommitments() {
    final String _sql = "SELECT * FROM commitments ORDER BY dueAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"commitments"}, new Callable<List<CommitmentEntity>>() {
      @Override
      @NonNull
      public List<CommitmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDueAt = CursorUtil.getColumnIndexOrThrow(_cursor, "dueAt");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVerificationMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "verificationMethod");
          final int _cursorIndexOfSourceEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEventId");
          final int _cursorIndexOfEscalationLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "escalationLevel");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfVerifiedOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "verifiedOnline");
          final List<CommitmentEntity> _result = new ArrayList<CommitmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CommitmentEntity _item;
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
            final CommitmentType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __converters.toCommitmentType(_tmp);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final long _tmpDueAt;
            _tmpDueAt = _cursor.getLong(_cursorIndexOfDueAt);
            final CommitmentState _tmpState;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfState)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfState);
            }
            _tmpState = __converters.toCommitmentState(_tmp_1);
            final VerificationMethod _tmpVerificationMethod;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfVerificationMethod)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfVerificationMethod);
            }
            _tmpVerificationMethod = __converters.toVerificationMethod(_tmp_2);
            final String _tmpSourceEventId;
            if (_cursor.isNull(_cursorIndexOfSourceEventId)) {
              _tmpSourceEventId = null;
            } else {
              _tmpSourceEventId = _cursor.getString(_cursorIndexOfSourceEventId);
            }
            final int _tmpEscalationLevel;
            _tmpEscalationLevel = _cursor.getInt(_cursorIndexOfEscalationLevel);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final boolean _tmpVerifiedOnline;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfVerifiedOnline);
            _tmpVerifiedOnline = _tmp_3 != 0;
            _item = new CommitmentEntity(_tmpId,_tmpPatientId,_tmpType,_tmpTitle,_tmpDueAt,_tmpState,_tmpVerificationMethod,_tmpSourceEventId,_tmpEscalationLevel,_tmpCompletedAt,_tmpCreatedAt,_tmpNotes,_tmpVerifiedOnline);
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
  public Flow<List<CommitmentEntity>> getTodayCommitments(final long startOfDay,
      final long endOfDay) {
    final String _sql = "SELECT * FROM commitments WHERE dueAt BETWEEN ? AND ? ORDER BY dueAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    _argIndex = 2;
    _statement.bindLong(_argIndex, endOfDay);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"commitments"}, new Callable<List<CommitmentEntity>>() {
      @Override
      @NonNull
      public List<CommitmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDueAt = CursorUtil.getColumnIndexOrThrow(_cursor, "dueAt");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVerificationMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "verificationMethod");
          final int _cursorIndexOfSourceEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEventId");
          final int _cursorIndexOfEscalationLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "escalationLevel");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfVerifiedOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "verifiedOnline");
          final List<CommitmentEntity> _result = new ArrayList<CommitmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CommitmentEntity _item;
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
            final CommitmentType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __converters.toCommitmentType(_tmp);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final long _tmpDueAt;
            _tmpDueAt = _cursor.getLong(_cursorIndexOfDueAt);
            final CommitmentState _tmpState;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfState)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfState);
            }
            _tmpState = __converters.toCommitmentState(_tmp_1);
            final VerificationMethod _tmpVerificationMethod;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfVerificationMethod)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfVerificationMethod);
            }
            _tmpVerificationMethod = __converters.toVerificationMethod(_tmp_2);
            final String _tmpSourceEventId;
            if (_cursor.isNull(_cursorIndexOfSourceEventId)) {
              _tmpSourceEventId = null;
            } else {
              _tmpSourceEventId = _cursor.getString(_cursorIndexOfSourceEventId);
            }
            final int _tmpEscalationLevel;
            _tmpEscalationLevel = _cursor.getInt(_cursorIndexOfEscalationLevel);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final boolean _tmpVerifiedOnline;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfVerifiedOnline);
            _tmpVerifiedOnline = _tmp_3 != 0;
            _item = new CommitmentEntity(_tmpId,_tmpPatientId,_tmpType,_tmpTitle,_tmpDueAt,_tmpState,_tmpVerificationMethod,_tmpSourceEventId,_tmpEscalationLevel,_tmpCompletedAt,_tmpCreatedAt,_tmpNotes,_tmpVerifiedOnline);
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
  public Object getCommitmentById(final String id,
      final Continuation<? super CommitmentEntity> $completion) {
    final String _sql = "SELECT * FROM commitments WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CommitmentEntity>() {
      @Override
      @Nullable
      public CommitmentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDueAt = CursorUtil.getColumnIndexOrThrow(_cursor, "dueAt");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVerificationMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "verificationMethod");
          final int _cursorIndexOfSourceEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEventId");
          final int _cursorIndexOfEscalationLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "escalationLevel");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfVerifiedOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "verifiedOnline");
          final CommitmentEntity _result;
          if (_cursor.moveToFirst()) {
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
            final CommitmentType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __converters.toCommitmentType(_tmp);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final long _tmpDueAt;
            _tmpDueAt = _cursor.getLong(_cursorIndexOfDueAt);
            final CommitmentState _tmpState;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfState)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfState);
            }
            _tmpState = __converters.toCommitmentState(_tmp_1);
            final VerificationMethod _tmpVerificationMethod;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfVerificationMethod)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfVerificationMethod);
            }
            _tmpVerificationMethod = __converters.toVerificationMethod(_tmp_2);
            final String _tmpSourceEventId;
            if (_cursor.isNull(_cursorIndexOfSourceEventId)) {
              _tmpSourceEventId = null;
            } else {
              _tmpSourceEventId = _cursor.getString(_cursorIndexOfSourceEventId);
            }
            final int _tmpEscalationLevel;
            _tmpEscalationLevel = _cursor.getInt(_cursorIndexOfEscalationLevel);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final boolean _tmpVerifiedOnline;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfVerifiedOnline);
            _tmpVerifiedOnline = _tmp_3 != 0;
            _result = new CommitmentEntity(_tmpId,_tmpPatientId,_tmpType,_tmpTitle,_tmpDueAt,_tmpState,_tmpVerificationMethod,_tmpSourceEventId,_tmpEscalationLevel,_tmpCompletedAt,_tmpCreatedAt,_tmpNotes,_tmpVerifiedOnline);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCommitmentBySourceId(final String sourceEventId,
      final Continuation<? super CommitmentEntity> $completion) {
    final String _sql = "SELECT * FROM commitments WHERE sourceEventId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (sourceEventId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sourceEventId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CommitmentEntity>() {
      @Override
      @Nullable
      public CommitmentEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDueAt = CursorUtil.getColumnIndexOrThrow(_cursor, "dueAt");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVerificationMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "verificationMethod");
          final int _cursorIndexOfSourceEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEventId");
          final int _cursorIndexOfEscalationLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "escalationLevel");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfVerifiedOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "verifiedOnline");
          final CommitmentEntity _result;
          if (_cursor.moveToFirst()) {
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
            final CommitmentType _tmpType;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __converters.toCommitmentType(_tmp);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final long _tmpDueAt;
            _tmpDueAt = _cursor.getLong(_cursorIndexOfDueAt);
            final CommitmentState _tmpState;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfState)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfState);
            }
            _tmpState = __converters.toCommitmentState(_tmp_1);
            final VerificationMethod _tmpVerificationMethod;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfVerificationMethod)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfVerificationMethod);
            }
            _tmpVerificationMethod = __converters.toVerificationMethod(_tmp_2);
            final String _tmpSourceEventId;
            if (_cursor.isNull(_cursorIndexOfSourceEventId)) {
              _tmpSourceEventId = null;
            } else {
              _tmpSourceEventId = _cursor.getString(_cursorIndexOfSourceEventId);
            }
            final int _tmpEscalationLevel;
            _tmpEscalationLevel = _cursor.getInt(_cursorIndexOfEscalationLevel);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final boolean _tmpVerifiedOnline;
            final int _tmp_3;
            _tmp_3 = _cursor.getInt(_cursorIndexOfVerifiedOnline);
            _tmpVerifiedOnline = _tmp_3 != 0;
            _result = new CommitmentEntity(_tmpId,_tmpPatientId,_tmpType,_tmpTitle,_tmpDueAt,_tmpState,_tmpVerificationMethod,_tmpSourceEventId,_tmpEscalationLevel,_tmpCompletedAt,_tmpCreatedAt,_tmpNotes,_tmpVerifiedOnline);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getCommitmentsInStates(final List<? extends CommitmentState> states,
      final Continuation<? super List<CommitmentEntity>> $completion) {
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT * FROM commitments WHERE state IN (");
    final int _inputSize = states.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (CommitmentState _item : states) {
      final String _tmp = __converters.fromCommitmentState(_item);
      if (_tmp == null) {
        _statement.bindNull(_argIndex);
      } else {
        _statement.bindString(_argIndex, _tmp);
      }
      _argIndex++;
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CommitmentEntity>>() {
      @Override
      @NonNull
      public List<CommitmentEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfPatientId = CursorUtil.getColumnIndexOrThrow(_cursor, "patientId");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDueAt = CursorUtil.getColumnIndexOrThrow(_cursor, "dueAt");
          final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
          final int _cursorIndexOfVerificationMethod = CursorUtil.getColumnIndexOrThrow(_cursor, "verificationMethod");
          final int _cursorIndexOfSourceEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceEventId");
          final int _cursorIndexOfEscalationLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "escalationLevel");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfVerifiedOnline = CursorUtil.getColumnIndexOrThrow(_cursor, "verifiedOnline");
          final List<CommitmentEntity> _result = new ArrayList<CommitmentEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CommitmentEntity _item_1;
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
            final CommitmentType _tmpType;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfType);
            }
            _tmpType = __converters.toCommitmentType(_tmp_1);
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final long _tmpDueAt;
            _tmpDueAt = _cursor.getLong(_cursorIndexOfDueAt);
            final CommitmentState _tmpState;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfState)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfState);
            }
            _tmpState = __converters.toCommitmentState(_tmp_2);
            final VerificationMethod _tmpVerificationMethod;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfVerificationMethod)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfVerificationMethod);
            }
            _tmpVerificationMethod = __converters.toVerificationMethod(_tmp_3);
            final String _tmpSourceEventId;
            if (_cursor.isNull(_cursorIndexOfSourceEventId)) {
              _tmpSourceEventId = null;
            } else {
              _tmpSourceEventId = _cursor.getString(_cursorIndexOfSourceEventId);
            }
            final int _tmpEscalationLevel;
            _tmpEscalationLevel = _cursor.getInt(_cursorIndexOfEscalationLevel);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final boolean _tmpVerifiedOnline;
            final int _tmp_4;
            _tmp_4 = _cursor.getInt(_cursorIndexOfVerifiedOnline);
            _tmpVerifiedOnline = _tmp_4 != 0;
            _item_1 = new CommitmentEntity(_tmpId,_tmpPatientId,_tmpType,_tmpTitle,_tmpDueAt,_tmpState,_tmpVerificationMethod,_tmpSourceEventId,_tmpEscalationLevel,_tmpCompletedAt,_tmpCreatedAt,_tmpNotes,_tmpVerifiedOnline);
            _result.add(_item_1);
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
