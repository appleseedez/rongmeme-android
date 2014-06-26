package org.dragon.rmm.view.cleaning;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.R;
import org.dragon.rmm.domain.CleaningBigItemVO;
import org.dragon.rmm.domain.CleaningItemBody;
import org.dragon.rmm.domain.CleaningItemResult;
import org.dragon.rmm.domain.CleaningItemVO;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

/**
 * 保洁首页界面组件
 * 
 * @author dengjie
 * 
 */
public class CleaningActivity extends Activity {

    /**
     * 星级服务的线性布局
     */
    private LinearLayout startItemContent;

    private ImageButton backNavigationImagebutton;
    private Button cleaningViewFlowBtn;
    private Button cleaningCustomFlowBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cleaning);

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
        getStarValue();
        backNavigationImagebutton = (ImageButton) findViewById(R.id.cleaning_back_navigation_imagebutton);
        cleaningViewFlowBtn = (Button) findViewById(R.id.cleaning_view_flow_btn);
        cleaningCustomFlowBtn = (Button) findViewById(R.id.cleaning_custom_flow_btn);
        
    }

    /**
     * 获取星级服务内容全部获取出来
     * 
     * @return
     */
    private void getStarValue() {
        // CleaningDAO.getItelFriendsByCurUser(getStarValueCallBack);
        List<CleaningItemVO> list = new ArrayList<CleaningItemVO>();
        CleaningItemVO cleaningItemVO1 = new CleaningItemVO(1, "bj", "A服务", 1, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO2 = new CleaningItemVO(2, "bj", "B服务", 2, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO3 = new CleaningItemVO(3, "bj", "C服务", 3, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO4 = new CleaningItemVO(4, "bj", "D服务", 4, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO5 = new CleaningItemVO(5, "bj", "E服务", 5, "", 1d, 1d, "");

        CleaningItemVO cleaningItemVO11 = new CleaningItemVO(6, "bj", "A1服务", 1, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO21 = new CleaningItemVO(7, "bj", "B1服务", 2, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO31 = new CleaningItemVO(8, "bj", "C1服务", 3, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO41 = new CleaningItemVO(9, "bj", "D1服务", 4, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO51 = new CleaningItemVO(10, "bj", "E1服务", 5, "", 1d, 1d, "");

        CleaningItemVO cleaningItemVO111 = new CleaningItemVO(11, "bj", "A2服务", 1, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO211 = new CleaningItemVO(12, "bj", "B2服务", 2, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO311 = new CleaningItemVO(13, "bj", "C2服务", 3, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO411 = new CleaningItemVO(14, "bj", "D2服务", 4, "", 1d, 1d, "");
        CleaningItemVO cleaningItemVO511 = new CleaningItemVO(15, "bj", "E2服务", 5, "", 1d, 1d, "");

        list.add(cleaningItemVO1);
        list.add(cleaningItemVO2);
        list.add(cleaningItemVO3);
        list.add(cleaningItemVO4);
        list.add(cleaningItemVO5);
        list.add(cleaningItemVO11);
        list.add(cleaningItemVO21);
        list.add(cleaningItemVO31);
        list.add(cleaningItemVO41);
        list.add(cleaningItemVO51);
        list.add(cleaningItemVO111);
        list.add(cleaningItemVO211);
        list.add(cleaningItemVO311);
        list.add(cleaningItemVO411);
        list.add(cleaningItemVO511);
        initStarComponents(list);
    }

    /**
     * 初始化星级服务content
     */
    private void initStarComponents(List<CleaningItemVO> list) {
        Set<Integer> starLevels = new HashSet<Integer>();
        for (CleaningItemVO cleaningItemVO : list) {
            int starLevel = cleaningItemVO.getStarlevel();
            starLevels.add(starLevel);
        }
        List<CleaningBigItemVO> cbis = new ArrayList<CleaningBigItemVO>();
        for (Integer bigLevel : starLevels) {
            if (bigLevel.intValue() == 1) {
                // 1星级服务
                CleaningBigItemVO cgi = new CleaningBigItemVO("一星服务", 1);
                cbis.add(cgi);
            }
            if (bigLevel.intValue() == 2) {
                // 2星级服务
                CleaningBigItemVO cgi = new CleaningBigItemVO("两星服务", 2);
                cbis.add(cgi);
            }
            if (bigLevel.intValue() == 3) {
                // 3星级服务
                CleaningBigItemVO cgi = new CleaningBigItemVO("三星服务", 3);
                cbis.add(cgi);
            }
            if (bigLevel.intValue() == 4) {
                // 4星级服务
                CleaningBigItemVO cgi = new CleaningBigItemVO("四星服务", 4);
                cbis.add(cgi);
            }
            if (bigLevel.intValue() == 5) {
                // 5星级服务
                CleaningBigItemVO cgi = new CleaningBigItemVO("五星服务", 5);
                cbis.add(cgi);
            }
        }

        for (CleaningBigItemVO cbi : cbis) {
            List<CleaningItemVO> cleaningItems = new ArrayList<CleaningItemVO>();
            for (CleaningItemVO cleaningItemVO : list) {
                if (cbi.getStarlevel() == cleaningItemVO.getStarlevel()) {
                    cleaningItems.add(cleaningItemVO);
                }
            }
            // 如果星级一样，那么就加入
            cbi.setCleaningItems(cleaningItems);
        }

        // 星级服务的content
        startItemContent = (LinearLayout) findViewById(R.id.start_item_content);
        for (CleaningBigItemVO cbi : cbis) {
            List<CleaningItemVO> cis = cbi.getCleaningItems();
            LinearLayout startItemView = (LinearLayout) getLayoutInflater().inflate(R.layout.star_item,
                    startItemContent, false);
            startItemView.setOnClickListener(startItemViewOnClickListener);
            startItemView.setTag(cbi.getStarlevel());
            startItemView.setTag(R.id.cleaning_big_category_tag,cbi);
            TextView bigCategoryNameTextView = (TextView) startItemView.findViewById(R.id.si_big_category_name_title);
            ImageButton bigCategoryPullDownImageButton = (ImageButton) startItemView
                    .findViewById(R.id.si_big_category_pull_down_imageButton);
            bigCategoryPullDownImageButton.setOnClickListener(bigCategoryPullDownOnClickListener);
            bigCategoryPullDownImageButton.setTag(cbi.getStarlevel());
            bigCategoryNameTextView.setText(cbi.getName().trim());
            for (CleaningItemVO ci : cis) {
                View startSmallItemView = getLayoutInflater().inflate(R.layout.star_small_item, null, false);
                TextView smallCategoryNameTextView = (TextView) startSmallItemView
                        .findViewById(R.id.ssi_small_category_name_title);
                smallCategoryNameTextView.setText(ci.getName());
                startSmallItemView.setTag(ci.getStarlevel());
                LinearLayout starSmallItemViewContent = (LinearLayout) startItemView
                        .findViewById(R.id.si_small_category_content);
                starSmallItemViewContent.addView(startSmallItemView);
            }
            // 从start_item.xml文件导入
            startItemContent.addView(startItemView, 0);
        }

    }

    /**
     * 初始化监听器
     */
    private void initLinsteners() {
        backNavigationImagebutton.setOnClickListener(backNavigationButtonOnClickListener);
        cleaningViewFlowBtn.setOnClickListener(cleaningViewFlowBtnOnClickListener);
        cleaningCustomFlowBtn.setOnClickListener(cleaningCustomFlowBtnOnClickListener);
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
     * 观看流程按钮点击事件
     * 
     */
    OnClickListener cleaningViewFlowBtnOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            //TODO dengjie
        }
    };
    /**
     * 我要定制按钮点击事件
     * 
     */
    OnClickListener cleaningCustomFlowBtnOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CleaningActivity.this,CleaningCustomActivity.class);
            startActivity(intent);
        }
    };
    /**
     * 星级服务item点击事件
     * 
     */
    OnClickListener bigCategoryPullDownOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            View starItem = (View) v.getParent().getParent();
            View starSmallContent = starItem.findViewById(R.id.si_small_category_content);
            starSmallContent.setVisibility(View.VISIBLE);
        }
    };

    /**
     * 星级服务item点击事件
     * 
     */
    OnClickListener startItemViewOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            CleaningBigItemVO cleaningBigItem = (CleaningBigItemVO) v.getTag(R.id.cleaning_big_category_tag);
            Intent intent = new Intent(CleaningActivity.this, CleaningAppointmentActivity.class);
            intent.putExtra("cleaningBigItem", cleaningBigItem);
            startActivity(intent);
        }
    };
    // --------GalHttpLoadTextCallBack区-----------//
    /**
     * 第一次进入界面加载数据的回调函数
     * 
     */
    final GalHttpLoadTextCallBack getStarValueCallBack = new GalHttpLoadTextCallBack() {
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