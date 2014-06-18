package org.dragon.rmm.ui;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoPlace;
import org.dragon.rmm.model.ModelResShopList;
import org.dragon.rmm.model.ResShop;
import org.dragon.rmm.ui.adapter.ShopAdapter;
import org.dragon.rmm.widget.xlistview.XListView;
import org.dragon.rmm.widget.xlistview.XListView.IXListViewListener;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

public class ActShopList extends Activity implements OnClickListener, IXListViewListener {

	private static final String EXTRA_LONGITUDE = "longitude";
	private static final String EXTRA_LATITUDE = "latitude";
	private ApiServer mApiServer;
	private XListView lvDetail;
	private ShopAdapter mAdapter;

	/**
	 * @param context
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 * @return
	 */
	public static Intent getIntent(Context context, double longitude, double latitude) {
		Intent intent = new Intent(context, ActShopList.class);
		intent.putExtra(EXTRA_LONGITUDE, longitude);
		intent.putExtra(EXTRA_LATITUDE, latitude);
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
		((TextView) parent.findViewById(R.id.actionbar_title)).setText("门店列表");
		parent.findViewById(R.id.actionbar_back).setOnClickListener(this);
	}

	private void initListview(View parent) {
		lvDetail = (XListView) parent.findViewById(R.id.shop_listview);
		lvDetail.setPullLoadEnable(false);
		lvDetail.setXListViewListener(this);
		lvDetail.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mAdapter = new ShopAdapter(getLayoutInflater());
		lvDetail.setAdapter(mAdapter);
	}

	private void refreshShopList(ModelResShopList shoplist) {
		mAdapter.append(shoplist.body);
		lvDetail.setItemChecked(1, true); // the position of listview header is
											// 0.
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.actionbar_back:
			int index = lvDetail.getCheckedItemPosition();
			ResShop item = (ResShop) mAdapter.getItem(index - 1); // the
																	// position
																	// of
																	// listview
																	// header is
																	// 0.
			startActivity(ActMain.getIntent(this, item.id));
			finish();
			break;
		}
	}

	private ResponseListener mResponseListener = new ResponseListener() {

		@Override
		public void success(ApiMethod api, String response) {
			switch (api) {
			case API_SHOPLIST:
				dismissDialog(0);
				if (lvDetail.isRefresh()) {
					mAdapter.clear();
				}
				refreshShopList(ApiServer.getGson().fromJson(response, ModelResShopList.class));
				break;
			}
			lvDetail.reset();
		}

		@Override
		public void fail(ApiMethod api, VolleyError error) {
			lvDetail.reset();
			switch (api) {
			case API_SHOPLIST:
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
		double longitude = getIntent().getDoubleExtra(EXTRA_LONGITUDE, -1);
		double latitude = getIntent().getDoubleExtra(EXTRA_LATITUDE, -1);
		showDialog(0);
		mApiServer.shopList(new InfoPlace(longitude, latitude), mResponseListener);
	}

	@Override
	public void onLoadMore() {

	}
}
