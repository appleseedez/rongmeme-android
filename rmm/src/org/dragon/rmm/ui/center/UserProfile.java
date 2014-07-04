package org.dragon.rmm.ui.center;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoEditUserInfo;
import org.dragon.rmm.model.InfoUserLogout;
import org.dragon.rmm.model.ResUser;
import org.dragon.rmm.model.RespEditUserInfo;
import org.dragon.rmm.ui.ActLogin;
import org.dragon.rmm.utils.PreferenceUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class UserProfile extends Activity implements OnClickListener, TextWatcher, ResponseListener {

	private boolean mAddressHasChanged;
	private ApiServer mApiServer;
	private String mAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApiServer = ApiServer.getInstance(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_user_profile);

		initViewLayout();
		updateViewLayout();
	}

	private void initViewLayout() {
		TextView backward = (TextView) findViewById(R.id.navigator_backward);
		backward.setOnClickListener(this);

		TextView title = (TextView) findViewById(R.id.navigator_title);
		title.setText(R.string.activity_label_user_profile);

		TextView forward = (TextView) findViewById(R.id.navigator_forward);
		forward.setVisibility(View.INVISIBLE);

		EditText address = (EditText) findViewById(R.id.address);
		address.addTextChangedListener(this);

		TextView confirm = (TextView) findViewById(R.id.user_confirm);
		confirm.setOnClickListener(this);
		
		TextView logout = (TextView) findViewById(R.id.user_logout);
		logout.setOnClickListener(this);
	}

	private void requestDataSource() {
		InfoEditUserInfo request = new InfoEditUserInfo();

		request.setUserid(PreferenceUtils.getUser(this).userid);
		request.setAddress(mAddress);

		mApiServer.editUserInfo(request, this);
	}

	private void updateViewLayout() {
		TextView telephone = (TextView) findViewById(R.id.telephone);
		telephone.setText(PreferenceUtils.getUser(this).username);

		EditText address = (EditText) findViewById(R.id.address);
		address.setText(PreferenceUtils.getUser(this).address);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.navigator_backward:
			onBackwardBtnClick();
			break;
		case R.id.user_confirm:
			onUserConfirmBtnClick();
			break;
		case R.id.user_logout:
			onUserLogoutBtnClick();
			break;
		}
	}

	private void onUserConfirmBtnClick() {
		if (mAddressHasChanged) {
			requestDataSource();
		}
	}

	private void onBackwardBtnClick() {
		finish();
	}

	private void onUserLogoutBtnClick() {
		ResUser user = PreferenceUtils.getUser(this);
		InfoUserLogout info = new InfoUserLogout(user.userid, user.username);
		mApiServer.logout(info, this);
	}

	@Override
	public void success(ApiMethod api, String response) {
		switch (api) {
		case API_LOGOUT:
			logout();
			break;
		default:
			changeUserInfo(response);
			break;
		}
	}

	@Override
	public void fail(ApiMethod api, VolleyError error) {
		switch (api) {
		case API_LOGOUT:
			logout();
			break;
		default:
			if (mAddressHasChanged) {
				Toast.makeText(this, R.string.user_profile_address_changed_failture, Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void logout() {
		PreferenceUtils.saveUser(UserProfile.this, new ResUser("", "", -1, "", ""));
		startActivity(new Intent(UserProfile.this, ActLogin.class));
		finish();
	}

	private void changeUserInfo(String response) {
		RespEditUserInfo resp = ApiServer.getGson().fromJson(response, RespEditUserInfo.class);
		if (resp.getBody().getErrorcode().length() == 0) {
 			PreferenceUtils.save(this, PreferenceUtils.PREFERENCE_USERADDR, mAddress);
			updateViewLayout();
			Toast.makeText(this, R.string.user_profile_address_changed_success, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, R.string.user_profile_address_changed_failture, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (mAddress == null) {
			mAddress = ((EditText) findViewById(R.id.address)).getText().toString();
			return;
		}

		String address = ((EditText) findViewById(R.id.address)).getText().toString();
		if (!address.equals(mAddress)) {
			mAddress = address;
			mAddressHasChanged = true;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}
}
