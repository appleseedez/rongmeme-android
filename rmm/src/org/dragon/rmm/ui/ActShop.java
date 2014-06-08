package org.dragon.rmm.ui;

import java.util.HashMap;

import org.dragon.rmm.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ActShop extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ShareSDK.initSDK(this);
		View parent = getLayoutInflater().inflate(R.layout.act_business, null);
		setContentView(parent);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
