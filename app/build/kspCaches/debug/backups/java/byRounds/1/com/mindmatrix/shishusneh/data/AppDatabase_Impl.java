package com.mindmatrix.shishusneh.data;

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
import com.mindmatrix.shishusneh.data.dao.BabyProfileDao;
import com.mindmatrix.shishusneh.data.dao.BabyProfileDao_Impl;
import com.mindmatrix.shishusneh.data.dao.FeedingTipDao;
import com.mindmatrix.shishusneh.data.dao.FeedingTipDao_Impl;
import com.mindmatrix.shishusneh.data.dao.GrowthRecordDao;
import com.mindmatrix.shishusneh.data.dao.GrowthRecordDao_Impl;
import com.mindmatrix.shishusneh.data.dao.MilestoneDao;
import com.mindmatrix.shishusneh.data.dao.MilestoneDao_Impl;
import com.mindmatrix.shishusneh.data.dao.VaccinationDao;
import com.mindmatrix.shishusneh.data.dao.VaccinationDao_Impl;
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
public final class AppDatabase_Impl extends AppDatabase {
  private volatile BabyProfileDao _babyProfileDao;

  private volatile GrowthRecordDao _growthRecordDao;

  private volatile VaccinationDao _vaccinationDao;

  private volatile MilestoneDao _milestoneDao;

  private volatile FeedingTipDao _feedingTipDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `baby_profile` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `dateOfBirth` INTEGER NOT NULL, `gender` TEXT NOT NULL, `birthWeightKg` REAL NOT NULL, `birthHeightCm` REAL NOT NULL, `hospitalName` TEXT, `createdAt` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `growth_records` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `babyId` INTEGER NOT NULL, `recordDate` INTEGER NOT NULL, `weightKg` REAL NOT NULL, `heightCm` REAL NOT NULL, `ageInWeeks` INTEGER NOT NULL, `weightPercentile` REAL, `heightPercentile` REAL, `notes` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `vaccinations` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `babyId` INTEGER NOT NULL, `vaccineName` TEXT NOT NULL, `diseaseInfo` TEXT NOT NULL, `scheduledDate` INTEGER NOT NULL, `isCompleted` INTEGER NOT NULL, `completedDate` INTEGER, `reminderEnabled` INTEGER NOT NULL, `notes` TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `milestones` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `babyId` INTEGER NOT NULL, `weekNumber` INTEGER NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `status` TEXT NOT NULL, `checkedDate` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `feeding_tips` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `babyId` INTEGER NOT NULL, `dateEpochDay` INTEGER NOT NULL, `content` TEXT NOT NULL, `babyAgeWeeks` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '49578fcaf4a34d421a73e9686bf61563')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `baby_profile`");
        db.execSQL("DROP TABLE IF EXISTS `growth_records`");
        db.execSQL("DROP TABLE IF EXISTS `vaccinations`");
        db.execSQL("DROP TABLE IF EXISTS `milestones`");
        db.execSQL("DROP TABLE IF EXISTS `feeding_tips`");
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
        final HashMap<String, TableInfo.Column> _columnsBabyProfile = new HashMap<String, TableInfo.Column>(8);
        _columnsBabyProfile.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBabyProfile.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBabyProfile.put("dateOfBirth", new TableInfo.Column("dateOfBirth", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBabyProfile.put("gender", new TableInfo.Column("gender", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBabyProfile.put("birthWeightKg", new TableInfo.Column("birthWeightKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBabyProfile.put("birthHeightCm", new TableInfo.Column("birthHeightCm", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBabyProfile.put("hospitalName", new TableInfo.Column("hospitalName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBabyProfile.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBabyProfile = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBabyProfile = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBabyProfile = new TableInfo("baby_profile", _columnsBabyProfile, _foreignKeysBabyProfile, _indicesBabyProfile);
        final TableInfo _existingBabyProfile = TableInfo.read(db, "baby_profile");
        if (!_infoBabyProfile.equals(_existingBabyProfile)) {
          return new RoomOpenHelper.ValidationResult(false, "baby_profile(com.mindmatrix.shishusneh.data.entity.BabyProfile).\n"
                  + " Expected:\n" + _infoBabyProfile + "\n"
                  + " Found:\n" + _existingBabyProfile);
        }
        final HashMap<String, TableInfo.Column> _columnsGrowthRecords = new HashMap<String, TableInfo.Column>(9);
        _columnsGrowthRecords.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrowthRecords.put("babyId", new TableInfo.Column("babyId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrowthRecords.put("recordDate", new TableInfo.Column("recordDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrowthRecords.put("weightKg", new TableInfo.Column("weightKg", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrowthRecords.put("heightCm", new TableInfo.Column("heightCm", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrowthRecords.put("ageInWeeks", new TableInfo.Column("ageInWeeks", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrowthRecords.put("weightPercentile", new TableInfo.Column("weightPercentile", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrowthRecords.put("heightPercentile", new TableInfo.Column("heightPercentile", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsGrowthRecords.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysGrowthRecords = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesGrowthRecords = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoGrowthRecords = new TableInfo("growth_records", _columnsGrowthRecords, _foreignKeysGrowthRecords, _indicesGrowthRecords);
        final TableInfo _existingGrowthRecords = TableInfo.read(db, "growth_records");
        if (!_infoGrowthRecords.equals(_existingGrowthRecords)) {
          return new RoomOpenHelper.ValidationResult(false, "growth_records(com.mindmatrix.shishusneh.data.entity.GrowthRecord).\n"
                  + " Expected:\n" + _infoGrowthRecords + "\n"
                  + " Found:\n" + _existingGrowthRecords);
        }
        final HashMap<String, TableInfo.Column> _columnsVaccinations = new HashMap<String, TableInfo.Column>(9);
        _columnsVaccinations.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("babyId", new TableInfo.Column("babyId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("vaccineName", new TableInfo.Column("vaccineName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("diseaseInfo", new TableInfo.Column("diseaseInfo", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("scheduledDate", new TableInfo.Column("scheduledDate", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("isCompleted", new TableInfo.Column("isCompleted", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("completedDate", new TableInfo.Column("completedDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("reminderEnabled", new TableInfo.Column("reminderEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsVaccinations.put("notes", new TableInfo.Column("notes", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysVaccinations = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesVaccinations = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoVaccinations = new TableInfo("vaccinations", _columnsVaccinations, _foreignKeysVaccinations, _indicesVaccinations);
        final TableInfo _existingVaccinations = TableInfo.read(db, "vaccinations");
        if (!_infoVaccinations.equals(_existingVaccinations)) {
          return new RoomOpenHelper.ValidationResult(false, "vaccinations(com.mindmatrix.shishusneh.data.entity.Vaccination).\n"
                  + " Expected:\n" + _infoVaccinations + "\n"
                  + " Found:\n" + _existingVaccinations);
        }
        final HashMap<String, TableInfo.Column> _columnsMilestones = new HashMap<String, TableInfo.Column>(7);
        _columnsMilestones.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("babyId", new TableInfo.Column("babyId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("weekNumber", new TableInfo.Column("weekNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMilestones.put("checkedDate", new TableInfo.Column("checkedDate", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMilestones = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMilestones = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMilestones = new TableInfo("milestones", _columnsMilestones, _foreignKeysMilestones, _indicesMilestones);
        final TableInfo _existingMilestones = TableInfo.read(db, "milestones");
        if (!_infoMilestones.equals(_existingMilestones)) {
          return new RoomOpenHelper.ValidationResult(false, "milestones(com.mindmatrix.shishusneh.data.entity.Milestone).\n"
                  + " Expected:\n" + _infoMilestones + "\n"
                  + " Found:\n" + _existingMilestones);
        }
        final HashMap<String, TableInfo.Column> _columnsFeedingTips = new HashMap<String, TableInfo.Column>(5);
        _columnsFeedingTips.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedingTips.put("babyId", new TableInfo.Column("babyId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedingTips.put("dateEpochDay", new TableInfo.Column("dateEpochDay", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedingTips.put("content", new TableInfo.Column("content", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFeedingTips.put("babyAgeWeeks", new TableInfo.Column("babyAgeWeeks", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFeedingTips = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFeedingTips = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFeedingTips = new TableInfo("feeding_tips", _columnsFeedingTips, _foreignKeysFeedingTips, _indicesFeedingTips);
        final TableInfo _existingFeedingTips = TableInfo.read(db, "feeding_tips");
        if (!_infoFeedingTips.equals(_existingFeedingTips)) {
          return new RoomOpenHelper.ValidationResult(false, "feeding_tips(com.mindmatrix.shishusneh.data.entity.FeedingTip).\n"
                  + " Expected:\n" + _infoFeedingTips + "\n"
                  + " Found:\n" + _existingFeedingTips);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "49578fcaf4a34d421a73e9686bf61563", "52423256fcc9c05995820536da4eca39");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "baby_profile","growth_records","vaccinations","milestones","feeding_tips");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `baby_profile`");
      _db.execSQL("DELETE FROM `growth_records`");
      _db.execSQL("DELETE FROM `vaccinations`");
      _db.execSQL("DELETE FROM `milestones`");
      _db.execSQL("DELETE FROM `feeding_tips`");
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
    _typeConvertersMap.put(BabyProfileDao.class, BabyProfileDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(GrowthRecordDao.class, GrowthRecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(VaccinationDao.class, VaccinationDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MilestoneDao.class, MilestoneDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(FeedingTipDao.class, FeedingTipDao_Impl.getRequiredConverters());
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
  public BabyProfileDao babyProfileDao() {
    if (_babyProfileDao != null) {
      return _babyProfileDao;
    } else {
      synchronized(this) {
        if(_babyProfileDao == null) {
          _babyProfileDao = new BabyProfileDao_Impl(this);
        }
        return _babyProfileDao;
      }
    }
  }

  @Override
  public GrowthRecordDao growthRecordDao() {
    if (_growthRecordDao != null) {
      return _growthRecordDao;
    } else {
      synchronized(this) {
        if(_growthRecordDao == null) {
          _growthRecordDao = new GrowthRecordDao_Impl(this);
        }
        return _growthRecordDao;
      }
    }
  }

  @Override
  public VaccinationDao vaccinationDao() {
    if (_vaccinationDao != null) {
      return _vaccinationDao;
    } else {
      synchronized(this) {
        if(_vaccinationDao == null) {
          _vaccinationDao = new VaccinationDao_Impl(this);
        }
        return _vaccinationDao;
      }
    }
  }

  @Override
  public MilestoneDao milestoneDao() {
    if (_milestoneDao != null) {
      return _milestoneDao;
    } else {
      synchronized(this) {
        if(_milestoneDao == null) {
          _milestoneDao = new MilestoneDao_Impl(this);
        }
        return _milestoneDao;
      }
    }
  }

  @Override
  public FeedingTipDao feedingTipDao() {
    if (_feedingTipDao != null) {
      return _feedingTipDao;
    } else {
      synchronized(this) {
        if(_feedingTipDao == null) {
          _feedingTipDao = new FeedingTipDao_Impl(this);
        }
        return _feedingTipDao;
      }
    }
  }
}
