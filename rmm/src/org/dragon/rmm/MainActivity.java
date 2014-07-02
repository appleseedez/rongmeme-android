/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm;

import org.dragon.core.utils.image.BizImageUtils;
import org.dragon.core.utils.view.ViewUtils;
import org.dragon.rmm.view.cleaning.CleaningActivity;
import org.dragon.rmm.view.hourlyemployee.HourlyEmployeeActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

/**
 * 主体入口界面组件，需要判断是否是登陆状态，如果是登陆状态，则直接显示主体界面。如果未登陆则跳转到未登陆橱窗展示界面。
 * 
 * @author dengjie
 * 
 */
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final int SENSOR_SHAKE = 10;
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

    private Button goView;

    private Button gohourly;
    /**
     * 获取application
     */
    public static RmmApplication rmmApplication;
    private SensorManager sensorManager;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置好屏幕分辨率，并且赋值给BizImageThumbnailUtils，让主界面拥有BizImageThumbnailUtils工具类
        bizImageThumbnailUtils = new BizImageUtils(MainActivity.this);
        //
        viewUtils = new ViewUtils(MainActivity.this);

        setContentView(R.layout.activity_main);
        rmmApplication = (RmmApplication) getApplication();
        rmmApplication.requestLocationUpdates(true);

        //
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

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
        goView = (Button) findViewById(R.id.goview);
        gohourly = (Button) findViewById(R.id.gohourly);
    }

    /**
     * 初始化监听器
     */
    private void initLinsteners() {
        goView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CleaningActivity.class);
                startActivity(intent);
            }
        });
        gohourly.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HourlyEmployeeActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
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

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正
            Log.i(TAG, "x轴方向的重力加速度" + x + "；y轴方向的重力加速度" + y + "；z轴方向的重力加速度" + z);
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    /**
     * 动作执行
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case SENSOR_SHAKE:
                Location location = rmmApplication.getLastKnownLocation();
                Toast.makeText(MainActivity.this,
                        "检测到摇晃，执行操作！纬度为：" + location.getLatitude() + "经度：" + location.getLongitude(),
                        Toast.LENGTH_SHORT).show();
                Log.i(TAG, "检测到摇晃，执行操作！");
                break;
            }
        }

    };
}
