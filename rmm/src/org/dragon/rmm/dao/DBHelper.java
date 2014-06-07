/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 自定义SQLiteOpenHelper，方便sqlite操作
 * 
 * @author dengjie
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

    /**
     * 数据库名称
     */
    private static final String DATABASE_NAME = "mier.db";

    private static final String CREATE_USER_TABLE_SQL = "CREATE TABLE 'user' ('user_id' INTEGER PRIMARY KEY NOT NULL , 'email' VARCHAR, 'pwd' VARCHAR, 'nickname' VARCHAR, 'mi_num' INTEGER, 'phone' VARCHAR, 'sign' INTEGER, 'age' INTEGER, 'birthday' DATETIME, 'sex' INTEGER, 'mood' VARCHAR, 'head_portrait' VARCHAR, 'login_status' INTEGER, 'is_official' INTEGER, 'create_time' DATETIME, 'update_time' DATETIME);";

    private static final String CREATE_USER_DETAIL_TABLE_SQL = "CREATE TABLE 'user_detail' ('user_detail_id' INTEGER PRIMARY KEY NOT NULL , 'profession' VARCHAR, 'industry' VARCHAR, 'interest' VARCHAR, 'company' INTEGER, 'school' VARCHAR, 'user_id' INTEGER, 'followers_count' INTEGER, 'followings_count' INTEGER, 'like_count' INTEGER, 'share_count' INTEGER, 'pub_count' INTEGER, 'create_time' DATETIME, 'update_time' DATETIME);";

    private static final String CREATE_OAUTH_USER_TABLE_SQL = "CREATE TABLE 'oauth_user'('oauth_id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,'type' INTEGER NOT NULL,'oauth_info' VARCHAR NOT NULL,'oauth_userid' VARCHAR,'user_id' INTEGER NOT NULL);";

    // --------------删除所有db中的表的sql-----------------//
    private static final String DROP_USER_TABLE_SQL = "DROP TABLE 'user';";
    private static final String DROP_USER_DETAIL_TABLE_SQL = "DROP TABLE 'user_detail';";
    private static final String DROP_OAUTH_USER_TABLE_SQL = "DROP TABLE 'oauth_user';";

    /**
     * 数据库版本，以后版本发生变化，则在onUpgrade方法中进行表结构修订
     */
    private static final int DATABASE_VERSION = 7;

    public DBHelper(Context context) {
        // CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE_SQL);
        db.execSQL(CREATE_USER_DETAIL_TABLE_SQL);
        db.execSQL(CREATE_OAUTH_USER_TABLE_SQL);
    }

    // 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL(DROP_OAUTH_USER_TABLE_SQL);
        // db.execSQL(CREATE_OAUTH_USER_TABLE_SQL);
    }
}
