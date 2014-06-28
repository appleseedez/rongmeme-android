package org.dragon.rmm.ui;

import org.dragon.core.utils.data.MD5Utils;
import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoUserLogin;
import org.dragon.rmm.model.ResUser;
import org.dragon.rmm.utils.PreferenceUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class ActLogin extends Activity implements OnClickListener {

	private ApiServer mApiServer;
	private EditText etUser, etPwd;
	private boolean hasDialog;
	private boolean isAutoLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApiServer = ApiServer.getInstance(this);
		isAutoLogin = checkAutoLogin();
		if (!isAutoLogin) {
			setupView();
		}
	}

	private void setupView() {
		View parent = getLayoutInflater().inflate(R.layout.act_login, null);
		setContentView(parent);
		parent.findViewById(R.id.login_tv_enter).setOnClickListener(this);
		parent.findViewById(R.id.login_tv_regist).setOnClickListener(this);
		etUser = (EditText) parent.findViewById(R.id.login_et_user);
		etPwd = (EditText) parent.findViewById(R.id.login_et_pwd);
	}

	private boolean checkAutoLogin() {
		ResUser user = PreferenceUtils.getUser(this);
		if (null != user) {
			mApiServer.login(new InfoUserLogin(user.username, user.password), mResponseListener);
			return true;
		}
		return false;
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		hasDialog = true;
		Dialog dialog = new Dialog(this, R.style.dialog);
		dialog.setContentView(R.layout.dialog_progress);
		return dialog;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login_tv_enter:// 登录
			if (requestLogin()) {
				dismiss();
			}
			break;
		case R.id.login_tv_regist:// 注册
			startActivity(new Intent(ActLogin.this, ActRegist.class));
			break;
		}
	}

	private boolean requestLogin() {
		String name = etUser.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		String pwd = etPwd.getText().toString().trim();
		if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return false;
		}
		mApiServer.login(new InfoUserLogin(name, MD5Utils.encode(pwd)), mResponseListener);
		return true;
	}

	private ResponseListener mResponseListener = new ResponseListener() {

		@Override
		public void success(ApiMethod api, String response) {
			switch (api) {
			case API_LOGIN:
				if (!isAutoLogin) {
					String name = etUser.getText().toString().trim();
					if (!TextUtils.isEmpty(name)) {
						PreferenceUtils.save(ActLogin.this, PreferenceUtils.PREFERENCE_USERNAME, name);
					}
					String pwd = MD5Utils.encode(etPwd.getText().toString().trim());
					if (!TextUtils.isEmpty(pwd)) {
						PreferenceUtils.save(ActLogin.this, PreferenceUtils.PREFERENCE_USERPWD, pwd);
					}
				}
				startActivity(ActShopList.getIntent(ActLogin.this, 104.06, 30.67)); // 根据当前经纬度获取店铺列表
				finish();
				break;
			}
			dismiss();
		}

		@Override
		public void fail(ApiMethod api, VolleyError error) {
			switch (api) {
			case API_LOGIN:
				if (isAutoLogin) {
					setupView();
				}
				break;
			}
			dismiss();
		}
	};

	private void dismiss() {
		if (hasDialog) {
			hasDialog = false;
			dismissDialog(0);
		}
	}

}
