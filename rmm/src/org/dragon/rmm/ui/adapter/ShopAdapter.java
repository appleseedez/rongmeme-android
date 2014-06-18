package org.dragon.rmm.ui.adapter;

import java.util.ArrayList;

import org.dragon.rmm.R;
import org.dragon.rmm.model.ResShop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ShopAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<ResShop> mData;

	public ShopAdapter(LayoutInflater inflater) {
		mInflater = inflater;
	}

	public ShopAdapter(LayoutInflater inflater, ArrayList<ResShop> data) {
		mInflater = inflater;
		mData = data;
	}

	@Override
	public int getCount() {
		return null == mData ? 0 : mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.list_item_shop, null);
			holder = new Holder();
			holder.tvName = (TextView) convertView.findViewById(R.id.place_list_name);
			holder.tvAddress = (TextView) convertView.findViewById(R.id.place_list_address);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		bindView(holder, mData.get(position));
		return convertView;
	}

	private void bindView(Holder holder, ResShop dataSet) {
		holder.tvName.setText(dataSet.name);
		holder.tvAddress.setText(dataSet.address);
	}

	public void clear(){
		mData.clear();
		notifyDataSetChanged();
	}
	
	public void append(ResShop[] data) {
		if (null == data || data.length == 0) {
			return;
		}
		if (null == mData) {
			mData = new ArrayList<ResShop>();
		}
		for (int i = 0; i < data.length; i++) {
			mData.add(data[i]);
		}
		notifyDataSetChanged();
	}

	private static class Holder {
		TextView tvName;
		TextView tvAddress;
	}

}
