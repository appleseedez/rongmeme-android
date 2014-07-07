package org.dragon.rmm.view.cleaning;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.R;
import org.dragon.rmm.dao.CleaningDAO;
import org.dragon.rmm.domain.CleaningItemResult;
import org.dragon.rmm.domain.CleaningItemVO;
import org.dragon.rmm.utils.PreferenceUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        getStarValue();
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
        SharedPreferences curSp = getSharedPreferences(PreferenceUtils.PREFERENCE, 0);
        String curSessionToken = curSp.getString("curSessionToken", "");

        CleaningDAO.loadServicePackages(curSessionToken, getStarValueCallBack);
    }

    /**
     * 初始化星级服务content
     */
    private void initStarComponents(List<CleaningItemVO> list) {
        Collections.sort(list, new CleaningItemComparator());
        // 星级服务的content
        startItemContent = (LinearLayout) findViewById(R.id.start_item_content);
        for (CleaningItemVO cbi : list) {
            LinearLayout startItemView = (LinearLayout) getLayoutInflater().inflate(R.layout.star_item,
                    startItemContent, false);
            startItemView.setOnClickListener(startItemViewOnClickListener);
            startItemView.setTag(cbi.getStarlevel());
            startItemView.setTag(R.id.cleaning_big_category_tag, cbi);
            TextView bigCategoryNameTextView = (TextView) startItemView.findViewById(R.id.si_big_category_name_title);
            ImageView bigCategoryStartLevelImageview = (ImageView) startItemView
                    .findViewById(R.id.si_big_category_start_level_imageview);
            ImageButton bigCategoryPullDownImageButton = (ImageButton) startItemView
                    .findViewById(R.id.si_big_category_pull_down_imageButton);
            bigCategoryPullDownImageButton.setOnClickListener(bigCategoryPullDownOnClickListener);
            bigCategoryPullDownImageButton.setTag(cbi.getStarlevel());
            bigCategoryNameTextView.setText(cbi.getName().trim());
            // 获取星级服务登记然后赋值
            int starlevel = cbi.getStarlevel();
            if (starlevel == 1) {
                bigCategoryStartLevelImageview.setImageResource(R.drawable.onestart);
            } else if (starlevel == 2) {
                bigCategoryStartLevelImageview.setImageResource(R.drawable.twostart);
            } else if (starlevel == 3) {
                bigCategoryStartLevelImageview.setImageResource(R.drawable.threestart);
            } else if (starlevel == 4) {
                bigCategoryStartLevelImageview.setImageResource(R.drawable.fourstart);
            } else {
                bigCategoryStartLevelImageview.setImageResource(R.drawable.fivestart);
            }
            // 获取小项，小项存储在description中用逗号分隔
            String descs = cbi.getDescription();
            if (!TextUtils.isEmpty(descs)) {
                String[] smallItemArr = descs.split(",");
                for (String ci : smallItemArr) {
                    View startSmallItemView = getLayoutInflater().inflate(R.layout.star_small_item, null, false);
                    TextView smallCategoryNameTextView = (TextView) startSmallItemView
                            .findViewById(R.id.ssi_small_category_name_title);
                    smallCategoryNameTextView.setText(ci);
                    startSmallItemView.setTag(cbi.getStarlevel());
                    LinearLayout starSmallItemViewContent = (LinearLayout) startItemView
                            .findViewById(R.id.si_small_category_content);
                    starSmallItemViewContent.addView(startSmallItemView);
                }
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
            // TODO dengjie
        }
    };
    /**
     * 我要定制按钮点击事件
     * 
     */
    OnClickListener cleaningCustomFlowBtnOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CleaningActivity.this, CleaningCustomActivity.class);
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
            ImageButton pullDownImageButton = (ImageButton) v;
            View starItem = (View) v.getParent().getParent();
            View starSmallContent = starItem.findViewById(R.id.si_small_category_content);
            RelativeLayout bigStarItem = (RelativeLayout) starItem.findViewById(R.id.si_top_navigation_bar);
            if (starSmallContent.getVisibility() == View.GONE) {
                bigStarItem.setBackgroundColor(getResources().getColor(R.color.white));
                pullDownImageButton.setImageResource(R.drawable.horizontalline);
                starSmallContent.setVisibility(View.VISIBLE);
            } else {
                bigStarItem.setBackgroundResource(R.drawable.big_category_bg_n);
                pullDownImageButton.setImageResource(R.drawable.pull_down);
                starSmallContent.setVisibility(View.GONE);
            }

        }
    };

    /**
     * 星级服务item点击事件
     * 
     */
    OnClickListener startItemViewOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            CleaningItemVO cleaningBigItem = (CleaningItemVO) v.getTag(R.id.cleaning_big_category_tag);
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
            List<CleaningItemVO> list = msgList.getBody();
            if (msgList != null && list.size() != 0) {
                initStarComponents(list);
            }

        }
    };

    public class CleaningItemComparator implements Comparator<CleaningItemVO> {

        public int compare(CleaningItemVO o1, CleaningItemVO o2) {
            return -(o1.getStarlevel() - o2.getStarlevel());
        }
    }

    public class IntegerComparator implements Comparator<Integer> {

        public int compare(Integer o1, Integer o2) {
            return -(o1.intValue() - o2.intValue());
        }
    }
}
