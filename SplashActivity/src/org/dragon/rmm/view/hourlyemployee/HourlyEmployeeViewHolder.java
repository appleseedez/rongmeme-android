package org.dragon.rmm.view.hourlyemployee;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通讯录列表每行用到的item的适配器的viewholder
 * 
 * @author dengjie
 * 
 */
public final class HourlyEmployeeViewHolder {
    // 头像
    public ImageView employeeHeadPortraitImageView;
    // 名字
    public TextView employeeNameTextView;
    // 价格范围这个需要获取后台信息后进行拼接
    public TextView priceRangeTextView;
    // 技能描述
    public TextView skillsTextView;
    // 勾选框
    public CheckBox employeeCheckBox;
    // 非视图显示属性，用来标记这行是哪个位置，方便获取数据
    public int position;
}
