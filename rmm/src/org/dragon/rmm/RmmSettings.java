package org.dragon.rmm;

/**
 * 蜜儿开发过程中的调试、开发配置
 * 
 * @author dengjie
 * 
 */
public class RmmSettings {
    /**
     * 是否开启调试服务器，这个对以后有用，第一个版本不提供日志搜集工作
     */
    public static final boolean USE_DEBUG_SERVER = false;

    /**
     * 开发过程中想看到调试日志，运营版本默认为false，这个是一个总开关对整个系统有效。 所以在代码中的debug日志应该写成：
     * 
     * public void onLocationChanged(Location location) { if (DEBUG) Log.d(TAG, "onLocationChanged: " + location);
     * updateLocation(location); }
     */
    public static final boolean DEBUG = false;
    /**
     * 地理位置服务的调试开关，这个比较特殊单独做一个开关
     */
    public static final boolean LOCATION_DEBUG = false;

}
