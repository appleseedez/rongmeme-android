package org.dragon.rmm.ui;

import org.dragon.core.utils.data.MD5Utils;
import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoRegist;
import org.dragon.rmm.model.InfoVerycode;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class ActRegist extends Activity implements OnClickListener {

	private ApiServer mApiServer;
	private EditText etPhone, etVerifyCode, etPwd, etRePwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View parent = getLayoutInflater().inflate(R.layout.act_regist, null);
		setContentView(parent);
		mApiServer = ApiServer.getInstance(this);
		etPhone = (EditText) parent.findViewById(R.id.regist_et_phone_verifycode);
		etVerifyCode = (EditText) parent.findViewById(R.id.regist_et_verifycode);
		etPwd = (EditText) parent.findViewById(R.id.regist_et_pwd);
		etRePwd = (EditText) parent.findViewById(R.id.regist_et_pwd_confirm);
		parent.findViewById(R.id.regist_bt_verifycode).setOnClickListener(this);
		parent.findViewById(R.id.regist_tv_regist).setOnClickListener(this);
		parent.findViewById(R.id.actionbar_back).setOnClickListener(this);
		((TextView) parent.findViewById(R.id.actionbar_title)).setText("注册");
	}

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		return new ProgressDialog(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.actionbar_back:
			finish();
			break;
		case R.id.regist_bt_verifycode:
			requestVerifyCode();
			break;
		case R.id.regist_tv_regist:// 注册
			requestRegist();
			break;
		}
	}

	private void requestRegist() {
		String phone = etPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone) || phone.length() != 11) {
			Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
			return;
		}
		String verifyCode = etVerifyCode.getText().toString().trim();
		if (TextUtils.isEmpty(verifyCode)) {
			Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		String pwd = etPwd.getText().toString().trim();
		if (TextUtils.isEmpty(verifyCode)) {
			Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		String repwd = etRePwd.getText().toString().trim();
		if (!pwd.equals(repwd)) {
			Toast.makeText(this, "两次密码不同，请重新输入", Toast.LENGTH_SHORT).show();
			return;
		}
		InfoRegist info = new InfoRegist(phone, MD5Utils.encode(pwd), verifyCode, 0, 0);
		showDialog(0);
		mApiServer.regist(info, mResponseListener);
	}

	private void requestVerifyCode() {
		String phone = etPhone.getText().toString().trim();
		if (TextUtils.isEmpty(phone) || phone.length() != 11) {
			Toast.makeText(this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();
			return;
		}
		mApiServer.verifyCode(new InfoVerycode(phone), mResponseListener);
		Toast.makeText(this, "已发送", Toast.LENGTH_SHORT).show();
	}

	private ResponseListener mResponseListener = new ResponseListener() {

		@Override
		public void success(ApiMethod api, String response) {
			switch (api) {
			case API_REGIST_VERIFYCODE:
				Toast.makeText(ActRegist.this, "请注意查收短信验证码", Toast.LENGTH_SHORT).show();
				break;
			case API_REGIST:
				dismissDialog(0);
				Toast.makeText(ActRegist.this, "用户注册成功", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(ActRegist.this, ActLogin.class));
				finish();
				break;
			}
		}

		@Override
		public void fail(ApiMethod api, VolleyError error) {
			switch (api) {
			case API_REGIST:
				dismissDialog(0);
				Toast.makeText(ActRegist.this, "用户注册失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
}
