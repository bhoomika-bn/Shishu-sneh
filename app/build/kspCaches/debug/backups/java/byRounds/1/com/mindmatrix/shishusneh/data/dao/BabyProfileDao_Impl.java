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
import com.mindmatrix.shishusneh.data.entity.BabyProfile;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BabyProfileDao_Impl implements BabyProfileDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BabyProfile> __insertionAdapterOfBabyProfile;

  public BabyProfileDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBabyProfile = new EntityInsertionAdapter<BabyProfile>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `baby_profile` (`id`,`name`,`dateOfBirth`,`gender`,`birthWeightKg`,`birthHeightCm`,`hospitalName`,`createdAt`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BabyProfile entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getDateOfBirth());
        statement.bindString(4, entity.getGender());
        statement.bindDouble(5, entity.getBirthWeightKg());
        statement.bindDouble(6, entity.getBirthHeightCm());
        if (entity.getHospitalName() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getHospitalName());
        }
        statement.bindLong(8, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insert(final BabyProfile profile, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBabyProfile.insertAndReturnId(profile);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<BabyProfile> observeProfile() {
    final String _sql = "SELECT * FROM baby_profile ORDER BY id LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"baby_profile"}, new Callable<BabyProfile>() {
      @Override
      @Nullable
      public BabyProfile call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDateOfBirth = CursorUtil.getColumnIndexOrThrow(_cursor, "dateOfBirth");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfBirthWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "birthWeightKg");
          final int _cursorIndexOfBirthHeightCm = CursorUtil.getColumnIndexOrThrow(_cursor, "birthHeightCm");
          final int _cursorIndexOfHospitalName = CursorUtil.getColumnIndexOrThrow(_cursor, "hospitalName");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final BabyProfile _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpDateOfBirth;
            _tmpDateOfBirth = _cursor.getLong(_cursorIndexOfDateOfBirth);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final float _tmpBirthWeightKg;
            _tmpBirthWeightKg = _cursor.getFloat(_cursorIndexOfBirthWeightKg);
            final float _tmpBirthHeightCm;
            _tmpBirthHeightCm = _cursor.getFloat(_cursorIndexOfBirthHeightCm);
            final String _tmpHospitalName;
            if (_cursor.isNull(_cursorIndexOfHospitalName)) {
              _tmpHospitalName = null;
            } else {
              _tmpHospitalName = _cursor.getString(_cursorIndexOfHospitalName);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new BabyProfile(_tmpId,_tmpName,_tmpDateOfBirth,_tmpGender,_tmpBirthWeightKg,_tmpBirthHeightCm,_tmpHospitalName,_tmpCreatedAt);
          } else {
            _result = null;
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
  public Object getProfile(final Continuation<? super BabyProfile> $completion) {
    final String _sql = "SELECT * FROM baby_profile ORDER BY id LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<BabyProfile>() {
      @Override
      @Nullable
      public BabyProfile call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfDateOfBirth = CursorUtil.getColumnIndexOrThrow(_cursor, "dateOfBirth");
          final int _cursorIndexOfGender = CursorUtil.getColumnIndexOrThrow(_cursor, "gender");
          final int _cursorIndexOfBirthWeightKg = CursorUtil.getColumnIndexOrThrow(_cursor, "birthWeightKg");
          final int _cursorIndexOfBirthHeightCm = CursorUtil.getColumnIndexOrThrow(_cursor, "birthHeightCm");
          final int _cursorIndexOfHospitalName = CursorUtil.getColumnIndexOrThrow(_cursor, "hospitalName");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final BabyProfile _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpDateOfBirth;
            _tmpDateOfBirth = _cursor.getLong(_cursorIndexOfDateOfBirth);
            final String _tmpGender;
            _tmpGender = _cursor.getString(_cursorIndexOfGender);
            final float _tmpBirthWeightKg;
            _tmpBirthWeightKg = _cursor.getFloat(_cursorIndexOfBirthWeightKg);
            final float _tmpBirthHeightCm;
            _tmpBirthHeightCm = _cursor.getFloat(_cursorIndexOfBirthHeightCm);
            final String _tmpHospitalName;
            if (_cursor.isNull(_cursorIndexOfHospitalName)) {
              _tmpHospitalName = null;
            } else {
              _tmpHospitalName = _cursor.getString(_cursorIndexOfHospitalName);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _result = new BabyProfile(_tmpId,_tmpName,_tmpDateOfBirth,_tmpGender,_tmpBirthWeightKg,_tmpBirthHeightCm,_tmpHospitalName,_tmpCreatedAt);
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
