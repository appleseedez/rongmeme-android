package org.dragon.rmm.view.hourlyemployee;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.R;
import org.dragon.rmm.dao.HourlyEmployeeDAO;
import org.dragon.rmm.domain.HourlyEmployeeItemResult;
import org.dragon.rmm.domain.HourlyEmployeeItemVO;
import org.dragon.rmm.utils.PreferenceUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

public class HourlyEmployeeActivity extends Activity {

    private ImageButton backNavigationImagebutton;
    private ImageButton nextFlowButton;
    private ListView mListView;
    private HourlyEmployeeListViewAdapter mAdapter;

    private List<HourlyEmployeeItemVO> listItems = new ArrayList<HourlyEmployeeItemVO>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置横屏
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 设置全屏
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置布局视图
        setContentView(R.layout.hourlyemployee);

        initComponents();
        initLinsteners();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        backNavigationImagebutton=(ImageButton) findViewById(R.id.he_back_navigation_imagebutton);
        nextFlowButton = (ImageButton) findViewById(R.id.he_next_flow_button);
        mListView = (ListView) findViewById(R.id.hourly_employee_xlist_view);

        mAdapter = new HourlyEmployeeListViewAdapter(HourlyEmployeeActivity.this, listItems);
        mListView.setAdapter(mAdapter);
        loadData();
        //
        mListView.setItemsCanFocus(false);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    /**
     * 初始化监听器
     */
    private void initLinsteners() {
        backNavigationImagebutton.setOnClickListener(backNavigationButtonOnClickListener);
        nextFlowButton.setOnClickListener(nextFlowButtonOnClickListener);
    }

    /**
     * onCreate的时候调用的初始化
     */
    private void loadData() {
        SharedPreferences curSp = getSharedPreferences(PreferenceUtils.PREFERENCE, 0);
        String curSessionToken = curSp.getString("curSessionToken", "");
        // 商店
        long storeid = curSp.getLong("curStoreId", 0);

        HourlyEmployeeDAO.loadHourlyWorkers(curSessionToken, storeid, loadDataCallBack);
    }

    // ---------组件监听器区-------------//
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
    OnClickListener nextFlowButtonOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            List<HourlyEmployeeItemVO> hei = new ArrayList<HourlyEmployeeItemVO>();
            // 获取check下来的item
            for (int i = 0; i < mAdapter.mChecked.size(); i++) {
                if (mAdapter.mChecked.get(i)) {
                    hei.add(listItems.get(i));
                }
            }
            if(hei==null||hei.size()==0){
                Toast.makeText(HourlyEmployeeActivity.this, R.string.next_flow_no_check, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(HourlyEmployeeActivity.this, HourlyEmployeeSendAppointmentActivity.class);
            intent.putExtra("hourlyEmployeeItems", (Serializable) hei);
            startActivity(intent);
        }
    };
    // --------GalHttpLoadTextCallBack区-----------//
    /**
     * 第一次进入界面加载数据的回调函数
     * 
     */
    final GalHttpLoadTextCallBack loadDataCallBack = new GalHttpLoadTextCallBack() {
        @Override
        public void textLoaded(String text) {
            // 解析返回的JSON字符串
            HourlyEmployeeItemResult msgList = MierJsonUtils.readValue(text, new TypeToken<HourlyEmployeeItemResult>() {
            }.getType());
            // 成功
            List<HourlyEmployeeItemVO> heis = msgList.getBody();
            if (msgList != null && heis != null && heis.size() != 0) {
                // 从新刷新需要清空现有的数据
                listItems.clear();
                listItems.addAll(heis);
                mAdapter.notifyDataSetChanged();
            }
        }
    };

}
