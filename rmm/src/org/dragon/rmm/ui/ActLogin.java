package org.dragon.rmm.ui;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoStore;
import org.dragon.rmm.model.InfoUserLogin;
import org.dragon.rmm.model.InfoUserLogout;
import org.dragon.rmm.model.ModelResUser;
import org.dragon.rmm.model.ResUser;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

public class ActLogin extends Activity implements OnClickListener {

	private ApiServer mApiServer;
	private EditText etUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View parent = getLayoutInflater().inflate(R.layout.act_login, null);
		setContentView(parent);
		mApiServer = ApiServer.getInstance(this);
		parent.findViewById(R.id.login_tv_enter).setOnClickListener(this);
		parent.findViewById(R.id.login_tv_regist).setOnClickListener(this);
		// mApiServer.verifyCode(new InfoVerycode("13408620260"),
		// mResponseListener);
		etUser = (EditText) parent.findViewById(R.id.login_et_user);
		mApiServer.login(new InfoUserLogin("13688494410", "123456"), mResponseListener);
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
			// mApiServer.login(new InfoUserLogin("13688494410", "123456"),
			// mResponseListener);

			// mApiServer.verifyCode(new InfoVerycode("13408620260"),
			// mResponseListener);
			// InfoRegist info = new InfoRegist("13408620260",
			// "e10adc3949ba59abbe56e057f20f883e", etUser.getText().toString(),
			// 0, 0);
			// mApiServer.regist(info, mResponseListener);
			mApiServer.shopInfo(new InfoStore(10), mResponseListener);
			break;
		case R.id.login_tv_regist:// 注册
			if (null != mUser) {
				mApiServer.logout(new InfoUserLogout(mUser.userid, mUser.nickname), mResponseListener);
			}
			break;
		}
	}

	private ResUser mUser;

	private ResponseListener mResponseListener = new ResponseListener() {

		@Override
		public void success(ApiMethod api, String response) {
			System.out.println("response=" + response);
			switch (api) {
			case API_LOGIN:
				// Type t = (Type)
				// BaseModel.class.getClass().getGenericSuperclass();
				//
				// Type[] params = (Type[]) ((ParameterizedType)
				// t).getActualTypeArguments();
				//
				// Class<ResUser> ResUser = (Class<ResUser>) params[0];
				//
				ModelResUser result = new Gson().fromJson(response, ModelResUser.class);
				mUser = result.body;
				break;
			}
		}

		@Override
		public void fail(ApiMethod api, VolleyError error) {
			System.out.println("error=" + error.getMessage());

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
