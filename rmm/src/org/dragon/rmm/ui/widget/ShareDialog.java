package org.dragon.rmm.ui.widget;

import java.util.HashMap;

import org.dragon.rmm.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShareDialog extends Dialog {

	private Context mContext;

	private static boolean init = true;
	private long storeid, orderid;

	public ShareDialog(Context context, long storeid, long orderid) {
		super(context);
		mContext = context;
		this.storeid = storeid;
		this.orderid = orderid;
		if (init) {
			ShareSDK.initSDK(mContext);
			init = false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.dialog_share);
		findViewById(R.id.share_app).setOnClickListener(mListener);
		findViewById(R.id.share_wechat).setOnClickListener(mListener);
		findViewById(R.id.share_sinaweibo).setOnClickListener(mListener);
	}

	private android.view.View.OnClickListener mListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.share_wechat:
				Platform plat = null;
				ShareParams sp = getShareParams(v);
				plat = ShareSDK.getPlatform("Wechat");
				// plat = ShareSDK.getPlatform("WechatMoments");
				// plat = ShareSDK.getPlatform("WechatFavorite");
				plat.setPlatformActionListener(mPlatformActionListener);
				plat.share(sp);
				break;
			case R.id.share_app:
				float rating = ((RatingBar) findViewById(R.id.share_rating)).getRating();
				new ShareComment(mContext, storeid, orderid, rating).show();
				break;
			case R.id.share_sinaweibo:
				showShare(false, "SinaWeibo");
				break;
			}
			dismiss();
		}
	};

	private PlatformActionListener mPlatformActionListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// TODO Auto-generated method stub
		}
	};

	private void showShare(boolean silent, String platform) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher, getContext().getString(R.string.app_name));
		oks.setTitle("分享");
		oks.setText("这家店确实很棒，一起来试一试吧");
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}
		// 令编辑页面显示为Dialog模式
		oks.setDialogMode();
		// 在自动授权时可以禁用SSO方式
		oks.disableSSOWhenAuthorize();
		oks.setCallback(mPlatformActionListener);
		oks.show(mContext);
	}

	private ShareParams getShareParams(View v) {
		ShareParams sp = new ShareParams();
		sp.setTitle("分享");
		sp.setText("这家店不错，支持一下");
		sp.setShareType(Platform.SHARE_TEXT);
		return sp;
	}
}