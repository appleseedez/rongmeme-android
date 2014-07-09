package org.dragon.rmm.ui.drycleaning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoDryCleanReservation;
import org.dragon.rmm.model.RespDryCleanReservation;
import org.dragon.rmm.ui.drycleaning.model.DryCleaningReservedItem;
import org.dragon.rmm.ui.drycleaning.model.DryCleaningReservedList;
import org.dragon.rmm.ui.drycleaning.model.DryCleaningUtils;
import org.dragon.rmm.utils.PreferenceUtils;
import org.dragon.rmm.utils.StringResource;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class DryCleaningReservation extends Activity implements OnClickListener, ResponseListener {

	public final String UID_CELL_TITLE   = "cell_title";
	
	public final String UID_CELL_AMOUNT  = "cell_amount";
	
	public final String UID_CELL_PRICE   = "cell_price";
	
	private SimpleAdapter mSimpleAdapter;
	
	private List<HashMap<String, Object>> mDataSet;
	
	private DryCleaningReservedList mDataSource;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_dry_cleaning_reservation);
		
		String bundle = getIntent().getStringExtra(DryCleaningPortal.INTENT_EXTRA_RESERVED_LIST);
		mDataSource = DryCleaningUtils.fromJson(bundle);
		
		initViewLayout();
		initSimleAdapter();
		initDataSet();
	}
	
	private void initViewLayout() {
		
		TextView backward = (TextView) findViewById(R.id.navigator_backward);
		backward.setOnClickListener(this);

		TextView label = (TextView) findViewById(R.id.navigator_title);
		label.setText(R.string.dry_cleaning_reserve_title);
		
		TextView forward = (TextView) findViewById(R.id.navigator_forward);
		forward.setBackgroundResource(R.drawable.icon_chat);
		forward.setOnClickListener(this);

		TextView confirmed = (TextView) findViewById(R.id.reserve_confirmed);
		confirmed.setOnClickListener(this);
		
		EditText telephone = (EditText) findViewById(R.id.telephone);
		telephone.setText(PreferenceUtils.getUser(this).username);
		
		EditText address = (EditText) findViewById(R.id.address);
		address.setText(PreferenceUtils.getUser(this).address);
	}
	
	private void initSimleAdapter() {
		
		mDataSet = new ArrayList<HashMap<String, Object>>();
		
		String[] fields = { UID_CELL_TITLE, UID_CELL_AMOUNT, UID_CELL_PRICE };
		int[] elements = { R.id.cell_title, R.id.cell_amount, R.id.cell_price };
		
		mSimpleAdapter  = new SimpleAdapter(this, mDataSet, R.layout.activity_dry_cleaning_reservation_item, fields, elements);
		
		View header = getLayoutInflater().inflate(R.layout.activity_dry_cleaning_reservation_header, null);
		View footer = getLayoutInflater().inflate(R.layout.activity_dry_cleaning_reservation_footer, null);
		
		ListView listView = (ListView) findViewById(R.id.manifest);
		
		listView.addHeaderView(header);
		listView.addFooterView(footer);
		
		listView.setAdapter(mSimpleAdapter);
	}
	
	private String getPriceText(DryCleaningReservedItem item) {
		return "￥" + item.getPrice();
	}
	
	private void initDataSet() {
		List<DryCleaningReservedItem> items = mDataSource.getReservedItems();
		
		int totalPrice = 0;
		
		for(DryCleaningReservedItem item : items) {
			HashMap<String, Object> info = new HashMap<String, Object>();
			
			info.put(UID_CELL_TITLE , item.getName());
			info.put(UID_CELL_AMOUNT, item.getAmount());
			info.put(UID_CELL_PRICE , getPriceText(item));
			
			totalPrice += item.getAmount() * item.getPrice();
			
			mDataSet.add(info);
		}
		
		TextView tvTotalPrice = (TextView) findViewById(R.id.total_price);
		tvTotalPrice.setText("￥" + totalPrice);
		
		mSimpleAdapter.notifyDataSetChanged();
	}

	private String getContentText(String telephone) {
		String content = StringResource.getString(R.string.dry_cleaning_reserve_notification_part_a) + telephone
				+ StringResource.getString(R.string.dry_cleaning_reserve_notification_part_b);
		
		return content;
	}
	
	@Override
	public void success(ApiMethod api, String response) {
		if(api != ApiMethod.API_CREATE_DRY_CLEAN_APPOINTENT) { return; }
		RespDryCleanReservation resp = ApiServer.getGson().fromJson(response, RespDryCleanReservation.class);
		if(resp.getHead().getStatus() == 0) {
			EditText telephone = (EditText) findViewById(R.id.telephone);
			Dialog dialog = new DryCleaningNotification(this, getContentText(telephone.getText().toString()));
			dialog.show();
		} else {
			Toast.makeText(this, R.string.dry_cleaning_reserve_failture, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void fail(ApiMethod api, VolleyError error) {
		Toast.makeText(this, R.string.dry_cleaning_reserve_failture, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.navigator_backward:
			onBackwordBtnClick();
			break;
		case R.id.navigator_forward:
			onForwardBtnClick();
			break;
		case R.id.reserve_confirmed:
			onReserveConfirmedBtnClick();
			break;
		}
	}

	private void onReserveConfirmedBtnClick() {
		CheckBox disclaimer = (CheckBox) findViewById(R.id.disclaimer_accept);
		if(disclaimer.isChecked()) {
			InfoDryCleanReservation request = new InfoDryCleanReservation();
			setDrcyCleanReservation(request);
			ApiServer.getInstance(this).createDryCleanReservation(request, this);
		} else {
			Toast.makeText(this, 
					R.string.dry_cleaning_reserve_disclaimer_notification, Toast.LENGTH_SHORT).show();
		}
	}
	
	private boolean setDrcyCleanReservation(InfoDryCleanReservation request) {
		request.setServicetype("gx");
		request.setSource("app");
		
		// USER INFO
		if(PreferenceUtils.getUser(this) == null) { return false; }
		request.setUserid(PreferenceUtils.getUser(this).userid);
		request.setName(PreferenceUtils.getUser(this).nickname);
		
		EditText telephone = (EditText) findViewById(R.id.telephone);
		if(telephone.getText().toString().length() == 0) {
			Toast.makeText(this, R.string.dry_cleaning_reserve_telephone_hint, Toast.LENGTH_SHORT).show(); return false;
		}
		request.setPhone(telephone.getText().toString());
		
		EditText address = (EditText) findViewById(R.id.address);
		if(address.getText().toString().length() == 0) {
			Toast.makeText(this, R.string.dry_cleaning_reserve_address_hint, Toast.LENGTH_SHORT).show(); return false;
		}
		request.setAddress(address.getText().toString());
		
		// STORE INFO
		if(PreferenceUtils.getShop(this) == null) { return false; }
		request.setStoreid(PreferenceUtils.getShop(this).id);
		request.setStorename(PreferenceUtils.getShop(this).name);
		
		List<DryCleaningReservedItem> items = mDataSource.getReservedItems();
		request.setServices(items);
		
		int totalPrice = 0;
		for(DryCleaningReservedItem item : items) {
			totalPrice += item.getAmount() * item.getPrice();
		}
		request.setAllprice(totalPrice);
		return true;
	}
	

	private void onForwardBtnClick() {
		startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + PreferenceUtils.getShop(this).phone)));
	}
	
	private void onBackwordBtnClick() {
		finish();
	}
}
