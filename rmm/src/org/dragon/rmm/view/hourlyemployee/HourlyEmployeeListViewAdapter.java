package org.dragon.rmm.view.hourlyemployee;

import java.util.ArrayList;
import java.util.List;

import org.dragon.rmm.R;
import org.dragon.rmm.domain.HourlyEmployeeItemVO;
import org.dragon.rmm.widget.waterfall.bitmaputil.ImageFetcher;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 喜欢、粉丝、关注人员列表用到的自定义适配器
 * 
 * @author dengjie
 * @description 该类的部分实现模仿了SimpleAdapter
 */
public class HourlyEmployeeListViewAdapter extends BaseAdapter {

	private List<HourlyEmployeeItemVO> data;
	/**
	 * LayoutInflater 类是代码实现中获取布局文件的主要形式 LayoutInflater layoutInflater =
	 * LayoutInflater.from(context); View convertView =
	 * layoutInflater.inflate();
	 * LayoutInflater的使用,在实际开发种LayoutInflater这个类还是非常有用的,它的作用类似于 findViewById(),
	 * 不同点是LayoutInflater是用来找layout下xml布局文件，并且实例化！ 而findViewById()是找具体xml下的具体
	 * widget控件(如:Button,TextView等)。
	 */
	private LayoutInflater layoutInflater;
	private Context context;

	private ImageFetcher mImageFetcher;
	List<Boolean> mChecked = new ArrayList<Boolean>();

	public HourlyEmployeeListViewAdapter(Context context,
			List<HourlyEmployeeItemVO> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
		// 初始化图片下载工具类
		mImageFetcher = new ImageFetcher(context, 0);
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
		mChecked.add(false);
		HourlyEmployeeViewHolder mailListViewHolder = null;
		if (convertView == null) {
			mailListViewHolder = new HourlyEmployeeViewHolder();
			// 获取组件布局
			convertView = layoutInflater.inflate(
					R.layout.hourlyemployee_xlistview_item, null);
			mailListViewHolder.employeeHeadPortraitImageView = (ImageView) convertView
					.findViewById(R.id.employeeHeadPortraitImageView);
			mailListViewHolder.employeeNameTextView = (TextView) convertView
					.findViewById(R.id.employeeNameTextView);
			mailListViewHolder.priceRangeTextView = (TextView) convertView
					.findViewById(R.id.priceRangeTextView);
			mailListViewHolder.skillsTextView = (TextView) convertView
					.findViewById(R.id.skillsTextView);
			mailListViewHolder.employeeCheckBox = (CheckBox) convertView
					.findViewById(R.id.employeeCheckBox);

			final int p = position;
			mailListViewHolder.employeeCheckBox
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							CheckBox cb = (CheckBox) v;
							mChecked.set(p, cb.isChecked());
						}
					});

			// -----临时添加的代码 begin-------
			final HourlyEmployeeViewHolder temp = mailListViewHolder;
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					temp.employeeCheckBox.performClick();
				}
			});
			// -----临时添加的代码 end -------

			// 这里要注意，是使用的tag来存储数据的。
			convertView.setTag(mailListViewHolder);
		} else {
			mailListViewHolder = (HourlyEmployeeViewHolder) convertView
					.getTag();
		}
		// 绑定数据、以及事件触发
		bingUiAndData(mailListViewHolder, position);
		// 给每一行帮顶点击事件，点击后跳转到该用户的用户详细情况页面
		// convertView.setOnClickListener(convertViewOnClickListener);
		// mailListViewHolder.employeeCheckBox.setChecked(mChecked.get(position));
		return convertView;
	}

	/**
	 * 将UI和数据进行绑定
	 */
	private void bingUiAndData(HourlyEmployeeViewHolder mailListViewHolder,
			int position) {

		// 获取头像
		String headPortraitUrlPath = data.get(position).getAvatar();
		if (!TextUtils.isEmpty(headPortraitUrlPath)) {
			mImageFetcher.loadImage(headPortraitUrlPath,
					mailListViewHolder.employeeHeadPortraitImageView);
		} else {
			mailListViewHolder.employeeHeadPortraitImageView
					.setImageResource(R.drawable.default_head_portrait);
		}

		// 获取名字
		mailListViewHolder.employeeNameTextView.setText(data.get(position)
				.getName());
		// 获取价格范围字符串
		StringBuilder priceRangeSb = new StringBuilder(20)
				.append("￥   ")
				.append(Double.valueOf(data.get(position).getLowprice())
						.toString())
				.append("  -  ")
				.append(Double.valueOf(data.get(position).getPrice())
						.toString()).append("  /  2h");
		mailListViewHolder.priceRangeTextView.setText(priceRangeSb.toString());
		// 设置技巧文本
		mailListViewHolder.skillsTextView.setText(data.get(position)
				.getSkills());
		// 设置tag标记每行按钮的position，好对应位置
		mailListViewHolder.position = position;
		// mailListViewHolder.employeeCheckBox.setOnClickListener(sendImMsgButtonClickListener);
	}

}
