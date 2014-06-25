package org.dragon.rmm.view.cleaning;

import java.util.List;

import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.Constants;
import org.dragon.rmm.R;
import org.dragon.rmm.dao.CleaningDAO;
import org.dragon.rmm.domain.CleaningItemBody;
import org.dragon.rmm.domain.CleaningItemResult;
import org.dragon.rmm.domain.CleaningItemVO;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

/**
 * 保洁预约界面组件
 * 
 * @author dengjie
 * 
 */
public class CleaningCustomActivity extends Activity {

    private LinearLayout smallStartItemContent;
    private Button confirmAppointmentBtn;
    private Button contactCustomServiceBtn;
    private ImageButton backNavigationImagebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cleaning_custom);

        initComponents();
        initLinsteners();
        loadCleanServices();
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
        confirmAppointmentBtn = (Button) findViewById(R.id.cc_confirm_appointment_btn);
        contactCustomServiceBtn = (Button) findViewById(R.id.cc_contact_custom_service_btn);
        backNavigationImagebutton = (ImageButton) findViewById(R.id.cc_back_navigation_imagebutton);

        smallStartItemContent = (LinearLayout) findViewById(R.id.cc_small_start_item_content);

    }

    public void loadCleanServices() {
        CleaningDAO.loadCleanServices(loadCleanServicesCallBack);
    }

    private void initStarComponents(List<CleaningItemVO> list) {
        for (CleaningItemVO ct : list) {
            // 遍历生成item的
            String name = ct.getName();
            View checkStartSmallItemView = (View) getLayoutInflater().inflate(R.layout.star_small_item_custom, null);
            TextView smallCategoryNameTitleTextView = (TextView) checkStartSmallItemView
                    .findViewById(R.id.ssic_small_category_name_title);
            smallCategoryNameTitleTextView.setText(name);
            smallStartItemContent.addView(checkStartSmallItemView, 0);
        }
    }

    /**
     * 初始化监听器
     */
    private void initLinsteners() {
        confirmAppointmentBtn.setOnClickListener(confirmAppointmentBtnOnClickListener);
        contactCustomServiceBtn.setOnClickListener(contactCustomServiceBtnOnClickListener);
        backNavigationImagebutton.setOnClickListener(backNavigationButtonOnClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // -------监听器区域----------//
    // -------监听器区域----------//
    /**
     * 导航返回按钮点击事件
     * 
     */
    OnClickListener backNavigationButtonOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };
    /**
     * 确认预约按钮点击事件
     * 
     */
    OnClickListener confirmAppointmentBtnOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };
    /**
     * 联系客服按钮点击事件
     * 
     */
    OnClickListener contactCustomServiceBtnOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            callSalesTelephone();
        }
    };

    /**
     * 调用系统拨打电话功能进行一键咨询的功能
     */
    private void callSalesTelephone() {
        String strMobile = Constants.SALES_TELEPHONE;
        // 此处应该对电话号码进行验证。。
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + strMobile));
        startActivity(intent);
    }

    // --------GalHttpLoadTextCallBack区-----------//
    /**
     * 第一次进入界面加载数据的回调函数
     * 
     */
    final GalHttpLoadTextCallBack loadCleanServicesCallBack = new GalHttpLoadTextCallBack() {
        @Override
        public void textLoaded(String text) {
            // 解析返回的JSON字符串
            CleaningItemResult msgList = MierJsonUtils.readValue(text, new TypeToken<CleaningItemResult>() {
            }.getType());
            // 成功
            CleaningItemBody body = msgList.getBody();
            List<CleaningItemVO> list = body.getExtra();
            if (msgList != null && body != null && list.size() != 0) {
                initStarComponents(list);
            }

        }
    };
}
