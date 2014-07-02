package org.dragon.rmm.view.cleaning;

import java.util.ArrayList;
import java.util.List;

import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.Constants;
import org.dragon.rmm.R;
import org.dragon.rmm.dao.CleaningDAO;
import org.dragon.rmm.domain.CleaningAppointmentItemForm;
import org.dragon.rmm.domain.CleaningAppointmentResult;
import org.dragon.rmm.domain.CleaningItemResult;
import org.dragon.rmm.domain.CleaningItemVO;
import org.dragon.rmm.domain.common.Head;
import org.dragon.rmm.utils.PreferenceUtils;
import org.dragon.rmm.widget.dialog.NewMsgDialog;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

    private List<CleaningItemVO> list;

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
        SharedPreferences curSp = getSharedPreferences(PreferenceUtils.PREFERENCE, 0);
        String curSessionToken = curSp.getString("curSessionToken", "");
        CleaningDAO.loadCleanServices(curSessionToken, loadCleanServicesCallBack);
    }

    private void initStarComponents(List<CleaningItemVO> list) {
        for (CleaningItemVO ct : list) {
            // 遍历生成item的
            String name = ct.getName();
            View checkStartSmallItemView = (View) getLayoutInflater().inflate(R.layout.star_small_item_custom, null);
            TextView smallCategoryNameTitleTextView = (TextView) checkStartSmallItemView
                    .findViewById(R.id.ssic_small_category_name_title);
            smallCategoryNameTitleTextView.setText(name);
            checkStartSmallItemView.setTag(ct);
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
        final NewMsgDialog dialog = new NewMsgDialog(CleaningCustomActivity.this);
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
        final NewMsgDialog dialog = new NewMsgDialog(CleaningCustomActivity.this);
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
            List<CleaningItemVO> cis = new ArrayList<CleaningItemVO>();
            for (int i = 0; i < list.size(); i++) {
                RelativeLayout smallStartItem = (RelativeLayout) smallStartItemContent.getChildAt(i);
                CheckBox checkbox = (CheckBox) smallStartItem
                        .findViewById(R.id.ssic_small_category_checkbox_imageButton);
                if (checkbox.isChecked()) {
                    // 如果被选中，那么这个索引的服务就是要被预约提交的
                    cis.add((CleaningItemVO) smallStartItem.getTag());
                }
            }
            
            // 进行服务转换，且求总价
            double allprice=0;
            List<CleaningAppointmentItemForm> services = new ArrayList<CleaningAppointmentItemForm>();
            for (CleaningItemVO ci : cis) {
                CleaningAppointmentItemForm cleaningAppointmentItemForm = new CleaningAppointmentItemForm();
                cleaningAppointmentItemForm.setItemid(ci.getId());
                cleaningAppointmentItemForm.setName(ci.getName());
                services.add(cleaningAppointmentItemForm);
                // 累计总价
                allprice = allprice + ci.getPrice();
            }
            SharedPreferences curSp = getSharedPreferences(PreferenceUtils.PREFERENCE, 0);
            String curSessionToken = curSp.getString("curSessionToken", "");
            long userid = curSp.getLong("curUserId", 0);
            String name = curSp.getString("curUserName", "");
            String phone = curSp.getString("curUserPhone", "");
            String address = curSp.getString("curUserAddress", "");
            //商店
            long storeid = curSp.getLong("curStoreId", 0);
            String storename = curSp.getString("curStoreName", "");
            
            CleaningDAO.createCleanAppointment(storeid, storename, allprice, userid, name, phone, address,curSessionToken, services,
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
            list =  msgList.getBody();
            if (msgList != null && list.size() != 0) {
                initStarComponents(list);
            }

        }
    };

    /**
     * 发送保洁预约回调函数
     * 
     */
    final GalHttpLoadTextCallBack createCleanAppointmentCallBack = new GalHttpLoadTextCallBack() {
        @Override
        public void textLoaded(String text) {
            // 解析返回的JSON字符串
            CleaningAppointmentResult msgList = MierJsonUtils.readValue(text, new TypeToken<CleaningAppointmentResult>() {
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
