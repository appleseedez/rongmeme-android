package org.dragon.rmm.view.shake;

import java.util.List;

import org.dragon.rmm.R;
import org.dragon.rmm.domain.ShakeStoreItemVO;
import org.dragon.rmm.utils.PreferenceUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 自定义适配器
 * 
 * @author dengjie
 * @description 该类的部分实现模仿了SimpleAdapter
 */
public class ShakeSelectStoreListViewAdapter extends BaseAdapter {

    private List<ShakeStoreItemVO> data;
    /**
     * LayoutInflater 类是代码实现中获取布局文件的主要形式 LayoutInflater layoutInflater = LayoutInflater.from(context); View convertView
     * = layoutInflater.inflate(); LayoutInflater的使用,在实际开发种LayoutInflater这个类还是非常有用的,它的作用类似于 findViewById(),
     * 不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！ 而findViewById()是找具体xml下的具体 widget控件(如:Button,TextView等)。
     */
    private LayoutInflater layoutInflater;
    private Context context;

    public ShakeSelectStoreListViewAdapter(Context context, List<ShakeStoreItemVO> data) {
        this.context = context;
        this.data = data;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * 获取列数
     */
    public int getCount() {
        return data.size();
    }

    /**
     * 获取某一位置的数据
     */
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * 获取唯一标识
     */
    public long getItemId(int position) {
        return position;
    }

    /**
     * android绘制每一列的时候，都会调用这个方法
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ShakeSelectStoreViewHolder mailListViewHolder = null;
        if (convertView == null) {
            mailListViewHolder = new ShakeSelectStoreViewHolder();
            // 获取组件布局
            convertView = layoutInflater.inflate(R.layout.shake_select_store_xlistview_item, null);
            mailListViewHolder.storeNameTextView = (TextView) convertView.findViewById(R.id.sssxi_storeNameTextView);
            mailListViewHolder.storeAddressTextView = (TextView) convertView
                    .findViewById(R.id.sssxi_storeAddressTextView);

            // 这里要注意，是使用的tag来存储数据的。
            convertView.setTag(mailListViewHolder);
        } else {
            mailListViewHolder = (ShakeSelectStoreViewHolder) convertView.getTag();
        }
        // 绑定数据、以及事件触发
        bingUiAndData(mailListViewHolder, position);
        // 给每一行帮顶点击事件，点击后跳转到该用户的用户详细情况页面
        convertView.setOnClickListener(convertViewOnClickListener);
        return convertView;
    }

    /**
     * 将UI和数据进行绑定
     */
    private void bingUiAndData(ShakeSelectStoreViewHolder mailListViewHolder, int position) {
        // 获取头像

        // 获取名字
        mailListViewHolder.storeNameTextView.setText(data.get(position).getName());

        // 设置技巧文本
        mailListViewHolder.storeAddressTextView.setText(data.get(position).getAddress());
        // 设置tag标记每行按钮的position，好对应位置
        mailListViewHolder.position = position;
        mailListViewHolder.storeid = data.get(position).getId();
    }

    /**
     * 门店item点击事件
     * 
     */
    OnClickListener convertViewOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ShakeSelectStoreViewHolder shakeSelectStoreViewHolder = (ShakeSelectStoreViewHolder) v.getTag();
            SharedPreferences sharedPrefrences = context.getSharedPreferences(PreferenceUtils.PREFERENCE, context.MODE_WORLD_READABLE);// 得到SharedPreferences
            Editor editor = sharedPrefrences.edit();
            //把选择门店id存储起来
            editor.putLong(PreferenceUtils.PREFERENCE_SHOPID, shakeSelectStoreViewHolder.storeid);
            editor.putString(PreferenceUtils.PREFERENCE_SHOPNAME, shakeSelectStoreViewHolder.storeNameTextView.getText().toString());
            editor.putString(PreferenceUtils.PREFERENCE_SHOPADDR, shakeSelectStoreViewHolder.storeAddressTextView.getText().toString());
            ShakeSelectStoreActivity sssa = (ShakeSelectStoreActivity)context;
            sssa.finish();
        }
    };
}
