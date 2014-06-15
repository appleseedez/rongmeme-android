package org.dragon.rmm.ui;

import org.dragon.core.utils.data.MD5Utils;
import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoUserLogin;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View parent = getLayoutInflater().inflate(R.layout.act_login, null);
		setContentView(parent);
		mApiServer = ApiServer.getInstance(this);
		parent.findViewById(R.id.login_tv_enter).setOnClickListener(this);
		parent.findViewById(R.id.login_tv_regist).setOnClickListener(this);
		etUser = (EditText) parent.findViewById(R.id.login_et_user);
		etPwd = (EditText) parent.findViewById(R.id.login_et_pwd);
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new ProgressDialog(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.login_tv_enter:// 登录
			if (requestLogin()) {
				showDialog(0);
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
			dismissDialog(0);
			switch (api) {
			case API_LOGIN:
				startActivity(new Intent(ActLogin.this, ActMain.class));
				finish();
				break;
			}
		}

		@Override
		public void fail(ApiMethod api, VolleyError error) {
			dismissDialog(0);
		}
	};

}
