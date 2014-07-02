package org.dragon.rmm.ui;

import java.io.Serializable;
import java.util.List;

import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.R;
import org.dragon.rmm.RmmApplication;
import org.dragon.rmm.dao.ShakeStoreDAO;
import org.dragon.rmm.domain.ShakeStoreItemResult;
import org.dragon.rmm.domain.ShakeStoreItemVO;
import org.dragon.rmm.ui.center.UserCenterPortal;
import org.dragon.rmm.ui.drycleaning.DryCleaningPortal;
import org.dragon.rmm.utils.PreferenceUtils;
import org.dragon.rmm.view.cleaning.CleaningActivity;
import org.dragon.rmm.view.hourlyemployee.HourlyEmployeeActivity;
import org.dragon.rmm.view.shake.ShakeSelectStoreActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

public class ActMain extends Activity implements OnClickListener {
    private static final String TAG = "ActMain";
    public static final String EXTRA_ID = "ID";

    private static final int SENSOR_SHAKE = 10;
    /**
     * 获取application
     */
    public static RmmApplication rmmApplication;
    private SensorManager sensorManager;
    private Vibrator vibrator;

    public static Intent getIntent(Context context, long shopId) {
        Intent intent = new Intent(context, ActMain.class);
        intent.putExtra(ActMain.EXTRA_ID, shopId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View parent = getLayoutInflater().inflate(R.layout.act_main, null);
        setContentView(parent);

        rmmApplication = (RmmApplication) getApplication();
        rmmApplication.requestLocationUpdates(true);

        //
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        int[] ids = {R.id.menu_launcher_1,R.id.menu_launcher_2,R.id.menu_launcher_3,R.id.menu_launcher_4,
                R.id.menu_launcher_5};
        for (int i = 0; i < ids.length; i++) {
            parent.findViewById(ids[i]).setOnClickListener(this);
        }
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

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
        case R.id.menu_launcher_1: // 钟点工
            Intent intentHourlyEmployee = new Intent(ActMain.this, HourlyEmployeeActivity.class);
            startActivity(intentHourlyEmployee);
            break;
        case R.id.menu_launcher_2:// 我的预约
            startActivity(new Intent(this, UserCenterPortal.class));
            break;
        case R.id.menu_launcher_3:// 干洗
            startActivity(new Intent(this, DryCleaningPortal.class));
            break;
        case R.id.menu_launcher_4:// 保洁
            Intent intentCleaning = new Intent(ActMain.this, CleaningActivity.class);
            startActivity(intentCleaning);
            break;
        case R.id.menu_launcher_5:// 商铺
            long id = getIntent().getLongExtra(ActMain.EXTRA_ID, -1);
            if (-1 != id) {
                startActivity(ActShop.getIntent(this, id));
            }
            break;
        }
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
                if (location == null) {
                    // 摇一摇没有获取到地里位置
                    Toast.makeText(ActMain.this, R.string.no_location_error, Toast.LENGTH_SHORT).show();
                } else {
                    // 摇一摇获取
                    SharedPreferences curSp = getSharedPreferences(PreferenceUtils.PREFERENCE, 0);
                    String curSessionToken = curSp.getString("curSessionToken", "");
                    ShakeStoreDAO.loadRecentStores(curSessionToken, location.getLatitude(), location.getLongitude(),
                            loadRecentStoresCallBack);

                }
                Log.i(TAG, "检测到摇晃，执行操作！");
                break;
            }
        }

    };
    // --------GalHttpLoadTextCallBack区-----------//
    /**
     * 第一次进入界面加载数据的回调函数
     * 
     */
    final GalHttpLoadTextCallBack loadRecentStoresCallBack = new GalHttpLoadTextCallBack() {
        @Override
        public void textLoaded(String text) {
            // 解析返回的JSON字符串
            ShakeStoreItemResult msgList = MierJsonUtils.readValue(text, new TypeToken<ShakeStoreItemResult>() {
            }.getType());
            // 成功
            List<ShakeStoreItemVO> list = msgList.getBody();
            if (msgList != null && list.size() != 0) {
                Intent intent = new Intent(ActMain.this, ShakeSelectStoreActivity.class);
                intent.putExtra("nearbyStores", (Serializable) list);
                startActivity(intent);
            }

        }
    };

}
