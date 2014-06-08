package org.dragon.rmm.ui;

import org.dragon.rmm.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

public class ActLogin extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View parent = getLayoutInflater().inflate(R.layout.act_login, null);
		setContentView(parent);

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
			new Handler().postDelayed(new Runnable() {
				public void run() {
					startActivity(new Intent(ActLogin.this, ActShop.class));
					dismissDialog(0);
					finish();
				}
			}, 3000);
			break;
		case R.id.login_tv_regist:// 注册
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
