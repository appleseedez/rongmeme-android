package org.dragon.rmm.ui.center;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.api.ResponseListener;
import org.dragon.rmm.model.InfoOrderOfNo;
import org.dragon.rmm.model.RespOrderOfNo;
import org.dragon.rmm.payment.Payment;
import org.dragon.rmm.payment.Result;
import org.dragon.rmm.ui.ActShare;
import org.dragon.rmm.ui.center.model.UserOrder;
import org.dragon.rmm.ui.center.model.UserOrderConsts;
import org.dragon.rmm.ui.center.model.UserOrderService;
import org.dragon.rmm.ui.center.model.UserOrderUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

public class UserOrderDetail extends Activity implements OnClickListener, ResponseListener {
	
	public static final String INTENT_EXTRA_USER_ORDER    = "extra_user_order";
	
	public static final String INTENT_EXTRA_USER_ORDER_ID = "extra_user_order_id";
	
	public final String UID_SERVICE_ITEM_TITLE   = "service_item_title";
	
	public final String UID_SERVICE_ITEM_AMOUNT  = "service_item_amount";
	
	public final String UID_SERVICE_ITEM_PRICE   = "service_item_price";

	private UserOrder mUserOrder;
	
	private SimpleAdapter mSimpleAdapter;
	
	private List<HashMap<String, Object>> mDataSet;
	
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_user_order_detail);
		
		initViewLayout();
		initSimleAdapter();
		
		String bundle = getIntent().getStringExtra(INTENT_EXTRA_USER_ORDER);
		if(bundle != null) {
			mUserOrder = UserOrderUtils.fromJson(bundle);
			updateDataSetAndViewLayout();
		} else {
			String id = getIntent().getStringExtra(INTENT_EXTRA_USER_ORDER_ID);
			requestDataSource(id);
		}
		
		mHandler = new Handler(new PaymentHandler());
	}
	
	private void initViewLayout() {
		Button backward = (Button) findViewById(R.id.navigator_backward);
		backward.setOnClickListener(this);
		
		Button forward = (Button) findViewById(R.id.navigator_forward);
		forward.setOnClickListener(this);
	}
	
	private void initSimleAdapter() {
		
		mDataSet = new ArrayList<HashMap<String, Object>>();
		
		String[] fields = { UID_SERVICE_ITEM_TITLE, UID_SERVICE_ITEM_AMOUNT, UID_SERVICE_ITEM_PRICE };
		int[] elements = { R.id.service_item_title, R.id.service_item_amount, R.id.service_item_price };
		
		mSimpleAdapter  = new SimpleAdapter(this, mDataSet, R.layout.activity_user_order_detail_item, fields, elements);
		
		View header = getLayoutInflater().inflate(R.layout.activity_user_order_detail_header, null);
		View footer = getLayoutInflater().inflate(R.layout.activity_user_order_detail_footer, null);
		
		ListView listView = (ListView) findViewById(R.id.manifest);
		
		listView.addHeaderView(header);
		listView.addFooterView(footer);
		
		listView.setAdapter(mSimpleAdapter);
	}
	
	private void updateDataSetAndViewLayout() {
		mDataSet.clear();
		
		TextView title = (TextView) findViewById(R.id.navigator_title);
		title.setText(UserOrderUtils.getOrderTypeText(mUserOrder));
		
		Button forward = (Button) findViewById(R.id.navigator_forward);
		if(mUserOrder.getStatus() == UserOrderConsts.ORDER_STATUS_SREVED) {
			forward.setBackgroundResource(R.drawable.icon_share);
		} else {
			forward.setBackgroundResource(R.drawable.icon_refresh);
		}

		Button payment = (Button) findViewById(R.id.order_payment);
		payment.setOnClickListener(this);
		if(mUserOrder.getStatus() == UserOrderConsts.ORDER_STATUS_SREVED) {
			payment.setText(R.string.order_status_text_served_text);
			payment.setClickable(false);
			payment.setBackgroundResource(0);
		} else {
			payment.setBackgroundResource(R.drawable.icon_alipay);
		}
		
		TextView date = (TextView) findViewById(R.id.user_order_date);
		date.setText(mUserOrder.getUpdatetime());
		
		List<UserOrderService> services = mUserOrder.getServices();
		
		double totalPrice = 0;
		for(UserOrderService service : services) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			
			item.put(UID_SERVICE_ITEM_TITLE , service.getName());
			item.put(UID_SERVICE_ITEM_AMOUNT, service.getAmount());
			item.put(UID_SERVICE_ITEM_PRICE , String.valueOf(service.getPrice()));
			
			totalPrice += service.getAmount() * service.getPrice();
			
			mDataSet.add(item);
		}
		
		TextView tvTotalPrice = (TextView) findViewById(R.id.total_price);
		tvTotalPrice.setText(String.valueOf(totalPrice));
		
		mSimpleAdapter.notifyDataSetChanged();
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
		case R.id.order_payment:
			onPaymentBtnClick();
			break;
		}
	}

	private void onPaymentBtnClick() {
		Payment payment = new Payment(this, mHandler);
		payment.pay(mUserOrder);
	}
	
	private void requestDataSource(String id) {
		InfoOrderOfNo request = new InfoOrderOfNo();
		
		request.setOrderno(id);
		
		ApiServer.getInstance(this).findOrderByNo(request, this);
	}

	private void onForwardBtnClick() {
		if(mUserOrder.getStatus() == UserOrderConsts.ORDER_STATUS_SREVED) {
			// TODO: Share & Comments
			startActivity(ActShare.getIntent(
					this,
					ApiServer.mShopInfo.id,
					mUserOrder.getId(),
					"http://pic14.nipic.com/20110512/5793673_203706569388_2.jpg",
					"二号服务员",
					"服务很好\n质量很好\n人品很好\n很细心"));
		} else {
			requestDataSource(mUserOrder.getOrderno());
		}
	}

	private void onBackwordBtnClick() {
		finish();
	}

	@Override
	public void success(ApiMethod api, String response) {
		if(api != ApiMethod.API_FIND_ORDER_BY_NO) { return; }
		RespOrderOfNo resp = ApiServer.getGson().fromJson(response, RespOrderOfNo.class);
		if(resp.getBody() != null) {
			mUserOrder = resp.getBody();
			updateDataSetAndViewLayout();
		}
	}

	@Override
	public void fail(ApiMethod api, VolleyError error) {
	}
	
	class PaymentHandler implements Handler.Callback {

		@Override
		public boolean handleMessage(Message msg) {
			Result result = new Result((String) msg.obj);
			switch (msg.what) {
			case Payment.REQUEST_PAYMENT_ALIPAY:
				String content = result.getResult();
				if(content != null && !(content.length() == 0)) {
					Toast.makeText(UserOrderDetail.this, result.getResult(), Toast.LENGTH_SHORT).show();
				}
				break;
			}
			return true;
		}
		
	}
}
