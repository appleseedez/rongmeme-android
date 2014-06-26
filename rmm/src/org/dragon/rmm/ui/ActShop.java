package org.dragon.rmm.ui;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoCommentList;
import org.dragon.rmm.model.InfoShop;
import org.dragon.rmm.model.ModelResCommenList;
import org.dragon.rmm.model.ModelResShop;
import org.dragon.rmm.ui.adapter.CommentAdapter;
import org.dragon.rmm.ui.center.UserOrderDetail;
import org.dragon.rmm.widget.xlistview.XListView;
import org.dragon.rmm.widget.xlistview.XListView.IXListViewListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

public class ActShop extends Activity implements OnClickListener, IXListViewListener {

	private ApiServer mApiServer;
	private XListView lvDetail;
	private NetworkImageView imIcon;
	private TextView tvName, tvExtra, tvFWLN, tvFWZZ, tvCommentTitle;
	private ViewGroup vgServices;
	private CommentAdapter mAdapter;

	private int mPage = 0;
	private final int mPSize = ApiServer.PSIZE;

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
		((TextView) parent.findViewById(R.id.actionbar_title)).setText("店铺");
		parent.findViewById(R.id.actionbar_back).setOnClickListener(this);
		View next = parent.findViewById(R.id.actionbar_next);
		next.setVisibility(View.VISIBLE);
		next.setBackgroundResource(R.drawable.btn_zxing);
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
		tvCommentTitle = (TextView) header.findViewById(R.id.shop_comment_title);
		vgServices = (ViewGroup) header.findViewById(R.id.shop_services);
		imIcon = (NetworkImageView) header.findViewById(R.id.shop_icon);
		header.findViewById(R.id.shop_call).setOnClickListener(this);
		lvDetail.addHeaderView(header);
		mAdapter = new CommentAdapter(getLayoutInflater());
		lvDetail.setAdapter(mAdapter);
	}

	private void refreshShopInfo(ModelResShop shop) {
		ApiServer.mShopInfo = shop.body;
		imIcon.setDefaultImageResId(R.drawable.icon_login);
		imIcon.setImageUrl(ApiServer.mShopInfo.logo, ApiServer.getImageLoader(this));
		tvName.setText(ApiServer.mShopInfo.name);
		tvExtra.setText(ApiServer.mShopInfo.address);
		tvFWLN.setText(ApiServer.mShopInfo.serviceconcept);
		tvFWZZ.setText(ApiServer.mShopInfo.servicetenets);
		String[] serverIds = ApiServer.mShopInfo.serviceids.split(",");
		String[] serverNames = ApiServer.mShopInfo.services.split(",");
		vgServices.removeAllViews();
		if (serverIds.length == serverNames.length) {
			for (int i = 0; i < serverIds.length; i++) {
				View item = getLayoutInflater().inflate(R.layout.list_item_service, vgServices, false);
				TextView text = (TextView) item.findViewById(android.R.id.text1);
				ImageView icon = (ImageView) item.findViewById(android.R.id.icon);
				String id = serverIds[i];
				if ("1".equals(id)) {
					icon.setImageResource(R.drawable.icon_logo);
				} else if ("2".equals(id)) {
					icon.setImageResource(R.drawable.icon_logo);
				} else if ("3".equals(id)) {
					icon.setImageResource(R.drawable.icon_logo);
				} else {
					icon.setImageResource(R.drawable.icon_logo);
				}
				text.setText(serverNames[i]);
				vgServices.addView(item);
			}
		}
	}

	private void refreshCommentList(ModelResCommenList comments) {
		if (mAdapter.getCount() + comments.body.length < mPage * ApiServer.PSIZE) {
			lvDetail.setPullLoadEnable(false);
		} else {
			lvDetail.setPullLoadEnable(true);
		}
		if ((mAdapter.getCount() == 0) && (null == comments.body || comments.body.length == 0)) {
			tvCommentTitle.setVisibility(View.GONE);
		} else {
			tvCommentTitle.setVisibility(View.VISIBLE);
		}
		mAdapter.append(comments.body);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.actionbar_back:
			finish();
			break;
		case R.id.actionbar_next:// 扫一扫
			toScanBarcode(arg0);
			break;
		case R.id.shop_call:// 打电话
			startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + ApiServer.mShopInfo.phone)));
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// IntentResult result =
		// IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		// if (result == null) {
		// super.onActivityResult(requestCode, resultCode, data);
		// return;
		// }
		// if (result.getContents() != null) {
		// Intent intent = new Intent(this, UserOrderDetail.class);
		// intent.putExtra(UserOrderDetail.INTENT_EXTRA_USER_ORDER_ID,
		// result.getContents());
		// startActivity(intent);
		// }
	}

	private void toScanBarcode(View v) {
		// IntentIntegrator integrator = new IntentIntegrator(this);
		// integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
		// integrator.setResultDisplayDuration(0);
		// integrator.setCameraId(0);
		// integrator.initiateScan();
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
				} else {
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
		Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.dialog_progress);
		return dialog;
	}

	@Override
	public void onRefresh() {
		getComment(true);
	}

	private void getComment(boolean isRefresh) {
		long id = getIntent().getLongExtra(ActMain.EXTRA_ID, -1);
		if (-1 == id) {
			Toast.makeText(this, "请重新选择商铺", Toast.LENGTH_SHORT).show();
			return;
		}
		if (isRefresh) {
			mPage = 0;
			showDialog(0);
			mApiServer.shopInfo(new InfoShop(id), mResponseListener);
		}
		InfoCommentList info = new InfoCommentList(id);
		info.start = (mPage++) * mPSize;
		mApiServer.commentList(info, mResponseListener);
	}

	@Override
	public void onLoadMore() {
		getComment(false);
	}
}
