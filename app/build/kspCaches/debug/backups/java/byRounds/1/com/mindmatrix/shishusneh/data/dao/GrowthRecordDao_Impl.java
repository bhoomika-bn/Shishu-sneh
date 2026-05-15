package com.mindmatrix.shishusneh.data.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.mindmatrix.shishusneh.data.entity.GrowthRecord;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Float;
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
public final class GrowthRecordDao_Impl implements GrowthRecordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GrowthRecord> __insertionAdapterOfGrowthRecord;

  public GrowthRecordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGrowthRecord = new EntityInsertionAdapter<GrowthRecord>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `growth_records` (`id`,`babyId`,`recordDate`,`weightKg`,`heightCm`,`ageInWeeks`,`weightPercentile`,`heightPercentile`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GrowthRecord entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBabyId());
        statement.bindLong(3, entity.getRecordDate());
        statement.bindDouble(4, entity.getWeightKg());
        statement.bindDouble(5, entity.getHeightCm());
        statement.bindLong(6, entity.getAgeInWeeks());
        if (entity.getWeightPercentile() == null) {
          statement.bindNull(7);
        } else {
          statement.bindDouble(7, entity.getWeightPercentile());
        }
        if (entity.getHeightPercentile() == null) {
          statement.bindNull(8);
        } else {
          statement.bindDouble(8, entity.getHeightPercentile());
        }
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
      }
    };
  }

  @Override
  public Object insert(final GrowthRecord record, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfGrowthRecord.insert(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<GrowthRecord>> observeForBaby(final long babyId) {
    final String _sql = "SELECT * FROM growth_records WHERE babyId = ? ORDER BY recordDate";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, babyId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"growth_records"}, new Callable<List<GrowthRecord>>() {
      @Override
      @NonNull
      public List<GrowthRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBabyId = CursorUtil.getColumnIndexOrThrow(_cursor, "babyId");
          final int _cursorIndexOfRecordDate = CursorUtil.getColumnIndexOrThrow(_cursor, "recordDate");
          final int _cursorIndexOfWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "weightKg");
          final int _cursorIndexOfHeightCm = CursorUtil.getColumnIndexOrThrow(_cursor, "heightCm");
          final int _cursorIndexOfAgeInWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "ageInWeeks");
          final int _cursorIndexOfWeightPercentile = CursorUtil.getColumnIndexOrThrow(_cursor, "weightPercentile");
          final int _cursorIndexOfHeightPercentile = CursorUtil.getColumnIndexOrThrow(_cursor, "heightPercentile");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<GrowthRecord> _result = new ArrayList<GrowthRecord>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GrowthRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBabyId;
            _tmpBabyId = _cursor.getLong(_cursorIndexOfBabyId);
            final long _tmpRecordDate;
            _tmpRecordDate = _cursor.getLong(_cursorIndexOfRecordDate);
            final float _tmpWeightKg;
            _tmpWeightKg = _cursor.getFloat(_cursorIndexOfWeightKg);
            final float _tmpHeightCm;
            _tmpHeightCm = _cursor.getFloat(_cursorIndexOfHeightCm);
            final int _tmpAgeInWeeks;
            _tmpAgeInWeeks = _cursor.getInt(_cursorIndexOfAgeInWeeks);
            final Float _tmpWeightPercentile;
            if (_cursor.isNull(_cursorIndexOfWeightPercentile)) {
              _tmpWeightPercentile = null;
            } else {
              _tmpWeightPercentile = _cursor.getFloat(_cursorIndexOfWeightPercentile);
            }
            final Float _tmpHeightPercentile;
            if (_cursor.isNull(_cursorIndexOfHeightPercentile)) {
              _tmpHeightPercentile = null;
            } else {
              _tmpHeightPercentile = _cursor.getFloat(_cursorIndexOfHeightPercentile);
            }
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new GrowthRecord(_tmpId,_tmpBabyId,_tmpRecordDate,_tmpWeightKg,_tmpHeightCm,_tmpAgeInWeeks,_tmpWeightPercentile,_tmpHeightPercentile,_tmpNotes);
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
