package org.dragon.rmm.ui;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoCommentList;
import org.dragon.rmm.model.InfoShop;
import org.dragon.rmm.model.ModelResCommenList;
import org.dragon.rmm.model.ModelResShop;
import org.dragon.rmm.model.ResShop;
import org.dragon.rmm.ui.adapter.CommentAdapter;
import org.dragon.rmm.widget.xlistview.XListView;
import org.dragon.rmm.widget.xlistview.XListView.IXListViewListener;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

public class ActShop extends Activity implements OnClickListener, IXListViewListener {

	private ApiServer mApiServer;
	private XListView lvDetail;
	private NetworkImageView imIcon;
	private TextView tvName, tvExtra, tvFWLN, tvFWZZ;
	private ViewGroup vgServices;
	private CommentAdapter mAdapter;

	public static Intent getIntent(Context context, long shopId) {
		Intent intent = new Intent(context, ActShop.class);
		intent.putExtra(ActMain.EXTRA_ID, shopId);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApiServer = ApiServer.getInstance(this);
		View parent = getLayoutInflater().inflate(R.layout.act_shop, null);
		setContentView(parent);
		initListview(parent);
		initActionbar(parent);
		onRefresh();
	}

	private void initActionbar(View parent) {
		((TextView) parent.findViewById(R.id.actionbar_title)).setText("门店详情");
		parent.findViewById(R.id.actionbar_back).setOnClickListener(this);
		Button next = (Button) parent.findViewById(R.id.actionbar_next);
		next.setVisibility(View.VISIBLE);
		next.setText("扫一扫");
		next.setOnClickListener(this);
	}

	private void initListview(View parent) {
		lvDetail = (XListView) parent.findViewById(R.id.shop_listview);
		lvDetail.setPullLoadEnable(false);
		lvDetail.setXListViewListener(this);
		View header = getLayoutInflater().inflate(R.layout.shop_part_header, lvDetail, false);
		tvExtra = (TextView) header.findViewById(R.id.shop_extra);
		tvName = (TextView) header.findViewById(R.id.shop_name);
		tvFWLN = (TextView) header.findViewById(R.id.shop_fwln);
		tvFWZZ = (TextView) header.findViewById(R.id.shop_fwzz);
		vgServices = (ViewGroup) header.findViewById(R.id.shop_services);
		imIcon = (NetworkImageView) header.findViewById(R.id.shop_icon);
		lvDetail.addHeaderView(header);
		mAdapter = new CommentAdapter(getLayoutInflater());
		lvDetail.setAdapter(mAdapter);
	}

	private void refreshShopInfo(ModelResShop shop) {
		ResShop info = shop.body;
		if (!TextUtils.isEmpty(info.logo)) {
			imIcon.setImageUrl(info.logo, ApiServer.getImageLoader(this));
		} else {
			imIcon.setDefaultImageResId(R.drawable.ic_launcher);
		}
		tvName.setText(info.name);
		tvExtra.setText(info.introduce);
		tvFWLN.setText(info.serviceconcept);
		tvFWZZ.setText(info.servicetenets);
		String[] serverIds = info.serviceids.split(",");
		String[] serverNames = info.services.split(",");
		vgServices.removeAllViews();
		if (serverIds.length == serverNames.length) {
			for (int i = 0; i < serverIds.length; i++) {
				TextView item = (TextView) getLayoutInflater().inflate(android.R.layout.simple_list_item_1, null);
				String id = serverIds[i];
				Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				if ("1".equals(id)) {
					item.setCompoundDrawables(null, drawable, null, null);
				} else if ("2".equals(id)) {
					item.setCompoundDrawables(null, drawable, null, null);
				} else if ("3".equals(id)) {
					item.setCompoundDrawables(null, drawable, null, null);
				} else {
					item.setCompoundDrawables(null, drawable, null, null);
				}
				item.setText(serverNames[i]);
				vgServices.addView(item);
			}
		}
	}

	private void refreshCommentList(ModelResCommenList comments) {
		mAdapter.append(comments.body);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.actionbar_back:
			finish();
			break;
		case R.id.actionbar_next:
			// 扫一扫
			break;
		default:
			break;
		}

	}

	private ResponseListener mResponseListener = new ResponseListener() {

		@Override
		public void success(ApiMethod api, String response) {
			switch (api) {
			case API_SHOPINFO:
				dismissDialog(0);
				refreshShopInfo(ApiServer.getGson().fromJson(response, ModelResShop.class));
				break;
			case API_COMMENT_LIST:
				if (lvDetail.isRefresh()) {
					mAdapter.clear();
					lvDetail.stopRefresh();
				}else {
					lvDetail.stopLoadMore();
				}
				refreshCommentList(ApiServer.getGson().fromJson(response, ModelResCommenList.class));
				break;
			}
		}

		@Override
		public void fail(ApiMethod api, VolleyError error) {
			lvDetail.reset();
			switch (api) {
			case API_SHOPINFO:
				dismissDialog(0);
				break;
			}
		}
	};

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new ProgressDialog(this);
	}

	@Override
	public void onRefresh() {
		long id = getIntent().getLongExtra(ActMain.EXTRA_ID, -1);
		if (-1 == id) {
			Toast.makeText(this, "请重新选择商铺", Toast.LENGTH_SHORT).show();
			return;
		}
		showDialog(0);
		mApiServer.shopInfo(new InfoShop(id), mResponseListener);
		mApiServer.commentList(new InfoCommentList(id), mResponseListener);
	}

	@Override
	public void onLoadMore() {

	}
}
