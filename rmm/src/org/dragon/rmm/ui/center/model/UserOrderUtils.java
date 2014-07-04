package org.dragon.rmm.ui.center.model;


import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.utils.StringResource;

public class UserOrderUtils {
	
	public static String toJson(UserOrder order) {
		return ApiServer.getGson().toJson(order);
	}
	
	public static UserOrder fromJson(String bundle) {
		return ApiServer.getGson().fromJson(bundle, UserOrder.class);
	}
	
	public static int getIconResourceId(UserOrder order) {
		int status = order.getStatus();
		String type = order.getOrdertype();
		
		if(UserOrderConsts.ORDER_TYPE_CLEANING.equals(type)) {
			return getIconOrderCleaningResourceId(status);
		} else if(UserOrderConsts.ORDER_TYPE_DRY_CLEANING.equals(type)) {
			return getIconOrderDryCleaningResourceId(status);
		} else if(UserOrderConsts.ORDER_TYPE_HOURLY_WORKER.equals(type)) {
			return getIconOrderHourlyWorkerResourceId(status);
		} else {
			throw new IllegalArgumentException("invalid order type : " + type);
		}
	}
	
	private static int getIconOrderCleaningResourceId(int status) {
		switch (status) {
		case UserOrderConsts.ORDER_STATUS_SREVED:
			return R.drawable.icon_order_cleaning_served;
		case UserOrderConsts.ORDER_STATUS_PENDING:
			return R.drawable.icon_order_cleaning_pending;
		case UserOrderConsts.ORDER_STATUS_SERVING:
			return R.drawable.icon_order_cleaning_serving;
		default:
			return R.drawable.icon_order_cleaning_serving;
		}
	}
	
	private static int getIconOrderDryCleaningResourceId(int status) {
		switch (status) {
		case UserOrderConsts.ORDER_STATUS_SREVED:
			return R.drawable.icon_order_dry_cleaning_served;
		case UserOrderConsts.ORDER_STATUS_PENDING:
			return R.drawable.icon_order_dry_cleaning_pending;
		case UserOrderConsts.ORDER_STATUS_SERVING:
			return R.drawable.icon_order_dry_cleaning_serving;
		default:
			return R.drawable.icon_order_dry_cleaning_serving;
		}
	}
	
	private static int getIconOrderHourlyWorkerResourceId(int status) {
		switch (status) {
		case UserOrderConsts.ORDER_STATUS_SREVED:
			return R.drawable.icon_order_hourly_worker_served;
		case UserOrderConsts.ORDER_STATUS_PENDING:
			return R.drawable.icon_order_hourly_worker_pending;
		case UserOrderConsts.ORDER_STATUS_SERVING:
			return R.drawable.icon_order_hourly_worker_serving;
		default:
			return R.drawable.icon_order_hourly_worker_serving;
		}
	}
	
	public static String getOrderType(UserOrder order) {
		if(UserOrderConsts.ORDER_TYPE_CLEANING.equals(order.getOrdertype())) {
			return UserOrderConsts.ORDER_TYPE_CLEANING;
		} else if(UserOrderConsts.ORDER_TYPE_DRY_CLEANING.equals(order.getOrdertype())) {
			return UserOrderConsts.ORDER_TYPE_DRY_CLEANING;
		} else if(UserOrderConsts.ORDER_TYPE_HOURLY_WORKER.equals(order.getOrdertype())) {
			return UserOrderConsts.ORDER_TYPE_HOURLY_WORKER;
		}
		return UserOrderConsts.ORDER_TYPE_UNKNOWN;
	}
	
	public static String getOrderIdFixedWidth(UserOrder order) {
		String prefix = order.getOrderno().substring(0, 9);
		String suffix = order.getOrderno().substring(order.getOrderno().length() - 11);
		return prefix + "..." + suffix;
	}
	
	public static String getOrderDateText(UserOrder order) {
		return order.getUpdatetime().split(" ")[0];
	}
	
	public static String getOrderTypeText(UserOrder order) {
		if(UserOrderConsts.ORDER_TYPE_CLEANING.equals(order.getOrdertype())) {
			return StringResource.getString(R.string.order_type_text_cleaning);
		} else if(UserOrderConsts.ORDER_TYPE_DRY_CLEANING.equals(order.getOrdertype())) {
			return StringResource.getString(R.string.order_type_text_dry_cleaning);
		} else if(UserOrderConsts.ORDER_TYPE_HOURLY_WORKER.equals(order.getOrdertype())) {
			return StringResource.getString(R.string.order_type_text_hourly_worker);
		}
		return StringResource.getString(R.string.order_type_text_unknown);
	}
	
	public static String getOrderStatusText(UserOrder order) {
		switch (order.getStatus()) {
		case UserOrderConsts.ORDER_STATUS_PENDING:
			return StringResource.getString(R.string.order_status_text_pending);
		case UserOrderConsts.ORDER_STATUS_UNASSIGNED:
			return StringResource.getString(R.string.order_status_text_unassigned);
		case UserOrderConsts.ORDER_STATUS_SERVING:
			return StringResource.getString(R.string.order_status_text_serving);
		case UserOrderConsts.ORDER_STATUS_SREVED:
			return StringResource.getString(R.string.order_status_text_served);
		case UserOrderConsts.ORDER_STATUS_CANCELLED:
			return StringResource.getString(R.string.order_status_text_cancelled);
		default:
			return StringResource.getString(R.string.order_status_text_pending);
		}
	}
}
