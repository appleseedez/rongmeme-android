package org.dragon.rmm.ui;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoUser;

import com.android.volley.VolleyError;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ActLogin extends Activity implements OnClickListener {

	private ApiServer mApiServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View parent = getLayoutInflater().inflate(R.layout.act_login, null);
		setContentView(parent);
		mApiServer = ApiServer.getInstance(this);
		parent.findViewById(R.id.login_tv_enter).setOnClickListener(this);
		parent.findViewById(R.id.login_tv_regist).setOnClickListener(this);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return new ProgressDialog(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login_tv_enter:// 登录
			showDialog(0);
			// new Handler().postDelayed(new Runnable() {
			// public void run() {
			// startActivity(new Intent(ActLogin.this, ActShop.class));
			// dismissDialog(0);
			// finish();
			// }
			// }, 3000);
			mApiServer.login(new InfoUser("abc", "abc"), mResponseListener);
			break;
		case R.id.login_tv_regist:// 注册
			break;
		}
	}

	private ResponseListener mResponseListener = new ResponseListener() {

		@Override
		public void success(ApiMethod api, String response) {
			System.out.println("response=" + response);
			switch (api) {
			case API_LOGIN:

				break;
			}
		}

		@Override
		public void fail(ApiMethod api, VolleyError error) {
			System.out.println("error=" + error.toString());

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
