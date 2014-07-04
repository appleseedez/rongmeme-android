package org.dragon.rmm.ui.center;

import java.util.HashMap;
import java.util.List;


import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.model.InfoOrderOfUser;
import org.dragon.rmm.model.RespOrderOfUser;
import org.dragon.rmm.ui.center.model.UserOrder;
import org.dragon.rmm.ui.center.model.UserOrderUtils;
import org.dragon.rmm.ui.widget.ProgreadListActivity;
import org.dragon.rmm.utils.PreferenceUtils;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserCenterPortal extends ProgreadListActivity<UserOrder> {
	
	public final String UID_ORDER_ICON     = "order_icon";
	
	public final String UID_ORDER_TITLE    = "order_title";
	
	public final String UID_ORDER_ID       = "order_id";
	
	public final String UID_ORDER_DATE     = "order_date";
	
	public final String UID_ORDER_STATUS   = "order_status";
	
	public UserCenterPortal() {
		super(R.layout.activity_user_center_portal, R.layout.activity_user_center_portal_item);
		
		String[] fields = { UID_ORDER_ICON, UID_ORDER_TITLE, UID_ORDER_ID, UID_ORDER_DATE, UID_ORDER_STATUS};
		int[] elements = { R.id.order_icon, R.id.order_title, R.id.order_id, R.id.order_date, R.id.order_status };
		
		setFields(fields);
		setElements(elements);
		
		setTitleLabel(R.string.activity_label_user_center_portal);
		setForwardIcon(R.drawable.icon_config);
	}

	@Override
	protected void updateDataSet(UserOrder data, HashMap<String, Object> mapping) {
		mapping.put(UID_ORDER_ICON   , UserOrderUtils.getIconResourceId(data));
		mapping.put(UID_ORDER_TITLE  , UserOrderUtils.getOrderTypeText(data));
		mapping.put(UID_ORDER_ID     , UserOrderUtils.getOrderIdFixedWidth(data));
		mapping.put(UID_ORDER_DATE   , UserOrderUtils.getOrderDateText(data));
		mapping.put(UID_ORDER_STATUS , UserOrderUtils.getOrderStatusText(data));
	}

	@Override
	protected void requestDataSource() {
		InfoOrderOfUser req = new InfoOrderOfUser();
		
		req.setUserId(PreferenceUtils.getUser(this).userid);
		req.setUsername(PreferenceUtils.getUser(this).username);
		
		ApiServer.getInstance(this).loadOrdersOfUser(req , this);
	}

	@Override
	protected void updateDataSource(ApiMethod which, String bundle, List<UserOrder> dataSource) {
		if(which != ApiMethod.API_LOAD_ORDERS_OF_USER) { return; }
		RespOrderOfUser resp = ApiServer.getGson().fromJson(bundle, RespOrderOfUser.class);
		dataSource.addAll(resp.getBody());
	}

	@Override
	protected boolean updateViewValue(int id, View view, Object data) {
		switch (id) {
		case R.id.order_icon:
			((ImageView) view).setImageResource((Integer)data);
			break;
		case R.id.order_title:
			((TextView) view).setText((String) data);
			break;
		case R.id.order_id:
			((TextView) view).setText((String) data);
			break;
		case R.id.order_date:
			((TextView) view).setText((String) data);
			break;
		case R.id.order_status:
			((TextView) view).setText((String) data);
			break;
		}
		return true;
	}

	@Override
	protected void onForwardBtnClick() {
		startActivity(new Intent(this, UserProfile.class));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, UserOrderDetail.class);
		intent.putExtra(UserOrderDetail.INTENT_EXTRA_USER_ORDER, UserOrderUtils.toJson(getData(id)));
		startActivity(intent);
	}
	
}
