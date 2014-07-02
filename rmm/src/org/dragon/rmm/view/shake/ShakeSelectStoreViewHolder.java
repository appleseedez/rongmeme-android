package org.dragon.rmm.view.shake;

import android.widget.TextView;

/**
 * 通讯录列表每行用到的item的适配器的viewholder
 * 
 * @author dengjie
 * 
 */
public final class ShakeSelectStoreViewHolder {
    // 门店
    public TextView storeNameTextView;
    // 门店地址
    public TextView storeAddressTextView;
    // 非视图显示属性，用来标记这行是哪个位置，方便获取数据
    public int position;
    //门店ID
    public long storeid;
}
