package com.ting.a.livehome.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.ting.a.livehome.bean.UserInfo;
import com.ting.a.livehome.bean.UserOrderInfo;
import com.ting.a.livehome.db.DBManager;
import com.ting.a.livehome.db.SQLiteOperate;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据操作类
 */
public class DataDao {

    private DBManager dbManager = null;
    private static DataDao disDao = null;
    //用户表
    private static final String TbleNAME = "UserInfo";
    //查询用户表
    private static final String SQL_SELECT_USERNAME = "select * from UserInfo";
    //根据用户ID查询用户信息
    private static final String SQL_SELECT_ID = "select * from UserInfo where ID=?";
    //删除用户表
    private static final String SQL_DELETE = "delete from UserInfo";
    //删除订单表
    private static final String SQL_DELETE_ORDER = "delete from UserOrderInfo";
    //根据订单编号修改订单状态
    private static final String SQL_UPDATA_WHERE = "update  UserOrderInfo set orderState = ? where orderCode=?";
    //根据用户ID 修改用户电话
    private static final String SQL_UPDATA_USER_WHERE = "update  UserInfo set phone = ? where ID=?";
    //根据用户ID 修改用户的收货地址
    private static final String SQL_UPDATA_USER_ADDS_WHERE = "update  UserInfo set userAdds = ? where ID=?";
    //查询所有订单信息,根据订单状态倒序排序
    private static final String SQL_SELECT_ORDER = "SELECT * from  UserOrderInfo order by orderState desc ";
    //根据订单编号查询订单信息
    private static final String SQL_SELECT_ORDERBYCODE = "SELECT * from  UserOrderInfo WHERE orderCode=? ";


    public DataDao(Context context) {
        dbManager = DBManager.getInstance(context);
    }

    public static DataDao getInstance(Context context) {
        if (disDao == null) {//如果为空将例化DataDao
            disDao = new DataDao(context);
        }
        return disDao;
    }


    /**
     * 保存用户
     *
     * @param info
     * @param isDelete
     * @return
     */
    public boolean saveUser(UserInfo info, boolean isDelete) {
        long effectCounts = 0;
        //初始化数据库
        SQLiteOperate sqliteOperate = SQLiteOperate.getInstance(dbManager, false);
        if (isDelete)
            sqliteOperate.execSQL(SQL_DELETE);
        //判断是否有数据
        if (info != null) {
            //将一个个值提取出来分配到对应的字段上
            ContentValues contentValues = new ContentValues();
            contentValues.put("userName", info.getUserName());
            contentValues.put("phone", info.getPhone());
            contentValues.put("email", info.getUserEmail());
            contentValues.put("userHand", info.getUserHand());
            contentValues.put("userLeve", info.getUserLeve());
            contentValues.put("userPass", info.getPassword());
            contentValues.put("userType", info.getUserType());
            contentValues.put("userId", info.getUserId());
            contentValues.put("userAdds", info.getUserAdds());
            effectCounts = sqliteOperate.insert(TbleNAME, contentValues);
        }
        sqliteOperate.closeDatabase(null);
        return 0 < effectCounts ? true : false;
    }


    /**
     * 删除本地用户
     */
    public void deleteUser() {
        //初始化数据库
        SQLiteOperate sqliteOperate = SQLiteOperate.getInstance(dbManager, false);
        sqliteOperate.execSQL(SQL_DELETE);
        sqliteOperate.closeDatabase(null);
    }

    /**
     * 修改订单状态
     *
     * @param orderCode
     * @param state
     * @return
     */
    public boolean updataOrderState(String state, String orderCode) {
        //初始化数据库
        SQLiteOperate sqliteOperate = SQLiteOperate.getInstance(dbManager, true);
        //执行带条件的修改语句，根据订单编号去修改订单状态
        sqliteOperate.execSQL(SQL_UPDATA_WHERE, new String[]{state, orderCode});
        //关闭数据库
        sqliteOperate.closeDatabase(null);
        return true;
    }

    /**
     * 修改用戶電話
     *
     * @return
     */
    public boolean updataUserPhone(int userId, String phone) {
        //初始化数据库
        SQLiteOperate sqliteOperate = SQLiteOperate.getInstance(dbManager, true);
        //执行带条件的修改语句，根据用户的ID来修改用户的电话
        sqliteOperate.execSQL(SQL_UPDATA_USER_WHERE, new String[]{phone, userId + ""});
        //关闭数据库
        sqliteOperate.closeDatabase(null);
        return true;
    }

    /**
     * 修改用戶收貨地址
     *
     * @return
     */
    public boolean updataUserAdds(int userId, String adds) {
        //初始化数据库
        SQLiteOperate sqliteOperate = SQLiteOperate.getInstance(dbManager, true);
        //执行带条件的修改语句，根据用户的ID来修改用户的收货地址
        sqliteOperate.execSQL(SQL_UPDATA_USER_ADDS_WHERE, new String[]{adds, userId + ""});
        //关闭数据库
        sqliteOperate.closeDatabase(null);
        return true;
    }

    /**
     * 根据用ID查询用户
     */
    public UserInfo findgUserByID(int userId) {
        //初始化数据库
        SQLiteOperate sqliteOperate = SQLiteOperate.getInstance(dbManager,
                false);
        UserInfo info = null;
        //执行查询带条件的查询语句，根据用户ID来查询用户的信息
        info = sqliteOperate.queryForObject(new SQLiteOperate.RowDataMapper<UserInfo>() {
            @Override
            public UserInfo mapRowData(Cursor cursor, int index) {
                //返回查詢結果，根据游标获取对应值
                UserInfo info = new UserInfo();
                info.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                info.setID(cursor.getInt(cursor.getColumnIndex("ID")));
                info.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                info.setUserAdds(cursor.getString(cursor.getColumnIndex("userAdds")));
                return info;
            }

        }, SQL_SELECT_ID, new String[]{userId + ""});
        //将查询出来的数据送出去
        return info;
    }

    /**
     * 查询当前登录人信息
     */
    public UserInfo findgUser() {
        //初始化数据库
        SQLiteOperate sqliteOperate = SQLiteOperate.getInstance(dbManager, false);
        UserInfo info = null;
        //执行查询带条件的查询语句，根据用户名和用户密码来查询用户的信息
        info = sqliteOperate.queryForObject(new SQLiteOperate.RowDataMapper<UserInfo>() {
            @Override
            public UserInfo mapRowData(Cursor cursor, int index) {
                //返回查詢結果，根据游标获取对应值
                UserInfo info = new UserInfo();
                info.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                info.setID(cursor.getInt(cursor.getColumnIndex("ID")));
                info.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                info.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                info.setUserAdds(cursor.getString(cursor.getColumnIndex("userAdds")));
                info.setUserHand(cursor.getString(cursor.getColumnIndex("userHand")));
                info.setUserLeve(cursor.getInt(cursor.getColumnIndex("userLeve")));
                info.setPassword(cursor.getString(cursor.getColumnIndex("userPass")));
                return info;
            }

        }, SQL_SELECT_USERNAME, null);
        return info;
    }

}
