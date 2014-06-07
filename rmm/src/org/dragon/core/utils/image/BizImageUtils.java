/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.core.utils.image;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 头像、作品图片的缩略图名称等生成的工具类
 * 
 * @author dengjie
 */
public class BizImageUtils {

    private Activity activity;

    public BizImageUtils(Activity activity) {
        this.activity = activity;
    }

    /**
     * 获取屏幕宽的分辨率
     * 
     * @return
     */
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    /**
     * 获取屏幕高的分辨率
     * 
     * @return
     */
    public int getScreenHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

}
