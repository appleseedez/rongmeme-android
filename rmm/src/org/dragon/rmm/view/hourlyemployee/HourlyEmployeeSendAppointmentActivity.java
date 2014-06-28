package org.dragon.rmm.view.hourlyemployee;

import java.util.ArrayList;
import java.util.List;

import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.R;
import org.dragon.rmm.dao.HourlyEmployeeDAO;
import org.dragon.rmm.domain.HourlyEmployeeItemResult;
import org.dragon.rmm.domain.HourlyEmployeeItemVO;
import org.dragon.rmm.domain.HourlyEmployeeSendAppointmentItemForm;
import org.dragon.rmm.domain.common.Head;
import org.dragon.rmm.widget.dialog.NewMsgDialog;
import org.dragon.rmm.widget.waterfall.bitmaputil.ImageFetcher;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

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

    private TextView phoneTextview;
    private ImageButton backNavigationImagebutton;

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
        phoneTextview = (TextView) findViewById(R.id.hsa_phone_textview);
        backNavigationImagebutton = (ImageButton) findViewById(R.id.hsa_back_navigation_imagebutton);
    }

    /**
     * 生成成功的弹出框
     * 
     * @param phone
     *            用户的电话号码
     */
    private void createSuccessDialog(String phone) {
        // 生成弹出框
        final NewMsgDialog dialog = new NewMsgDialog(HourlyEmployeeSendAppointmentActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.new_msg_dialog, null);
        dialog.setView(view);
        dialog.show();
        String format = getResources().getString(R.string.nmd_msg_content_textview_default_text);
        dialog.nmdMsgContent.setText(String.format(format, phone));
        dialog.nmdCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 生成成功的弹出框
     * 
     * @param phone
     *            用户的电话号码
     */
    private void createFaildDialog() {
        // 生成弹出框
        final NewMsgDialog dialog = new NewMsgDialog(HourlyEmployeeSendAppointmentActivity.this);
        LayoutInflater factory = LayoutInflater.from(this);
        View view = factory.inflate(R.layout.new_msg_dialog, null);
        dialog.setView(view);
        dialog.show();
        String format = getResources().getString(R.string.nmd_msg_content_textview_default_faild_text);
        dialog.nmdMsgContent.setText(format);
        dialog.nmdCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
     * 发送预约按钮点击事件
     * 
     */
    OnClickListener sendAppointmentBtnOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            List<HourlyEmployeeItemVO> list = (List<HourlyEmployeeItemVO>) getIntent().getSerializableExtra(
                    "hourlyEmployeeItems");
            // TODO dengjie 这里需要获取当前用户,这些数据都要进行获取
            long storeid = 0;
            String storename = "";
            double allprice = 0;
            long userid = 0;
            String name = "";
            String phone = "";
            String address = "";
            // 进行服务转换，且求总价
            List<HourlyEmployeeSendAppointmentItemForm> services = new ArrayList<HourlyEmployeeSendAppointmentItemForm>();
            for (HourlyEmployeeItemVO hei : list) {
                HourlyEmployeeSendAppointmentItemForm hourlyEmployeeSendAppointmentItemForm = new HourlyEmployeeSendAppointmentItemForm();
                hourlyEmployeeSendAppointmentItemForm.setItemid(hei.getId());
                hourlyEmployeeSendAppointmentItemForm.setName(hei.getName());
                services.add(hourlyEmployeeSendAppointmentItemForm);
                // 累计总价
                allprice = allprice + hei.getPrice();
            }
            HourlyEmployeeDAO.createHourlyWorkerAppointment(storeid, storename, allprice, userid, name, phone, address,
                    services, createHourlyWorkerAppointmentCallBack);
        }
    };

    // --------GalHttpLoadTextCallBack区-----------//
    /**
     * 发送保洁预约回调函数
     * 
     */
    final GalHttpLoadTextCallBack createHourlyWorkerAppointmentCallBack = new GalHttpLoadTextCallBack() {
        @Override
        public void textLoaded(String text) {
            // 解析返回的JSON字符串
            HourlyEmployeeItemResult msgList = MierJsonUtils.readValue(text, new TypeToken<HourlyEmployeeItemResult>() {
            }.getType());
            // 成功
            Head head = msgList.getHead();
            int status = -1;
            if (head != null) {
                status = head.getStatus();
            }
            if (status == 0) {
                // 成功
                // 获取当前用户currentUser,根据用户获取电话号码 TODO dengjie
                String phone = phoneTextview.getText().toString();
                createSuccessDialog(phone);
            } else {
                createFaildDialog();
            }

        }
    };
}
