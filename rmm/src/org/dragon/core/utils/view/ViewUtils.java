/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.core.utils.view;

import org.dragon.rmm.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Build;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.Surface;

/**
 * 头像、作品图片的缩略图名称等生成的工具类
 * 
 * @author dengjie
 */
public class ViewUtils {

    private Activity activity;

    public ViewUtils(Activity activity) {
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

    /**
     * 根据当前机器屏幕，将dp转换成px像素
     * 
     * @param mContext
     * @param dps
     * @return
     */
    public float dp2Px(float dps) {
        DisplayMetrics metrics = new DisplayMetrics();
        float density = metrics.density;
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
        float pixels = dps * density;
        return pixels;
    }

    /**
     * 根据当前机器屏幕，将像素px转换成dp
     * 
     * @param mContext
     * @param dps
     * @return
     */
    public float px2Dp(float pixels) {
        DisplayMetrics metrics = new DisplayMetrics();
        float density = metrics.density;
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
        float dps = pixels / density;
        return dps;
    }

    /**
     * 获取屏幕的density的数值
     * 
     * @return
     */
    public float getDensity() {
        DisplayMetrics metrics = new DisplayMetrics();
        float density = metrics.density;
        return density;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void setCameraDisplayOrientation(int cameraId, Camera camera) {

        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
        case Surface.ROTATION_0:
            degrees = 0;
            break;
        case Surface.ROTATION_90:
            degrees = 90;
            break;
        case Surface.ROTATION_180:
            degrees = 180;
            break;
        case Surface.ROTATION_270:
            degrees = 270;
            break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {

            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // do mirror flip
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);

    }

    /**
     * 自动创建桌面图标
     * 
     * @param iconResId
     * @param appnameResId
     */
    public void createShortCut() {
        // com.android.launcher.permission.INSTALL_SHORTCUT
        Intent shortcutintent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(R.string.app_name));
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(activity.getApplicationContext(), R.drawable.icon_logo);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(activity.getApplicationContext(), activity.getClass()));
        // 发送广播
        activity.sendBroadcast(shortcutintent);
    }
}
