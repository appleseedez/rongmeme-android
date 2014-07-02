/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm;

/**
 * <p>
 * 设置里面的常量类。提供各类使用的常量，避免魔鬼数字。
 * </p>
 * 
 * @author dengjie
 * @since 1.0
 */
public class SettingConstants {

    /**
     * push，推送通知的存储在SharedPreferences中的key
     */
    public final static String PUSH_SETTING_STATUS_KEY = "push_setting_status";
    
    /**
     * push，推送通知的开状态。1表示开，0表示关
     */
    public final static int PUSH_SETTING_IS_OPEN_STATUS = 1;
    /**
     * push，推送通知的开状态。1表示开，0表示关
     */
    public final static int PUSH_SETTING_IS_CLOSE_STATUS = 0;

    /**
     * 背景音乐，推送通知的存储在SharedPreferences中的key
     */
    public final static String BG_MUSIC_SETTING_STATUS_KEY = "bg_music_setting_status";
    /**
     * 声音的开状态。1表示开，0表示关
     */
    public final static int BG_MUSIC_SETTING_IS_OPEN_STATUS = 1;
    /**
     * 声音的开状态。1表示开，0表示关
     */
    public final static int BG_MUSIC_SETTING_IS_CLOSE_STATUS = 0;

    /**
     * 私有构造方法，使该类不能被继承
     */
    private SettingConstants() {

    }
}
