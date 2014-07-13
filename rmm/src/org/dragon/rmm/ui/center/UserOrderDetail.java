package org.dragon.rmm.ui.center;

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
import org.dragon.rmm.ui.center.model.UserOrderServer;
import org.dragon.rmm.ui.center.model.UserOrderUtils;
import org.dragon.rmm.utils.PreferenceUtils;
import org.dragon.rmm.utils.StringResource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class UserOrderDetail extends Activity implements OnClickListener, ResponseListener {

	public static final String INTENT_EXTRA_USER_ORDER = "extra_user_order";

	public static final String INTENT_EXTRA_USER_ORDER_BARCODE_SCAN_MODE = "extra_user_order_barcode_scan_mode";

	public final String UID_SERVICE_ITEM_TITLE = "service_item_title";

	public final String UID_SERVICE_ITEM_AMOUNT = "service_item_amount";

	public final String UID_SERVICE_ITEM_PRICE = "service_item_price";

	private UserOrder mUserOrder;

	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_user_order_detail);

		initViewLayout();

		String bundle = getIntent().getStringExtra(INTENT_EXTRA_USER_ORDER);
		if (bundle != null) {
			mUserOrder = UserOrderUtils.fromJson(bundle);
			updateViewLayout();
		} else if(getIntent().getBooleanExtra(INTENT_EXTRA_USER_ORDER_BARCODE_SCAN_MODE, false)) {
			startBarcodeScanner();
		}

		mHandler = new Handler(new PaymentHandler());
	}

	private void initViewLayout() {
		TextView backward = (TextView) findViewById(R.id.navigator_backward);
		backward.setOnClickListener(this);

		TextView forward = (TextView) findViewById(R.id.navigator_forward);
		forward.setOnClickListener(this);
	}

	private void updateViewLayout() {
		
		if(!vaildateUserOrder()) {
			Toast.makeText(this, R.string.order_status_text_invalidate, Toast.LENGTH_SHORT).show(); finish();
		}

		TextView title = (TextView) findViewById(R.id.navigator_title);
		title.setText(UserOrderUtils.getOrderTypeText(mUserOrder));

		TextView forward = (TextView) findViewById(R.id.navigator_forward);
		if (mUserOrder.getStatus() == UserOrderConsts.ORDER_STATUS_SREVED) {
			forward.setBackgroundResource(R.drawable.icon_share);
		} else {
			forward.setBackgroundResource(R.drawable.icon_refresh);
		}

		TextView notify = (TextView) findViewById(R.id.notify_serve_done);
		TextView payment = (TextView) findViewById(R.id.order_payment);

		if (mUserOrder.getStatus() == UserOrderConsts.ORDER_STATUS_SREVED) {
			notify.setText(R.string.order_status_text_served_text);
			notify.setVisibility(View.VISIBLE);
			payment.setVisibility(View.INVISIBLE);
		} else {
			notify.setVisibility(View.INVISIBLE);
			payment.setText(R.string.payment_alipay_label);
			payment.setOnClickListener(this);
			payment.setVisibility(View.VISIBLE);
		}

		TextView totalPrice = (TextView) findViewById(R.id.order_total_price);
		totalPrice.setText("￥ " + mUserOrder.getAllprice());
		
		TextView status = (TextView) findViewById(R.id.order_status);
		status.setText(UserOrderUtils.getOrderTypeText(mUserOrder) + UserOrderUtils.getOrderStatusText(mUserOrder));
		
		TextView orderDate = (TextView) findViewById(R.id.order_date);
		orderDate.setText(mUserOrder.getUpdatetime());
		
		TextView userAddress = (TextView) findViewById(R.id.user_address);
		userAddress.setText(PreferenceUtils.getUser(this).address);
		
		TextView userTelephone = (TextView) findViewById(R.id.user_telephone);
		userTelephone.setText(PreferenceUtils.getUser(this).username);
		
		updateOrderServerViewLayout();
	}
	
	private void updateOrderServerViewLayout() {
		List<UserOrderServer> servers = mUserOrder.getServers();
		if(servers.size() >= 1) {
			setServerViewLayout(R.id.order_server_a, servers.get(0));
		}
		if(servers.size() >= 2) {
			setServerViewLayout(R.id.order_server_b, servers.get(1));
		}
		if(servers.size() >= 3) {
			setServerViewLayout(R.id.order_server_c, servers.get(2));
		}
		if(servers.size() >= 4) {
			setServerViewLayout(R.id.order_server_d, servers.get(3));
		}
	}
	
	private void setServerViewLayout(int resId, UserOrderServer server) {
		TextView view = (TextView) findViewById(resId);
		view.setText(server.getName());
		NetworkImageView imageView = new NetworkImageView(this);
		imageView.setImageUrl(server.getAvatar(), ApiServer.getImageLoader(this));
	}
	
	private void startBarcodeScanner() {
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
		integrator.setResultDisplayDuration(0); integrator.setCameraId(0);
		integrator.initiateScan(); 
	}

	private boolean vaildateUserOrder() {
		if((UserOrderUtils.getOrderType(mUserOrder) == UserOrderConsts.ORDER_TYPE_UNKNOWN)
			|| mUserOrder.getServices().isEmpty()) {
			return false;
		}
		return true;
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
		
		request.setOrderno(id.split("=")[1]);

		ApiServer.getInstance(this).findOrderByNo(request, this);
	}

	private void onForwardBtnClick() {
		if (mUserOrder.getStatus() == UserOrderConsts.ORDER_STATUS_SREVED) {
			// FIXME: 1. how to make comments if severs of specific order don't
			// exist ?
			// 2. is it still to display the first one in server list ?
			List<UserOrderServer> servers = mUserOrder.getServers();
			if (servers != null && !servers.isEmpty()) {
				UserOrderServer server = servers.get(0);
				startActivity(ActShare.getIntent(this, PreferenceUtils.getShop(this).id, mUserOrder.getId(), server.getAvatar(), server.getName(),
						StringResource.getString(R.string.order_service_comments_list)));
			}
		} else {
			requestDataSource(mUserOrder.getOrderno());
		}
	}

	private void onBackwordBtnClick() {
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK) {
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			if (result == null) { super.onActivityResult(requestCode, resultCode, data); return; }
			if (result.getContents() != null) {
				requestDataSource(result.getContents());
			}
		} else if(resultCode == Activity.RESULT_CANCELED) {
			finish();
		}
	}

	@Override
	public void success(ApiMethod api, String response) {
		if (api != ApiMethod.API_FIND_ORDER_BY_NO) {
			return;
		}
		RespOrderOfNo resp = ApiServer.getGson().fromJson(response, RespOrderOfNo.class);
		if (resp.getBody() != null) {
			mUserOrder = resp.getBody();
			updateViewLayout();
		}
	}

	@Override
	public void fail(ApiMethod api, VolleyError error) {
		Toast.makeText(this, R.string.order_status_text_invalidate, Toast.LENGTH_LONG).show();
		finish();
	}

	class PaymentHandler implements Handler.Callback {

		@Override
		public boolean handleMessage(Message msg) {
			Result result = new Result((String) msg.obj);
			switch (msg.what) {
			case Payment.REQUEST_PAYMENT_ALIPAY:
				String content = result.getResult();
				if (content != null && !(content.length() == 0)) {
					Toast.makeText(UserOrderDetail.this, result.getResult(), Toast.LENGTH_SHORT).show();
				}
				break;
			}
			return true;
		}

	}
}
