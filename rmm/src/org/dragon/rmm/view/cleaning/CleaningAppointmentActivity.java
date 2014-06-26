package org.dragon.rmm.view.cleaning;

import java.util.ArrayList;
import java.util.List;

import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.Constants;
import org.dragon.rmm.R;
import org.dragon.rmm.dao.CleaningDAO;
import org.dragon.rmm.domain.CleaningAppointmentItemForm;
import org.dragon.rmm.domain.CleaningBigItemVO;
import org.dragon.rmm.domain.CleaningItemResult;
import org.dragon.rmm.domain.CleaningItemVO;
import org.dragon.rmm.domain.common.Head;
import org.dragon.rmm.widget.dialog.NewMsgDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
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

    /**
     * 生成成功的弹出框
     * 
     * @param phone
     *            用户的电话号码
     */
    private void createSuccessDialog(String phone) {
        // 生成弹出框
        final NewMsgDialog dialog = new NewMsgDialog(CleaningAppointmentActivity.this);
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
        final NewMsgDialog dialog = new NewMsgDialog(CleaningAppointmentActivity.this);
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
            CleaningBigItemVO cleaningBigItem = (CleaningBigItemVO) getIntent().getExtras().get("cleaningBigItem");
            List<CleaningItemVO> cis = cleaningBigItem.getCleaningItems();
            // TODO dengjie 这里需要获取当前用户,这些数据都要进行获取
            long storeid = 0;
            String storename = "";
            double allprice = 0;
            long userid = 0;
            String name = "";
            String phone = "";
            String address = "";
            // 进行服务转换，且求总价
            List<CleaningAppointmentItemForm> services = new ArrayList<CleaningAppointmentItemForm>();
            for (CleaningItemVO ci : cis) {
                CleaningAppointmentItemForm cleaningAppointmentItemForm = new CleaningAppointmentItemForm();
                cleaningAppointmentItemForm.setItemid(ci.getId());
                cleaningAppointmentItemForm.setName(ci.getName());
                services.add(cleaningAppointmentItemForm);
                // 累计总价
                allprice = allprice + ci.getPrice();
            }
            CleaningDAO.createCleanAppointment(storeid, storename, allprice, userid, name, phone, address, services,
                    createCleanAppointmentCallBack);
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
     * 发送保洁预约回调函数
     * 
     */
    final GalHttpLoadTextCallBack createCleanAppointmentCallBack = new GalHttpLoadTextCallBack() {
        @Override
        public void textLoaded(String text) {
            // 解析返回的JSON字符串
            CleaningItemResult msgList = MierJsonUtils.readValue(text, new TypeToken<CleaningItemResult>() {
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
                String phone = "";
                createSuccessDialog(phone);
            } else {
                createFaildDialog();
            }

        }
    };
}