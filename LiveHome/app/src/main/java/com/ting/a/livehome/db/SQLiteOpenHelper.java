package com.ting.a.livehome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.File;

//import com.infox.trainexamisa.R;
//import android.os.Environment;

/**
 * 该类主要用于设置,创建,升级数据库以及打开,关闭数据库资源
 */
public abstract class SQLiteOpenHelper {
	private static final String TAG = SQLiteOpenHelper.class
			.getSimpleName();
	private final Context mContext;
	private final String mName;
	private final CursorFactory mFactory;
	private final int mNewVersion;

	private SQLiteDatabase mDatabase = null;
	private boolean mIsInitializing = false;
	
	public SQLiteOpenHelper(Context context, String name,
                            CursorFactory factory, int version) {
		if (version < 1)
			throw new IllegalArgumentException("版本必须 >= 1, 现在的版本为 "
					+ version);

		mContext = context;
		mName = name;
		mFactory = factory;
		mNewVersion = version;
	}
	
	public synchronized SQLiteDatabase getWritableDatabase() {
		if (mDatabase != null && mDatabase.isOpen() && !mDatabase.isReadOnly()) {
			return mDatabase; // 数据库已打开

		}
		if (mIsInitializing) {
			throw new IllegalStateException(
					"getWritableDatabase 递归调用");
		}
		//防止数据库被锁
		boolean success = false;
		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			if (mName == null) {
				db = SQLiteDatabase.create(null);
			} else {
				String path = getDatabasePath(mName).getPath();
				db = SQLiteDatabase.openOrCreateDatabase(path, mFactory);
			}

			int version = db.getVersion();
			if (version != mNewVersion) {
				db.beginTransaction();
				try {
					if (version == 0) {
						onCreate(db);
					} else {
						onUpgrade(db, version, mNewVersion);
					}
					db.setVersion(mNewVersion);
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
			}
			onOpen(db);
			success = true;
			return db;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mIsInitializing = false;
			if (success) {
				if (mDatabase != null) {
					try {
						mDatabase.close();
					} catch (Exception e) {
					}
				}
				mDatabase = db;
			} else {
				if (db != null)
					db.close();
			}
		}
		return db;
	}
	public synchronized SQLiteDatabase getReadableDatabase() {
		if (mDatabase != null && mDatabase.isOpen()) {
			return mDatabase; // 数据库已打开

		}
		if (mIsInitializing) {
			throw new IllegalStateException(
					"getReadableDatabase called recursively");
		}
		try {
			return getWritableDatabase();
		} catch (SQLiteException e) {
			if (mName == null)
				throw e; // 无法打开只读临时数据库

			Log.e(TAG, "无法打开 " + mName
					+ " 进行写入操作 (只读):", e);
		}

		SQLiteDatabase db = null;
		try {
			mIsInitializing = true;
			String path = getDatabasePath(mName).getPath();
			db = SQLiteDatabase.openDatabase(path, mFactory,
					SQLiteDatabase.OPEN_READWRITE);
			if (db.getVersion() != mNewVersion) {
				throw new SQLiteException(
						"Can't upgrade read-only database from version "
								+ db.getVersion() + " to " + mNewVersion + ": "
								+ path);
			}

			onOpen(db);
			Log.w(TAG, "Opened " + mName + " in read-only mode");
			mDatabase = db;
			return mDatabase;
		} finally {
			mIsInitializing = false;
			if (db != null && db != mDatabase)
				db.close();
		}
	}
	/**
	 * 关闭打开的数据库对象
	 */
	public synchronized void close() {
		if (mIsInitializing)
			throw new IllegalStateException("正在关闭数据库");

		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
			mDatabase = null;
		}
	}
	//获取数据库存放路径
	public File getDatabasePath(String name) {
		String EXTERN_PATH = null;
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED) == true) {
//			String dbPath = mContext.getString(R.string.file_dir)
//					+ mContext.getString(R.string.db_file_dir) + "/";
//			EXTERN_PATH = android.os.Environment.getExternalStorageDirectory()
//					.getAbsolutePath() + dbPath;
		EXTERN_PATH=mContext.getDatabasePath("record").getAbsolutePath();
			File f = new File(EXTERN_PATH);
			if (!f.exists()) {
				f.mkdirs();
			}
		//}
		return new File(EXTERN_PATH + name);
	}
	/*
	 * 创建数据库
	 * */
	public abstract void onCreate(SQLiteDatabase db);
	/*
	 * 版本升级
	 * */
	public abstract void onUpgrade(SQLiteDatabase db, int oldVersion,
                                   int newVersion);

	/**
	 * 打开数据库
	 */
	public  void onOpen(SQLiteDatabase db){
		
	}
}
