package org.dragon.core.galhttprequest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.util.Log;

/**
 * log util
 * 
 */
public abstract class LogUtil {

    private static final int LOG_LEVEL_ERROR = 4;
    private static final int LOG_LEVEL_WARN = 3;
    private static final int LOG_LEVEL_INFO = 2;
    private static final int LOG_LEVEL_DEBUG = 1;
    private static final int LOG_LEVEL_VERBOSE = 0;

    /** Log信息级别>=logLevel的日志信息打印出来 */
    private static int logLevel = 0;

    // 是否将日志写到文件中
    private static final boolean LOG_IN_FILE = false;
    private static final boolean LOG_WITH_POSTION = false;
    private static final String LOG_TAG = "Carddaren";
    private static final String LOG_FILE = LOG_TAG + ".log";

    private static long curTime;
    private static Context mContext = null;

    /**
     * open log
     * 
     * @param context
     *            context
     */
    public static void openLog(Context context) {
        mContext = context;
    }

    /**
     * start calculation
     * 
     * @param title
     *            title
     */
    public static void startCal(String title) {
        curTime = System.currentTimeMillis();
        // i(title + new Time(curTime).toLocaleString());
    }

    /**
     * start calculation
     */
    public static void startCal() {
        curTime = System.currentTimeMillis();
    }

    /**
     * 计算当前时间和上一次调用Cal()或beginCal()的时间间隔
     * 
     * @param log
     *            log
     */
    public static void cal(String log) {
        long time = System.currentTimeMillis();
        w(log + " cost " + (time - curTime));
        curTime = time;
    }

    /**
     * 详细信息
     * 
     * @param msg
     *            message
     */
    public static void v(String msg) {
        v(LOG_TAG, msg);
    }

    /**
     * 详细信息
     * 
     * @param msg
     *            message
     * @param tag
     *            tag
     */
    public static void v(String tag, String msg) {
        if (LOG_LEVEL_VERBOSE >= logLevel) {

            if (LOG_WITH_POSTION) {
                msg = msg + " on " + new Throwable().getStackTrace()[1].toString();
            }

            Log.v(tag, msg);
            if (LOG_IN_FILE) {
                writeIntoFile(LOG_TAG + " v: " + msg);
            }
        }
    }

    /**
     * 调试日志
     * 
     * @param msg
     *            message
     */
    public static void d(String msg) {
        d(LOG_TAG, msg);
    }

    /**
     * debug
     * 
     * @param tag
     *            tag
     * @param info
     *            info
     */
    public static void d(String tag, String info) {
        if (LOG_LEVEL_DEBUG >= logLevel) {

            if (LOG_WITH_POSTION) {
                info = info + " on " + new Throwable().getStackTrace()[1].toString();
            }

            Log.d(tag, info);
            if (LOG_IN_FILE) {
                writeIntoFile(LOG_TAG + " d: " + info);
            }
        }
    }

    /**
     * 信息日志
     * 
     * @param info
     *            info
     */
    public static void i(String info) {
        i(LOG_TAG, info);
    }

    /**
     * info log
     * @param tag tag
     * @param info info
     */
    public static void i(String tag, String info) {
        if (LOG_LEVEL_INFO >= logLevel) {

            if (LOG_WITH_POSTION) {
                info = info + " on " + new Throwable().getStackTrace()[1].toString();
            }

            Log.i(tag, info);
            if (LOG_IN_FILE) {
                writeIntoFile(LOG_TAG + " i: " + info);

            }
        }
    }

    /**
     * 警告日志
     * 
     * @param msg
     *            message
     */
    public static void w(String msg) {
        w(LOG_TAG, msg);
    }

    /**
     * warning log
     * 
     * @param tag
     *            tag
     * @param info
     *            info
     */
    public static void w(String tag, String info) {
        if (LOG_LEVEL_WARN >= logLevel) {

            if (LOG_WITH_POSTION) {
                info = info + " on " + new Throwable().getStackTrace()[1].toString();
            }

            Log.w(tag, info);
            if (LOG_IN_FILE) {
                writeIntoFile(LOG_TAG + " w: " + info);

            }
        }
    }

    /**
     * 错误日志
     * 
     * @param msg
     *            message
     */
    public static void e(String msg) {
        e(LOG_TAG, msg);
    }

    /**
     * error log
     * 
     * @param tag
     *            tag
     * @param info
     *            info
     */
    public static void e(String tag, String info) {
        if (LOG_LEVEL_ERROR >= logLevel) {

            if (LOG_WITH_POSTION) {
                info = info + " on " + new Throwable().getStackTrace()[1].toString();
            }

            if (info != null) {
                Log.e(tag, info);
            } else {
                Log.e(LOG_TAG, "info null");
            }
            if (LOG_IN_FILE) {
                writeIntoFile(LOG_TAG + " e: " + info);

            }
        }
    }

    /**
     * error log
     * 
     * @param title
     *            title
     * @param e
     *            exception
     */
    public static void e(String title, Exception e) {

        String msg = null;
        if (e == null) {
            msg = title + ": " + "null";
        } else {
            msg = title + ": " + e.toString();
        }
        e(msg);
    }

    /**
     * write into file
     * 
     * @param log
     *            log
     * @return boolean
     */
    public static boolean writeIntoFile(String log) {

        log = log + "\n";

        boolean res = false;
        try {
            // Properties properties = new Properties();
            FileOutputStream fOut = mContext.openFileOutput(LOG_FILE, Context.MODE_APPEND);
            try {
                fOut.write(log.getBytes());
                res = true;
            } catch (IOException e) {
                LogUtil.e(LOG_TAG, e.toString());
            }

        } catch (FileNotFoundException e) {
            LogUtil.e(LOG_TAG, e.toString());
        }
        return res;
    }
}
