package org.dragon.rmm.view.shake;

import java.util.ArrayList;
import java.util.List;

import org.dragon.rmm.R;
import org.dragon.rmm.domain.ShakeStoreItemVO;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

public class ShakeSelectStoreActivity extends Activity {

    private ListView mListView;
    private ImageButton backNavigationImagebutton;

    private ShakeSelectStoreListViewAdapter mAdapter;

    private List<ShakeStoreItemVO> listItems = new ArrayList<ShakeStoreItemVO>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置横屏
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 设置布局视图
        setContentView(R.layout.shake_select_store);

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
        backNavigationImagebutton = (ImageButton) findViewById(R.id.sss_back_navigation_imagebutton);

        mListView = (ListView) findViewById(R.id.shake_select_store_xlist_view);

        mAdapter = new ShakeSelectStoreListViewAdapter(ShakeSelectStoreActivity.this, listItems);
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
    }

    /**
     * onCreate的时候调用的初始化
     */
    private void loadData() {
        // TODO dengjie 将前面摇一摇出来的数据获取出来然后赋值
        // 从新刷新需要清空现有的数据
        listItems.clear();
        listItems.addAll((List<ShakeStoreItemVO>) getIntent().getSerializableExtra("nearbyStores"));
        mAdapter.notifyDataSetChanged();
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
    // --------GalHttpLoadTextCallBack区-----------//

}
