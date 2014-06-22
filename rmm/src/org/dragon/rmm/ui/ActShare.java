package org.dragon.rmm.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RatingBar;
import android.widget.TextView;

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
	}

	private void initView(View parent) {
		parent.findViewById(R.id.share_app).setOnClickListener(this);
		parent.findViewById(R.id.share_wechat).setOnClickListener(this);
		parent.findViewById(R.id.share_sinaweibo).setOnClickListener(this);
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
			((TextView) parent.findViewById(R.id.share_content)).setText(extraText);
		}

		mOrderId = intent.getLongExtra(EXTRA_ORDERID, -1);
		mStoreId = intent.getLongExtra(EXTRA_STOREID, -1);
		if (-1 == mOrderId || -1 == mStoreId) {
			finish();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.share_app:
			break;
		case R.id.share_wechat:
			break;
		case R.id.share_sinaweibo:
			break;
		}
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
