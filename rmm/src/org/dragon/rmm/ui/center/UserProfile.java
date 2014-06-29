package org.dragon.rmm.ui.center;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoEditUserInfo;
import org.dragon.rmm.model.RespEditUserInfo;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class UserProfile extends Activity implements OnClickListener, TextWatcher, ResponseListener {

	private boolean mAddressHasChanged;
	
	private String mAddress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_user_profile);
		
		initViewLayout();
		updateViewLayout();
	}
	
	private void initViewLayout() {
		Button backward = (Button) findViewById(R.id.navigator_backward);
		backward.setOnClickListener(this);
		
		TextView title = (TextView) findViewById(R.id.navigator_title);
		title.setText(R.string.activity_label_user_profile);
		
		Button forward = (Button) findViewById(R.id.navigator_forward);
		forward.setVisibility(View.INVISIBLE);
		
		EditText address = (EditText) findViewById(R.id.address);
		address.addTextChangedListener(this);
		
		TextView logout = (TextView) findViewById(R.id.user_logout);
		logout.setOnClickListener(this);
	}
	
	private void requestDataSource() {
		InfoEditUserInfo request = new InfoEditUserInfo();
		
		request.setUserid(ApiServer.mUser.userid);
		request.setAddress(mAddress);
		
		ApiServer.getInstance(this).editUserInfo(request, this);
	}
	
	private void updateViewLayout() {
		TextView telephone = (TextView) findViewById(R.id.telephone);
		telephone.setText(ApiServer.mUser.username);
		
		EditText address = (EditText) findViewById(R.id.address);
		address.setText(ApiServer.mUser.address);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.navigator_backward) {
			if(mAddressHasChanged) {
				requestDataSource();
			} else { 
				finish();
			}
		} else if(view.getId() == R.id.user_logout) {
			onUserLogoutBtnClick();
		}
	}

	private void onUserLogoutBtnClick() {
		// TODO: user logout
	}

	@Override
	public void success(ApiMethod api, String response) {
		RespEditUserInfo resp = ApiServer.getGson().fromJson(response, RespEditUserInfo.class);
		if(resp.getBody().getErrorcode().length() == 0) {
			ApiServer.mUser.address = mAddress;
			updateViewLayout();
			Toast.makeText(this, R.string.user_profile_address_changed_success, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, R.string.user_profile_address_changed_failture, Toast.LENGTH_SHORT).show();
		}
		
		if(mAddressHasChanged) { finish(); }
	}

	@Override
	public void fail(ApiMethod api, VolleyError error) {
		if(mAddressHasChanged) {
			Toast.makeText(this, R.string.user_profile_address_changed_failture, Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(mAddress == null) {
			mAddress = ((EditText) findViewById(R.id.address)).getText().toString();
			return;
		}
		
		String address = ((EditText) findViewById(R.id.address)).getText().toString();
		if(!address.equals(mAddress)) {
			mAddressHasChanged = true;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}
}
