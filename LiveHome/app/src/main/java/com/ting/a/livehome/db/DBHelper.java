package com.ting.a.livehome.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * SQLite数据库的帮助类 该类属于扩展类,主要数据库创建和版本升级,其他由父类完成
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 用户地址信息
        db.execSQL("CREATE TABLE IF NOT EXISTS [UserAddsInfo] ([ID] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[consignee] varchar(20),[phone] varchar(25),[province] varchar(20),[city] varchar(20),[district] varchar(20),[adds]  varchar(100),[postcode] varchar(20),[isSecect] INTEGER)");
        // 用户信息
        db.execSQL("CREATE TABLE IF NOT EXISTS [UserInfo] ([ID] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[userId] varchar(20),[userName] varchar(20),[userPass] varchar(20),[userType] INTEGER,[userLeve] INTEGER,[phone] varchar(20),[email]  varchar(100),[userHand] text,[userAdds] varchar(200))");
        // 用户订单信息
        db.execSQL("CREATE TABLE IF NOT EXISTS [UserOrderInfo] ([ID] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[orderCode] varchar(20),[orderType] INTEGER,[dealType] INTEGER,[orderAmount] Float,[orderState] INTEGER,[creaDate]  varchar(25),[dealDate] varchar(25),[shipmentsDate] varchar(25),[completionDate] varchar(25),num INTEGER ,backNum INTEGER,format varchar(50),[consignee] varchar(100),[consigneeAddress] varchar(200))");
        // 新闻通知等信息
        db.execSQL("CREATE TABLE IF NOT EXISTS [MessageInfo] ([ID] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[title] text,[context] text,[serImgUrl] text,[creatDate] Float,[type] INTEGER,[isRead]  INTEGER)");
        // 商店信息
        db.execSQL("CREATE TABLE IF NOT EXISTS [StoreInfo] ([ID] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,[storeNamenum] text,[context] text,[serImgUrl] text,[creatDate] Float,[type] INTEGER,[isRead]  INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // 每个次数据库更新.版本加1(important!!!)
        if (oldVersion != newVersion) {
            for (int i = oldVersion + 1; i <= newVersion; i++)
                updateContent(db, i);
        }
    }

    public void updateContent(SQLiteDatabase db, int version) {
        switch (version) {// 正式更新测试的时候
            case 1:// 上线时间：
                break;
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}