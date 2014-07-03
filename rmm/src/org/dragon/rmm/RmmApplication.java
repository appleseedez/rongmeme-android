/**
 * Copyright 2009 Joe LaPenna
 */

package org.dragon.rmm;

import java.util.Observer;

import org.dragon.core.galhttprequest.GalHttpRequest;
import org.dragon.core.location.BestLocationListener;
import org.dragon.core.location.LocationException;
import org.dragon.rmm.dao.BeanFactory;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

/**
 * 系统的Application，是单一的，可以作为主体程序来使用。使用方法例如： ((MierApplication) getApplication()).requestLocationUpdates(true)
 * 
 * @author dengjie
 * @since 1.0
 */
public class RmmApplication extends Application {
    /**
     * TAG用来描述是哪个类的字符串常量，多用于日志
     */
    private static final String TAG = "RmmApplication";
    /**
     * 蜜儿系统的设置常量类
     */
    private static final boolean DEBUG = RmmSettings.DEBUG;

    /**
     * 包
     */
    public static final String PACKAGE_NAME = "org.dragon.rmm";

    /**
     * 整个系统第3方的HTTP异步请求都是使用的这个，因为需要第一次初始化其内部的Handler
     */
    public static GalHttpRequest request = null;

    private String mVersion = null;
    /**
     * 本地存储共享区，可以把一些公用数据设置到里面去。保存为xml
     */
    private SharedPreferences mPrefs;
    /**
     * 自定义根据算法计算的地理位置监听器,并且还实现了观察者模式，不过一般系统用不着实时监听，所以观察者模式用的少
     */
    private BestLocationListener mBestLocationListener = new BestLocationListener();
    /**
     * 应用是否第一次启动
     */
    private boolean mIsFirstRun;

    @Override
    public void onCreate() {
        Log.i(TAG, "Using Debug Server:\t" + RmmSettings.USE_DEBUG_SERVER);
        Log.i(TAG, "Using Debug Log:\t" + DEBUG);

        // 启动应用程序的时候，为所有DAO设置context，以便sqlite使用
        BeanFactory.setContextForAllDAOs(this);
        mVersion = getVersionString(this);

        // Check if this is a new install by seeing if our preference file
        // exists on disk.
        mIsFirstRun = checkIfIsFirstRun();

    }

    /**
     * 内存过低，可以部署一些内存回收策略
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 应用结束，可在此处保存数据
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 获取应用版本号
     * 
     * @return 应用版本号
     */
    public String getVersion() {

        if (mVersion != null) {
            return mVersion;
        } else {
            return "";
        }
    }

    /**
     * 注册地理位置监听器，开启监听。监听器最好是在onResume方法中注册，在onPause中调用removeLocationUpdates注销。 注册成功后，
     * 就可以使用getLastKnownLocation获取Location地理位置。
     * 
     * @param gps
     *            是否使用gps。true表示使用，false表示不使用
     * @return 地理位置监听器。
     */
    public BestLocationListener requestLocationUpdates(boolean gps) {
        mBestLocationListener.register((LocationManager) getSystemService(Context.LOCATION_SERVICE), gps);
        return mBestLocationListener;
    }

    /**
     * 注册地理位置监听器，开启监听，并且传入观察者，可以让观察者异步操作UI变化，做到实时变化。监听器最好是在onResume方法中注册， 在onPause中调用removeLocationUpdates注销。注册成功后，
     * 就可以使用getLastKnownLocation获取Location地理位置。
     * 
     * @param observer
     *            观察者
     * @return 地理位置监听器。
     */
    public BestLocationListener requestLocationUpdates(Observer observer) {
        mBestLocationListener.addObserver(observer);
        mBestLocationListener.register((LocationManager) getSystemService(Context.LOCATION_SERVICE), true);
        return mBestLocationListener;
    }

    /**
     * 注销地理位置监听器。在onPause中调用。
     */
    public void removeLocationUpdates() {
        mBestLocationListener.unregister((LocationManager) getSystemService(Context.LOCATION_SERVICE));
    }

    /**
     * 注销地理位置监听器。在onPause中调用。
     * 
     * @param observer
     *            观察者
     */
    public void removeLocationUpdates(Observer observer) {
        mBestLocationListener.deleteObserver(observer);
        this.removeLocationUpdates();
    }

    /**
     * 注册requestLocationUpdates成功后，即可获取当前地理位置信息
     * 
     * @return 地理位置信息
     */
    public Location getLastKnownLocation() {
        return mBestLocationListener.getLastKnownLocation();
    }

    /**
     * 注册requestLocationUpdates成功后，即可获取当前地理位置信息
     * 
     * @return 地理位置信息
     */
    public Location getLastKnownLocationOrThrow() throws LocationException {
        Location location = mBestLocationListener.getLastKnownLocation();
        if (location == null) {
            throw new LocationException();
        }
        return location;
    }

    /**
     * 清空地理位置信息
     */
    public void clearLastKnownLocation() {
        mBestLocationListener.clearLastKnownLocation();
    }

    /**
     * Constructs the version string of the application.
     * 
     * @param context
     *            the context to use for getting package info
     * @return the versions string of the application
     */
    private static String getVersionString(Context context) {
        // Get a version string for the app.
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(PACKAGE_NAME, 0);
            return PACKAGE_NAME + ":" + String.valueOf(pi.versionCode);
        } catch (NameNotFoundException e) {
            if (DEBUG) {
                Log.d(TAG, "Could not retrieve package info", e);
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取应用是否是第一次启动
     * 
     * @return 是否第一次启动
     */
    public boolean getIsFirstRun() {
        return mIsFirstRun;
    }

    /**
     * 第一次启动时，因为没SharedPreferences文件，所以为初始化值，比如true要显示，然后在将这个值赋为false，保存后， 下次启动是读取SharedPreferences文件，找到值就为false。
     * 你在后面写判断要不要显示引导界面就好了。
     * 
     * @return 是否是第一次启动应用
     */
    private boolean checkIfIsFirstRun() {
        // Setup prefs，这里主要是保存是否第一次运行的参数，其他参数可以额外保存
        mPrefs = this.getSharedPreferences("use_first", MODE_PRIVATE);
        boolean isFirstRun = mPrefs.getBoolean("isFirstRun", true);
        Editor editor = mPrefs.edit();

        if (isFirstRun) {
            Log.d("debug", "第一次运行");
            editor.putBoolean("isFirstRun", false);
            editor.commit();
        } else {
            Log.d("debug", "不是第一次运行");
        }
        return isFirstRun;
    }

    /**
     * 获取地理位置监听器对象
     * 
     * @return LocationListener
     */
    public LocationListener getLocationListener() {
        return mBestLocationListener;
    }

    // --------GalHttpLoadTextCallBack区-----------//
}
