package com.ting.a.livehome.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;


////做备份不使用
public class DbHelperbackup extends SQLiteOpenHelper {
    private static final String DBNAME = "myDB.db";
    private static final int VERSION = 1;
    private SQLiteDatabase database;

    public DbHelperbackup(Context context) {
        super(context, DBNAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {////做备份不使用

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        if (oldVersion != newVersion) {
        }
    }

    // 打开数据连接
    public void open() {
        database = this.getWritableDatabase();
    }

    public void openTran() {
        database = this.getWritableDatabase();
        database.beginTransaction();
    }

    // 关闭数据连接
    public void close() {
        database.close();
    }

    public void closeTran() {
        database.setTransactionSuccessful();
        database.endTransaction();
        database.close();
    }

    // 根据SQL查询
    public Cursor execQuery(String sql) throws DbException {
        try {
            return database.rawQuery(sql, null);
        } catch (Throwable e) {
            throw new DbException(e);
        }
    }

    // 获取最后一个自增ID
    @SuppressWarnings("unused")
    private long getLastAutoIncrementId(String tableName) throws DbException {
        long id = -1;
        open();
        Cursor cursor = execQuery("SELECT seq FROM sqlite_sequence WHERE name='"
                + tableName + "'");
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    id = cursor.getLong(0);
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtils.closeQuietly(cursor);
                close();
            }
        }
        return id;
    }

    // 检查表是否存在
    public boolean tableIsExist(String tableName) throws DbException {
        open();
        Cursor cursor = execQuery("SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name='"
                + tableName + "'");
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    int count = cursor.getInt(0);
                    if (count > 0) {
                        return true;
                    }
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtils.closeQuietly(cursor);
                close();
            }
        }
        return false;
    }

    // 删除表
    public void dropTable(String tableName) {
        open();
        database.execSQL("drop table if exists '" + tableName + "'");
        close();
    }

    // 插入数据
    public long Insert(String tableName, ContentValues contentValues) {
        SQLiteStatement sQLiteStatement = database.compileStatement("");
        sQLiteStatement.bindString(1, "");
        long result = sQLiteStatement.executeInsert();
        if (result < 0) {
        }
        return database.insert(tableName, null, contentValues);
    }

    // 更新数据
    public boolean update(String tableName, ContentValues contentValues,
                          String columnWhere, String[] whereValue) {
        return database.update(tableName, contentValues, columnWhere,
                whereValue) > 0;
    }

    // 查找数据
    public Cursor find(String tableName, String[] columns, String columnWhere,
                       String[] whereValue) {
        return database.query(tableName, columns, columnWhere, whereValue,
                null, null, null);
    }

    // 删除数据
    public boolean delete(String tableName, String columnWhere, Integer... ids) {
        if (columnWhere != null) {
            if (ids.length > 0) {
                StringBuilder sb = new StringBuilder();
                String[] strIds = new String[ids.length];
                for (int i = 0; i < strIds.length; i++) {
                    sb.append('?').append(',');
                    strIds[i] = String.valueOf(ids[i]);
                }
                sb.deleteCharAt(sb.length() - 1);
                return database.delete(tableName,
                        (columnWhere + " in(" + sb + ")"), strIds) > 0;
            }
        }
        return database.delete(tableName, null, null) > 0;
    }

    // 获取数据量
    public long getCount(String tableName) throws DbException {
        long l = 0;
        Cursor cursor = database.query(tableName, new String[]{"count(*)"},
                null, null, null, null, null);
        if (cursor != null) {
            try {
                if (cursor.moveToNext()) {
                    l = cursor.getLong(0);
                }
            } catch (Throwable e) {
                throw new DbException(e);
            } finally {
                IOUtils.closeQuietly(cursor);
            }
        }
        return l;
    }

    // 根据自定义SQL查询
    public Cursor select(String strSQL, String[] whereValue) {
        /*
         * select
		 * p.ProductCode,p.ProductName,d.LastNumber,d.LastAmount,d.LastDays from
		 * Product as p left outer join (select b.ProductCode,Sum(Quantity) as
		 * LastNumber, Sum(Quantity * Price) as LastAmount,Sum(OOSDays) as
		 * LastDays from SalesMaster as a inner join SalesDetail as b on
		 * a.MasterID=b.MasterID where a.SalesCode='001' and a.StoreCode='002'
		 * Group By b.ProductCode)d on p.ProductCode=d.ProductCode where
		 * p.IsFreeGift=0 and p.BrandCode='003'
		 */

        return database.rawQuery(strSQL, whereValue);
    }
}
