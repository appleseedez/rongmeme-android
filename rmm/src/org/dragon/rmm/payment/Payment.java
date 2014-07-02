package org.dragon.rmm.payment;

import java.net.URLEncoder;

import org.dragon.rmm.R;
import org.dragon.rmm.ui.center.model.UserOrder;
import org.dragon.rmm.utils.StringResource;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

//import com.alipay.android.app.sdk.AliPay;

public class Payment {
	
	public static final int REQUEST_PAYMENT_ALIPAY = 1;
	
	private Activity mContext;
	
	private Handler mHandler;
	
	public Payment(Activity context, Handler handler) {
		mContext = context;
		mHandler = handler;
	}
	
	public void pay(UserOrder order) {
		try {
			String orderInfo = getOrderInfoFromUserOrder(order);
			
			String sign = Rsa.sign(orderInfo, Keys.PRIVATE);
			sign = URLEncoder.encode(sign, "UTF-8");
			
			orderInfo += "&sign=\"" + sign + "\"&" + getSignType();
			System.out.println("[ALIPAY] order info : " + orderInfo);
			
			new Thread(new AlipayPayer(orderInfo)).start();
		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(mContext, R.string.payment_alipay_failture, Toast.LENGTH_SHORT).show();
		}
	}
	
	class AlipayPayer implements Runnable {

		private String mOrderInfo;
		
		public AlipayPayer(String orderInfo) {
			mOrderInfo = orderInfo;
		}
		
		@Override
		public void run() {
//			AliPay alipay = new AliPay(mContext, mHandler);
//			
////			alipay.setSandBox(true);
//
//			String result = alipay.pay(mOrderInfo);
//			System.out.println("[ALIPAY] result : " + result);
//			
//			Message msg = new Message();
//			msg.what = REQUEST_PAYMENT_ALIPAY;
//			msg.obj = result;
//			mHandler.sendMessage(msg);
		}
	}
	
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	private String getSubject(UserOrder order) {
		String orderType = order.getOrdertype();
		if(orderType.equals("bj")) {
			return StringResource.getString(R.string.payment_order_type_cleaning);
		} else if(orderType.equals("gx")) {
			return StringResource.getString(R.string.payment_order_type_dry_cleaning);
		} else if(orderType.equals("zd")) {
			return StringResource.getString(R.string.payment_order_type_hourly_worker);
		} else {
			throw new IllegalArgumentException("invalid order type : " + orderType);
		}
	}
	
	private String getBody(UserOrder order) {
		return StringResource.getString(R.string.payment_order_description);
	}

	@SuppressWarnings("deprecation")
	private String getOrderInfoFromUserOrder(UserOrder order) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		
		sb.append("\"&out_trade_no=\"");
		sb.append(order.getOrderno());
		
		sb.append("\"&subject=\"");
		sb.append(getSubject(order));
		
		sb.append("\"&body=\"");
		sb.append(getBody(order));
		
		sb.append("\"&total_fee=\"");
//		sb.append(order.getAllprice() / 10.0);
		sb.append(0.01);
		
		sb.append("\"&notify_url=\"");
		sb.append("http%3A%2F%2F218.244.130.240:8080/eclean/alipay/noticeEcleanServer.do");
//		sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		
		sb.append("\"&payment_type=\"1");
		
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

//		sb.append("\"&show_url=\"");

		sb.append("\"&it_b_pay=\"1m");
		
		sb.append("\"");

		return new String(sb);
	}
}