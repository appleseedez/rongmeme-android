package org.dragon.rmm.view.hourlyemployee;

import java.util.List;

import org.dragon.rmm.R;
import org.dragon.rmm.domain.HourlyEmployeeItemVO;
import org.dragon.rmm.widget.waterfall.bitmaputil.ImageFetcher;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 保洁发送预约界面组件
 * 
 * @author dengjie
 * 
 */
public class HourlyEmployeeSendAppointmentActivity extends Activity {

    private LinearLayout waiterItemTableContent;
    private Button sendAppointmentBtn;
    private ImageFetcher mImageFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hourlyemployee_send_appointment);
        // 初始化图片下载工具类
        mImageFetcher = new ImageFetcher(HourlyEmployeeSendAppointmentActivity.this, 0);
        initComponents();
        initLinsteners();
        initWaiterComponents();
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
        sendAppointmentBtn = (Button) findViewById(R.id.hsa_confirm_appointment_btn);
        waiterItemTableContent = (LinearLayout) findViewById(R.id.hsa_waiter_table);

    }

    private void initWaiterComponents() {
        List<HourlyEmployeeItemVO> list = (List<HourlyEmployeeItemVO>) getIntent().getSerializableExtra(
                "hourlyEmployeeItems");
        for (int i = 0; i < list.size(); i++) {
            // 遍历生成item的
            HourlyEmployeeItemVO hei = list.get(i);
            String name = hei.getName();

            LinearLayout waiterItemView = (LinearLayout) getLayoutInflater().inflate(R.layout.waiter_item, null);
            ImageView employeeHeadPortraitImageView = (ImageView) waiterItemView
                    .findViewById(R.id.wi_employeeHeadPortraitImageView);
            TextView employeeName = (TextView) waiterItemView.findViewById(R.id.wi_employee_name);

            // 获取头像
            String headPortraitUrlPath = hei.getAvatar();
            if (!TextUtils.isEmpty(headPortraitUrlPath)) {
                mImageFetcher.loadImage(headPortraitUrlPath, employeeHeadPortraitImageView);
            } else {
                employeeHeadPortraitImageView.setImageResource(R.drawable.default_head_portrait);
            }

            employeeName.setText(name);

            TableRow tablerow = new TableRow(HourlyEmployeeSendAppointmentActivity.this);
            for (int j = 0; j < 2; j++) {
                tablerow.addView(waiterItemView);
            }
            waiterItemTableContent.addView(tablerow, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));

        }
    }

    /**
     * 初始化监听器
     */
    private void initLinsteners() {
        sendAppointmentBtn.setOnClickListener(sendAppointmentBtnOnClickListener);
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

    /**
     * 发送预约按钮点击事件
     * 
     */
    OnClickListener sendAppointmentBtnOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    // --------GalHttpLoadTextCallBack区-----------//

}
