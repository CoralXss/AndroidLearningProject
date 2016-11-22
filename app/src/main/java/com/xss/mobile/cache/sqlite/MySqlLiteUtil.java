package com.xss.mobile.cache.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Desc：抽离出统一的数据库操作方法
 * Author: xss
 * Time：2016/2/16 10:48
 */
public class MySqlLiteUtil {
    private MySqliteOpenHelper mySqliteOpenHelper;
    private SQLiteDatabase db;

    public MySqlLiteUtil(Context context) {
        mySqliteOpenHelper = new MySqliteOpenHelper(context);
    }

    /**
     *  插入数据到数据库
     */
    public void insert(String table_name, ContentValues values) {
        db = mySqliteOpenHelper.getWritableDatabase();
        db.insert(table_name, null, values);
        db.close();
    }

    /**
     * 删除数据
     * @param table_name
     * @param whereClause
     * @param whereArgs
     */
    public void delete(String table_name, String whereClause, String[] whereArgs) {
        db = mySqliteOpenHelper.getWritableDatabase();
//        db.delete(table_name, "_id=?", new String[]{String.valueOf(_id)});
        db.delete(table_name, whereClause, whereArgs);
    }

    /**
     * 更新数据
     * @param table_name
     * @param values
     * @param whereClause
     * @param whereArgs
     */
    public void update(String table_name, ContentValues values, String whereClause, String[] whereArgs) {
        db = mySqliteOpenHelper.getWritableDatabase();
        db.update(table_name, values, whereClause, whereArgs);
    }

    /**
     * 查询
     * @param sql
     * @param selectionArgs
     * @return
     */
    public Cursor query(String sql, String[] selectionArgs) {
        db = mySqliteOpenHelper.getReadableDatabase();
//        String sql = "select * from " + table_name + "where name = ? and password = ?";
//        Cursor cursor = db.query(table_name, "columns[]", "selection", "String[]selectionArgs", "String groupBy", "having", "orderBy");

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return cursor;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        if (db != null) {
            db.close();
        }
    }
}
