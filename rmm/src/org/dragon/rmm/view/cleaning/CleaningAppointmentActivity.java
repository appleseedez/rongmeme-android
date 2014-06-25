package org.dragon.rmm.view.cleaning;

import java.io.Serializable;
import java.util.List;

import org.dragon.rmm.Constants;
import org.dragon.rmm.R;
import org.dragon.rmm.domain.CleaningBigItemVO;
import org.dragon.rmm.domain.CleaningItemVO;
import org.dragon.rmm.view.hourlyemployee.HourlyEmployeeSendAppointmentActivity;

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

/**
 * 保洁预约界面组件
 * 
 * @author dengjie
 * 
 */
public class CleaningAppointmentActivity extends Activity {

    private TextView tipMsgTextview;
    private LinearLayout smallStartItemContent;
    private Button confirmAppointmentBtn;
    private Button contactCustomServiceBtn;
    private ImageButton backNavigationImagebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cleaning_appointment);

        initComponents();
        initLinsteners();
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
        confirmAppointmentBtn = (Button) findViewById(R.id.ca_confirm_appointment_btn);
        contactCustomServiceBtn = (Button) findViewById(R.id.ca_contact_custom_service_btn);
        backNavigationImagebutton = (ImageButton) findViewById(R.id.ca_back_navigation_imagebutton);

        CleaningBigItemVO cleaningBigItem = (CleaningBigItemVO) getIntent().getExtras().get("cleaningBigItem");
        List<CleaningItemVO> cis = cleaningBigItem.getCleaningItems();
        int starlevel = cleaningBigItem.getStarlevel();
        String tipMsgTextviewStr = "您选择了一星级保洁服务";
        if (starlevel == 1) {
            tipMsgTextviewStr = "您选择了一星级保洁服务";
        } else if (starlevel == 2) {
            tipMsgTextviewStr = "您选择了二星级保洁服务";
        } else if (starlevel == 3) {
            tipMsgTextviewStr = "您选择了三星级保洁服务";
        } else if (starlevel == 4) {
            tipMsgTextviewStr = "您选择了四星级保洁服务";
        } else {
            tipMsgTextviewStr = "您选择了五星级保洁服务";
        }
        tipMsgTextview = (TextView) findViewById(R.id.ca_tip_msg_textview);
        tipMsgTextview.setText(tipMsgTextviewStr);
        smallStartItemContent = (LinearLayout) findViewById(R.id.ca_small_start_item_content);

        for (CleaningItemVO ct : cis) {
            // 遍历生成item的
            String name = ct.getName();
            View checkStartSmallItemView = (View) getLayoutInflater().inflate(R.layout.star_small_item_appointment,
                    null);
            TextView smallCategoryNameTitleTextView = (TextView) checkStartSmallItemView
                    .findViewById(R.id.ssia_small_category_name_title);
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

}
