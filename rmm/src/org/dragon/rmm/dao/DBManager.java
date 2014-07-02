/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 所有DAO核心的接口，包含了公用核心方法和属性。是单例的容器工场中，跟spring一样在APP启动的时候初始化好，在后续应用执行中则节约了调用实例化的时间。 未将其做成单例，是为了避免sqlite线程死锁的问题。
 * 
 * @author dengjie
 * 
 */
public class DBManager {
    public DBHelper helper;
    public SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public DBHelper getHelper() {
        return helper;
    }

    public void setHelper(DBHelper helper) {
        this.helper = helper;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 关闭了数据库
     */
    public void closeDB() {
        db.close();
    }
}
