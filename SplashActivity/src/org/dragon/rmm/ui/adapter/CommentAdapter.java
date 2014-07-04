package org.dragon.rmm.ui.adapter;

import java.util.ArrayList;

import org.dragon.rmm.R;
import org.dragon.rmm.model.ResCommentList;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {

	public static final String COLUMN_TITLE = "TITLE";
	public static final String COLUMN_CONTENT = "CONTENT";
	public static final String COLUMN_RATE = "RATE";

	private LayoutInflater mInflater;
	private ArrayList<ResCommentList> mData;

	public CommentAdapter(LayoutInflater inflater) {
		mInflater = inflater;
	}

	public CommentAdapter(LayoutInflater inflater, ArrayList<ResCommentList> data) {
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
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.list_item_comment, null);
			holder = new Holder();
			holder.tvName = (TextView) convertView.findViewById(R.id.shop_list_user);
			holder.tvComment = (TextView) convertView.findViewById(R.id.shop_list_comment);
			holder.rbScore = (RatingBar) convertView.findViewById(R.id.shop_list_rating);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		if ((position & 1) == 0) {
			convertView.setBackgroundColor(0x44aaaaaa);
		} else {
			convertView.setBackgroundColor(Color.TRANSPARENT);
		}
		bindView(holder, mData.get(position));
		return convertView;
	}

	private void bindView(Holder holder, ResCommentList dataSet) {
		if (null != dataSet.username) {
			holder.tvName.setText(dataSet.username);
		}
		holder.tvComment.setText(dataSet.content);
		holder.rbScore.setRating(dataSet.level);
	}

	public void clear() {
		if (null != mData) {
			mData.clear();
		}
		notifyDataSetChanged();
	}

	public void append(ResCommentList[] data) {
		if (null == data || data.length == 0) {
			return;
		}
		if (null == mData) {
			mData = new ArrayList<ResCommentList>();
		}
		for (int i = 0; i < data.length; i++) {
			mData.add(data[i]);
		}
		notifyDataSetChanged();
	}

	private static class Holder {
		TextView tvName;
		TextView tvComment;
		RatingBar rbScore;
	}

}
