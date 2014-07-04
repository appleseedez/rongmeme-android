package org.dragon.core.galhttprequest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * GalDBHelper
 * 
 * @Title: GalDBHelper.java
 * @author 林秋明
 * @version V1.0
 */
public final class GalDBHelper extends SQLiteOpenHelper {

    /**
     * tag
     */
    public static final String TAG = "GALDBHelper";

    /**
     * 版本号
     */
    public static final int DB_VERSION = 1;
    /**
     * database name
     */
    public static final String DB_NAME = "galhttprequest_database";

    /**
     * 表名
     */
    public static final String TB_NAME = "httprecord_table";

    /**
     * 字段名:http url
     */
    public static final String HTTP_URL = GALURL.URLFiled.URL;
    /**
     * http last modified
     */
    public static final String HTTP_LASTMODIFIED = GALURL.URLFiled.LASTMODIFIED;
    /**
     * field name: HTTP ETAG
     */
    public static final String HTTP_ETAG = GALURL.URLFiled.ETAG;
    /**
     * field name:HTTP LOCLDATA
     */
    public static final String HTTP_LOCALDATA = GALURL.URLFiled.LOCALDATA;
    /**
     * query columns
     */
    public static final String[] QUERY_COLUMNS = {HTTP_URL,HTTP_LASTMODIFIED,HTTP_ETAG,HTTP_LOCALDATA};
    /**
     * create table SQL
     */
    public static final String SQL_CREATETABLE = "create table if not exists " + TB_NAME
            + " (_id integer primary key autoincrement," + HTTP_URL + " text, " + HTTP_LASTMODIFIED + " text, "
            + HTTP_ETAG + " text, " + HTTP_LOCALDATA + " text);";

    private static GalDBHelper instance;
    private Context context;
    private String tbName;

    /**
     * constructor
     * 
     * @param context
     */
    private GalDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.setContext(context);
        this.tbName = TB_NAME;
        this.getWritableDatabase();
    }

    /**
     * factory method for singleton instancing
     * 
     * @param context
     *            Context
     * @return instance of GAlDBhelper
     */
    public static GalDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new GalDBHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATETABLE);
        } catch (Exception e) {
            LogUtil.i(TAG, "打开或创建数据库失败");
        }
    }

    // 本次版本更新时，调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.i(TAG, "数据库更新到 version" + newVersion);
        // TODO 更新时做点什么
    }

    /**
     * find whether the cursor is empty or not
     * 
     * @return boolean
     */
    public synchronized boolean notEmpty() {
        Cursor cursor = query();
        boolean res = cursor.moveToFirst();
        cursor.close();
        return res;
    }

    /**
     * delete url
     * 
     * @param url
     *            url
     * @return deleted record count
     */
    public synchronized int deleteURL(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 执行删除
        int res = db.delete(tbName, HTTP_URL + "=? ", new String[] {url});
        LogUtil.i("affect +" + res + " row data， delete url =" + url + " successfully!");
        return res;
    }

    /**
     * 清空表
     * 
     * @return count of table cleared
     */
    public synchronized int clear() {
        // 获取SQLiteDatabase实例
        SQLiteDatabase db = this.getWritableDatabase();

        // 执行删除
        int res = db.delete(tbName, null, null);
        LogUtil.i("affect +" + res + " row data， delete table =" + tbName + " successfully!");
        return res;
    }

    @Override
    public synchronized void close() {
        try {
            getWritableDatabase().close();
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.toString(), e);
        }
    }

    /**
     * find whether the URL exists
     * 
     * @param url
     *            url
     * @return boolean
     */
    public synchronized boolean existURL(String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = new String[] {HTTP_URL};
        String selection = HTTP_URL + "=?";
        String[] selectionArgs = {url};
        Cursor c = db.query(tbName, columns, selection, selectionArgs, null, null, null);
        boolean res = c.moveToFirst();
        c.close();
        return res;
    }

    /**
     * insert values into database
     * 
     * @param values
     *            ContentValues
     * @return inserted record count
     */
    public long insert(ContentValues values) {
        // 获取SQLiteDatabase实例
        SQLiteDatabase db = this.getWritableDatabase();

        long err = db.insert(tbName, null, values);
        db.close();
        return err;
    }

    /**
     * insert URL to database
     * 
     * @param galurl
     *            GALURL
     * @return boolean true:insert success,else false
     */
    public synchronized boolean insertURL(GALURL galurl) {
        try {
            ContentValues values = new ContentValues();
            values.put(HTTP_URL, galurl.getUrl());
            values.put(HTTP_LASTMODIFIED, galurl.getLastModified());
            values.put(HTTP_ETAG, galurl.getEtag());
            values.put(HTTP_LOCALDATA, galurl.getLocalData());

            long err = insert(values);
            if (err == -1) {
                LogUtil.i("Error from insertURL:" + err);
                return false;
            } else {
                LogUtil.i("insertURL successful! ");
                return true;
            }
        } catch (Exception e) {
            LogUtil.e("Error from insertURL:" + e.toString());
            return false;
        }
    }

    /**
     * update url
     * 
     * @param galurl
     *            GALURL instance
     */
    public void updateURL(GALURL galurl) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        if (!GalStringUtil.isEmpty(galurl.getLastModified())) {
            contentValues.put(HTTP_LASTMODIFIED, galurl.getLastModified());
        }
        if (!GalStringUtil.isEmpty(galurl.getEtag())) {
            contentValues.put(HTTP_ETAG, galurl.getEtag());
        }
        contentValues.put(HTTP_LOCALDATA, galurl.getLocalData());
        try {
            int num = db.update(tbName, contentValues, HTTP_URL + "=?", new String[] {galurl.getUrl()});
            if (num == 0) {
                insert(contentValues);
            }
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.toString(), e);
        }
    }

    /**
     * get url
     * 
     * @param url
     *            url
     * @return GALURL
     */
    public synchronized GALURL getURL(String url) {
        try {
            GALURL galurl = null;
            Cursor cursor = null;
            SQLiteDatabase db = getWritableDatabase();
            String[] columns = {HTTP_URL,HTTP_LASTMODIFIED,HTTP_ETAG,HTTP_LOCALDATA};
            cursor = db.query(tbName, columns, HTTP_URL + "=?", new String[] {url}, null, null, null);
            if (cursor.moveToFirst()) {
                String lastModified = cursor.getString(cursor.getColumnIndex(HTTP_LASTMODIFIED));
                String etag = cursor.getString(cursor.getColumnIndex(HTTP_ETAG));
                String localdata = cursor.getString(cursor.getColumnIndex(HTTP_LOCALDATA));
                galurl = new GALURL(url, lastModified, etag, localdata);
            }
            cursor.close();
            return galurl;

        } catch (Exception e) {
            LogUtil.e(" Exception " + e.toString(), e);
            return null;
        }
    }

    /**
     * query
     * 
     * @return Cursor
     */
    public Cursor query() {
        // 获取SQLiteDatabase实例
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.query(tbName, null, null, null, null, null, null);
        return c;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
