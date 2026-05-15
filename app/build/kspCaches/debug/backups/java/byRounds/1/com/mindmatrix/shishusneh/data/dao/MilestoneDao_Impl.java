package com.mindmatrix.shishusneh.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mindmatrix.shishusneh.data.entity.Milestone;
import com.mindmatrix.shishusneh.data.entity.MilestoneStatus;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
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
public final class MilestoneDao_Impl implements MilestoneDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Milestone> __insertionAdapterOfMilestone;

  private final EntityDeletionOrUpdateAdapter<Milestone> __updateAdapterOfMilestone;

  public MilestoneDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMilestone = new EntityInsertionAdapter<Milestone>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `milestones` (`id`,`babyId`,`weekNumber`,`title`,`description`,`status`,`checkedDate`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Milestone entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBabyId());
        statement.bindLong(3, entity.getWeekNumber());
        statement.bindString(4, entity.getTitle());
        statement.bindString(5, entity.getDescription());
        statement.bindString(6, __MilestoneStatus_enumToString(entity.getStatus()));
        if (entity.getCheckedDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getCheckedDate());
        }
      }
    };
    this.__updateAdapterOfMilestone = new EntityDeletionOrUpdateAdapter<Milestone>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `milestones` SET `id` = ?,`babyId` = ?,`weekNumber` = ?,`title` = ?,`description` = ?,`status` = ?,`checkedDate` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Milestone entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBabyId());
        statement.bindLong(3, entity.getWeekNumber());
        statement.bindString(4, entity.getTitle());
        statement.bindString(5, entity.getDescription());
        statement.bindString(6, __MilestoneStatus_enumToString(entity.getStatus()));
        if (entity.getCheckedDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getCheckedDate());
        }
        statement.bindLong(8, entity.getId());
      }
    };
  }

  @Override
  public Object insertAll(final List<Milestone> milestones,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfMilestone.insert(milestones);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Milestone milestone, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMilestone.handle(milestone);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Milestone>> observeForBaby(final long babyId) {
    final String _sql = "SELECT * FROM milestones WHERE babyId = ? ORDER BY weekNumber";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, babyId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"milestones"}, new Callable<List<Milestone>>() {
      @Override
      @NonNull
      public List<Milestone> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBabyId = CursorUtil.getColumnIndexOrThrow(_cursor, "babyId");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCheckedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "checkedDate");
          final List<Milestone> _result = new ArrayList<Milestone>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Milestone _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBabyId;
            _tmpBabyId = _cursor.getLong(_cursorIndexOfBabyId);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final MilestoneStatus _tmpStatus;
            _tmpStatus = __MilestoneStatus_stringToEnum(_cursor.getString(_cursorIndexOfStatus));
            final Long _tmpCheckedDate;
            if (_cursor.isNull(_cursorIndexOfCheckedDate)) {
              _tmpCheckedDate = null;
            } else {
              _tmpCheckedDate = _cursor.getLong(_cursorIndexOfCheckedDate);
            }
            _item = new Milestone(_tmpId,_tmpBabyId,_tmpWeekNumber,_tmpTitle,_tmpDescription,_tmpStatus,_tmpCheckedDate);
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
  public Object countForBaby(final long babyId, final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM milestones WHERE babyId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, babyId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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

  private String __MilestoneStatus_enumToString(@NonNull final MilestoneStatus _value) {
    switch (_value) {
      case NotChecked: return "NotChecked";
      case Achieved: return "Achieved";
      case NotYet: return "NotYet";
      case Skipped: return "Skipped";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private MilestoneStatus __MilestoneStatus_stringToEnum(@NonNull final String _value) {
    switch (_value) {
      case "NotChecked": return MilestoneStatus.NotChecked;
      case "Achieved": return MilestoneStatus.Achieved;
      case "NotYet": return MilestoneStatus.NotYet;
      case "Skipped": return MilestoneStatus.Skipped;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
