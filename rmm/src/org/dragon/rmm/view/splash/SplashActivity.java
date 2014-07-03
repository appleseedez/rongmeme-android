package org.dragon.rmm.view.splash;

import org.dragon.rmm.R;
import org.dragon.rmm.ui.ActLogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.PushManager;

/**
 * 开机界面组件
 * 
 * @author dengjie
 * 
 */
public class SplashActivity extends Activity {

    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
        new Handler().postDelayed(new Runnable() {
            public void run() {
                goHome();
            }
        }, SPLASH_DELAY_MILLIS);
        // 百度push初始化
        // 以apikey的方式登录
//        PushManager.startWork(this, PushConstants.LOGIN_TYPE_API_KEY,
//                Utils.getMetaValue(SplashActivity.this, "api_key"));
//        createAllCustomPushNotificationStyle();

    }

    @Override
    protected void onStart() {
        super.onStart();
      //  PushManager.activityStarted(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    //    PushManager.activityStoped(this);
    }

    /**
     * 预先设置好所有的百度推送通知的自定义样式，并且设置好编号
     */
    private void createAllCustomPushNotificationStyle() {
        BasicPushNotificationBuilder cBuilder = new BasicPushNotificationBuilder();
        cBuilder.setNotificationVibrate(new long[] {1000,500,500,100,50});
        cBuilder.setStatusbarIcon(R.drawable.simple_notification_icon);
        PushManager.setNotificationBuilder(this, 1, cBuilder);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, ActLogin.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
}
