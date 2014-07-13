package org.dragon.rmm.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoComment;
import org.dragon.rmm.ui.widget.ShareComment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

public class ActShare extends Activity implements OnClickListener {

	private ApiServer mApiServer;

	private RatingBar rbRating;

	private static final String EXTRA_ICON = "extraIcon";
	private static final String EXTRA_NAME = "extraName";
	private static final String EXTRA_TEXT = "extraText";
	private static final String EXTRA_ORDERID = "extraOrderId";
	private static final String EXTRA_STOREID = "extraStoreId";

	private long mStoreId = -1;
	private long mOrderId = -1;

	public static Intent getIntent(Context context, long storeid, long orderid, String iconUrl, String name, String text) {
		Intent intent = new Intent(context, ActShare.class);
		intent.putExtra(EXTRA_ORDERID, orderid);
		intent.putExtra(EXTRA_STOREID, storeid);
		intent.putExtra(EXTRA_ICON, iconUrl);
		intent.putExtra(EXTRA_NAME, name);
		intent.putExtra(EXTRA_TEXT, text);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View parent = getLayoutInflater().inflate(R.layout.act_share, null);
		setContentView(parent);
		mApiServer = ApiServer.getInstance(this);
		initView(parent);
		ShareSDK.initSDK(this);
	}

	private void initView(View parent) {
		parent.findViewById(R.id.share_app).setOnClickListener(this);
		parent.findViewById(R.id.share_wechat).setOnClickListener(this);
		parent.findViewById(R.id.share_sinaweibo).setOnClickListener(this);
		parent.findViewById(R.id.actionbar_back).setOnClickListener(this);
		rbRating = (RatingBar) parent.findViewById(R.id.share_rating);
		TextView date = (TextView) parent.findViewById(R.id.share_date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);
		date.setText(format.format(new Date()));
		Intent intent = getIntent();
		if (null == intent) {
			finish();
		}
		NetworkImageView icon = (NetworkImageView) parent.findViewById(R.id.share_icon);
		icon.setDefaultImageResId(R.drawable.icon_logo);
		icon.setImageUrl(intent.getStringExtra(EXTRA_ICON), ApiServer.getImageLoader(this));
		String extraName = intent.getStringExtra(EXTRA_NAME);
		if (!TextUtils.isEmpty(extraName)) {
			((TextView) parent.findViewById(R.id.share_name)).setText(extraName);
		}
		String extraText = intent.getStringExtra(EXTRA_TEXT);
		if (!TextUtils.isEmpty(extraText)) {
			((EditText) parent.findViewById(R.id.share_content)).setText(extraText);
		}

		mOrderId = intent.getLongExtra(EXTRA_ORDERID, -1);
		mStoreId = intent.getLongExtra(EXTRA_STOREID, -1);
		if (-1 == mOrderId || -1 == mStoreId) {
			finish();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ShareSDK.stopSDK(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actionbar_back:
			finish();
			break;
		case R.id.share_app:
			float rating = ((RatingBar) findViewById(R.id.share_rating)).getRating();
			new ShareComment(this, mStoreId, mOrderId, rating).show();
			break;
		case R.id.share_wechat:
			Platform plat = null;
			ShareParams sp = getShareParams(v);
			sp.setImageData(BitmapFactory.decodeResource(getResources(), R.drawable.icon_logo));
			sp.setShareType(Platform.SHARE_IMAGE);
			// plat = ShareSDK.getPlatform("Wechat");
			plat = ShareSDK.getPlatform(this, WechatMoments.NAME);
			// plat = ShareSDK.getPlatform("WechatFavorite");
			plat.setPlatformActionListener(mPlatformActionListener);
			plat.share(sp);
			break;
		case R.id.share_sinaweibo:
			showShare(false, "SinaWeibo");
			break;
		}
	}

	private PlatformActionListener mPlatformActionListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub
			String text = arg2.get("text").toString();
			InfoComment info = new InfoComment(mStoreId, mOrderId, rbRating.getRating(), text);
			mApiServer.comment(info, mResponseListener);
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// TODO Auto-generated method stub
		}
	};

	private void showShare(boolean silent, String platform) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.icon_login, getString(R.string.app_name));
		oks.setTitle("分享");
		oks.setText("这家店不错，支持一下");
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		oks.setCallback(mPlatformActionListener);
		oks.show(this);
	}

	private ShareParams getShareParams(View v) {
		ShareParams sp = new ShareParams();
		sp.setTitle("分享");
		sp.setText("这家店不错，支持一下");
		sp.setShareType(Platform.SHARE_TEXT);
		return sp;
	}

	private ResponseListener mResponseListener = new ResponseListener() {

		@Override
		public void success(ApiMethod api, String response) {
		}

		@Override
		public void fail(ApiMethod api, VolleyError error) {
		}
	};

}
