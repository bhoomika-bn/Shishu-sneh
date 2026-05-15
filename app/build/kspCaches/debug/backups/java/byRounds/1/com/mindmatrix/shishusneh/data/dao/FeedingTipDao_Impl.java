package com.mindmatrix.shishusneh.data.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mindmatrix.shishusneh.data.entity.FeedingTip;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FeedingTipDao_Impl implements FeedingTipDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FeedingTip> __insertionAdapterOfFeedingTip;

  public FeedingTipDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFeedingTip = new EntityInsertionAdapter<FeedingTip>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `feeding_tips` (`id`,`babyId`,`dateEpochDay`,`content`,`babyAgeWeeks`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FeedingTip entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBabyId());
        statement.bindLong(3, entity.getDateEpochDay());
        statement.bindString(4, entity.getContent());
        statement.bindLong(5, entity.getBabyAgeWeeks());
      }
    };
  }

  @Override
  public Object insert(final FeedingTip tip, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFeedingTip.insert(tip);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getForDate(final long babyId, final long dateEpochDay,
      final Continuation<? super FeedingTip> $completion) {
    final String _sql = "SELECT * FROM feeding_tips WHERE babyId = ? AND dateEpochDay = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, babyId);
    _argIndex = 2;
    _statement.bindLong(_argIndex, dateEpochDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<FeedingTip>() {
      @Override
      @Nullable
      public FeedingTip call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBabyId = CursorUtil.getColumnIndexOrThrow(_cursor, "babyId");
          final int _cursorIndexOfDateEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "dateEpochDay");
          final int _cursorIndexOfContent = CursorUtil.getColumnIndexOrThrow(_cursor, "content");
          final int _cursorIndexOfBabyAgeWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "babyAgeWeeks");
          final FeedingTip _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBabyId;
            _tmpBabyId = _cursor.getLong(_cursorIndexOfBabyId);
            final long _tmpDateEpochDay;
            _tmpDateEpochDay = _cursor.getLong(_cursorIndexOfDateEpochDay);
            final String _tmpContent;
            _tmpContent = _cursor.getString(_cursorIndexOfContent);
            final int _tmpBabyAgeWeeks;
            _tmpBabyAgeWeeks = _cursor.getInt(_cursorIndexOfBabyAgeWeeks);
            _result = new FeedingTip(_tmpId,_tmpBabyId,_tmpDateEpochDay,_tmpContent,_tmpBabyAgeWeeks);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
