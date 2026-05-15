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
import com.mindmatrix.shishusneh.data.entity.Vaccination;
import java.lang.Class;
import java.lang.Exception;
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
public final class VaccinationDao_Impl implements VaccinationDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Vaccination> __insertionAdapterOfVaccination;

  private final EntityDeletionOrUpdateAdapter<Vaccination> __updateAdapterOfVaccination;

  public VaccinationDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfVaccination = new EntityInsertionAdapter<Vaccination>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `vaccinations` (`id`,`babyId`,`vaccineName`,`diseaseInfo`,`scheduledDate`,`isCompleted`,`completedDate`,`reminderEnabled`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Vaccination entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBabyId());
        statement.bindString(3, entity.getVaccineName());
        statement.bindString(4, entity.getDiseaseInfo());
        statement.bindLong(5, entity.getScheduledDate());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getCompletedDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getCompletedDate());
        }
        final int _tmp_1 = entity.getReminderEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
      }
    };
    this.__updateAdapterOfVaccination = new EntityDeletionOrUpdateAdapter<Vaccination>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `vaccinations` SET `id` = ?,`babyId` = ?,`vaccineName` = ?,`diseaseInfo` = ?,`scheduledDate` = ?,`isCompleted` = ?,`completedDate` = ?,`reminderEnabled` = ?,`notes` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Vaccination entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getBabyId());
        statement.bindString(3, entity.getVaccineName());
        statement.bindString(4, entity.getDiseaseInfo());
        statement.bindLong(5, entity.getScheduledDate());
        final int _tmp = entity.isCompleted() ? 1 : 0;
        statement.bindLong(6, _tmp);
        if (entity.getCompletedDate() == null) {
          statement.bindNull(7);
        } else {
          statement.bindLong(7, entity.getCompletedDate());
        }
        final int _tmp_1 = entity.getReminderEnabled() ? 1 : 0;
        statement.bindLong(8, _tmp_1);
        if (entity.getNotes() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getNotes());
        }
        statement.bindLong(10, entity.getId());
      }
    };
  }

  @Override
  public Object insertAll(final List<Vaccination> vaccinations,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfVaccination.insert(vaccinations);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Vaccination vaccination,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfVaccination.handle(vaccination);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Vaccination>> observeForBaby(final long babyId) {
    final String _sql = "SELECT * FROM vaccinations WHERE babyId = ? ORDER BY scheduledDate";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, babyId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"vaccinations"}, new Callable<List<Vaccination>>() {
      @Override
      @NonNull
      public List<Vaccination> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBabyId = CursorUtil.getColumnIndexOrThrow(_cursor, "babyId");
          final int _cursorIndexOfVaccineName = CursorUtil.getColumnIndexOrThrow(_cursor, "vaccineName");
          final int _cursorIndexOfDiseaseInfo = CursorUtil.getColumnIndexOrThrow(_cursor, "diseaseInfo");
          final int _cursorIndexOfScheduledDate = CursorUtil.getColumnIndexOrThrow(_cursor, "scheduledDate");
          final int _cursorIndexOfIsCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "isCompleted");
          final int _cursorIndexOfCompletedDate = CursorUtil.getColumnIndexOrThrow(_cursor, "completedDate");
          final int _cursorIndexOfReminderEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderEnabled");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final List<Vaccination> _result = new ArrayList<Vaccination>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Vaccination _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpBabyId;
            _tmpBabyId = _cursor.getLong(_cursorIndexOfBabyId);
            final String _tmpVaccineName;
            _tmpVaccineName = _cursor.getString(_cursorIndexOfVaccineName);
            final String _tmpDiseaseInfo;
            _tmpDiseaseInfo = _cursor.getString(_cursorIndexOfDiseaseInfo);
            final long _tmpScheduledDate;
            _tmpScheduledDate = _cursor.getLong(_cursorIndexOfScheduledDate);
            final boolean _tmpIsCompleted;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCompleted);
            _tmpIsCompleted = _tmp != 0;
            final Long _tmpCompletedDate;
            if (_cursor.isNull(_cursorIndexOfCompletedDate)) {
              _tmpCompletedDate = null;
            } else {
              _tmpCompletedDate = _cursor.getLong(_cursorIndexOfCompletedDate);
            }
            final boolean _tmpReminderEnabled;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfReminderEnabled);
            _tmpReminderEnabled = _tmp_1 != 0;
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            _item = new Vaccination(_tmpId,_tmpBabyId,_tmpVaccineName,_tmpDiseaseInfo,_tmpScheduledDate,_tmpIsCompleted,_tmpCompletedDate,_tmpReminderEnabled,_tmpNotes);
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
    final String _sql = "SELECT COUNT(*) FROM vaccinations WHERE babyId = ?";
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
}
