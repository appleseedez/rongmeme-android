/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm;

import org.dragon.core.utils.image.BizImageUtils;
import org.dragon.core.utils.view.ViewUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

/**
 * 主体入口界面组件，需要判断是否是登陆状态，如果是登陆状态，则直接显示主体界面。如果未登陆则跳转到未登陆橱窗展示界面。
 * 
 * @author dengjie
 * 
 */
public class MainActivity extends Activity {

    /**
     * 生成自己的主界面对象
     */
    public static Activity mainActivity;
    /**
     * 其他地方可以用用主界面组件的这个缩略图后缀生成的工具类
     */
    public static BizImageUtils bizImageThumbnailUtils;
    /**
     * 其他地方可以用用主界面组件的这个缩略图后缀生成的工具类
     */
    public static ViewUtils viewUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置好屏幕分辨率，并且赋值给BizImageThumbnailUtils，让主界面拥有BizImageThumbnailUtils工具类
        bizImageThumbnailUtils = new BizImageUtils(MainActivity.this);
        //
        viewUtils = new ViewUtils(MainActivity.this);

        setContentView(R.layout.activity_main);

        initComponents();
        initLinsteners();

        mainActivity = this;

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 初始化界面组件
     */
    private void initComponents() {

    }

    /**
     * 初始化监听器
     */
    private void initLinsteners() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 捕获键盘上的返回键，避免返回后到注册登陆界面去，用户确定退出就直接退出系统
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听
            isExit.setButton(DialogInterface.BUTTON_POSITIVE, "确定", backKeyDownlistener);
            isExit.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", backKeyDownlistener);
            // 显示对话框
            isExit.show();

        }

        return false;

    }

    /** 监听对话框里面的button点击事件 */
    DialogInterface.OnClickListener backKeyDownlistener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
            case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                finish();
                break;
            case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                break;
            default:
                break;
            }
        }
    };

}
