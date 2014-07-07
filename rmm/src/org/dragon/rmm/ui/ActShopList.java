package org.dragon.rmm.ui;

import org.dragon.rmm.R;
import org.dragon.rmm.RmmApplication;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoPlace;
import org.dragon.rmm.model.ModelResShopList;
import org.dragon.rmm.model.ResShop;
import org.dragon.rmm.ui.adapter.ShopAdapter;
import org.dragon.rmm.utils.PreferenceUtils;
import org.dragon.rmm.widget.xlistview.XListView;
import org.dragon.rmm.widget.xlistview.XListView.IXListViewListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;

public class ActShopList extends Activity implements OnClickListener, IXListViewListener, OnItemClickListener {

	private ApiServer mApiServer;
	private XListView lvDetail;
	private ShopAdapter mAdapter;

	private ResShop mCurrentShop;

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
		lvDetail.setOnItemClickListener(this);
	}

	private void refreshShopList(ModelResShopList shoplist) {
		ResShop lastShop = PreferenceUtils.getShop(this);
		long except = -1;
		if (null != lastShop) {
			except = lastShop.id;
			mAdapter.append(lastShop);// 加入上次保存的店铺
		}
		mAdapter.append(shoplist.body, except);
		lvDetail.setItemChecked(1, true); // the position of listview header is
	}

	private DialogInterface.OnClickListener mListener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				startActivity(ActMain.getIntent(ActShopList.this, mCurrentShop.id));
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				long oldId = getSharedPreferences(PreferenceUtils.PREFERENCE_SHOPADDR, MODE_WORLD_READABLE).getLong(PreferenceUtils.PREFERENCE_SHOPID, -1);
				startActivity(ActMain.getIntent(ActShopList.this, oldId));
				break;
			}
			save(mCurrentShop);
			finish();
		}
	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.actionbar_back:
			// changeShop();

			onBackPressed();
			break;
		}
	}

	@Override
	public void onBackPressed() {
		long oldId = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_WORLD_READABLE).getLong(PreferenceUtils.PREFERENCE_SHOPID, -1);
		if (-1 != oldId) {
			startActivity(ActMain.getIntent(this, oldId));
			finish();
		} else {
			int index = lvDetail.getCheckedItemPosition();
			// the position of listview header is 0.
			mCurrentShop = (ResShop) mAdapter.getItem(index - 1);
			changeShop();
		}
	}

	private void changeShop() {
		// int index = lvDetail.getCheckedItemPosition();
		// // the position of listview header is 0.
		// mCurrentShop = (ResShop) mAdapter.getItem(index - 1);
		if (mAdapter.getCount() > 0) {
			long oldId = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_WORLD_READABLE).getLong(PreferenceUtils.PREFERENCE_SHOPID, -1);
			if ((-1 == oldId) || (oldId == mCurrentShop.id)) {
				save(mCurrentShop);
				startActivity(ActMain.getIntent(ActShopList.this, mCurrentShop.id));
				finish();
			} else {
				// 创建退出对话框
				AlertDialog changeShop = new AlertDialog.Builder(this).create();
				// 设置对话框标题
				changeShop.setTitle("切换店铺");
				// 设置对话框消息
				changeShop.setMessage("确定需要切换店铺吗");
				// 添加选择按钮并注册监听
				changeShop.setButton(DialogInterface.BUTTON_POSITIVE, "确定", mListener);
				changeShop.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", mListener);
				// 显示对话框
				changeShop.show();
			}
		}
	}

	private void save(ResShop shop) {
		SharedPreferences sp = getSharedPreferences(PreferenceUtils.PREFERENCE, MODE_WORLD_WRITEABLE);
		Editor edit = sp.edit();
		edit.putLong(PreferenceUtils.PREFERENCE_SHOPID, shop.id);
		edit.putString(PreferenceUtils.PREFERENCE_SHOPNAME, shop.name);
		edit.putString(PreferenceUtils.PREFERENCE_SHOPADDR, shop.address);
		edit.commit();
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
		Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.dialog_progress);
		return dialog;
	}

	@Override
	public void onRefresh() {
		showDialog(0);
		new AsyncTask<Void, Void, InfoPlace>() {

			@Override
			protected InfoPlace doInBackground(Void... params) {
				Location location = ((RmmApplication) getApplication()).getLastKnownLocation();
				String preLongitude = PreferenceUtils.getValue(ActShopList.this, PreferenceUtils.EXTRA_LONGITUDE);
				String preLatitude = PreferenceUtils.getValue(ActShopList.this, PreferenceUtils.EXTRA_LATITUDE);
				double longitude = -1;
				double latitude = -1;
				if (null != location) {
					longitude = location.getLongitude();
					latitude = location.getLatitude();
				} else if (!TextUtils.isEmpty(preLongitude) && !TextUtils.isEmpty(preLatitude)) {
					longitude = Double.parseDouble(preLongitude);
					latitude = Double.parseDouble(preLatitude);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return new InfoPlace(longitude, latitude);
			}

			@Override
			protected void onPostExecute(InfoPlace result) {
				mApiServer.shopList(result, mResponseListener);
			}
		}.execute();

	}

	@Override
	public void onLoadMore() {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mCurrentShop = (ResShop) mAdapter.getItem(position - 1);
		changeShop();
	}
}
