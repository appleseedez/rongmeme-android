package org.dragon.rmm.ui.drycleaning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.dragon.rmm.R;
import org.dragon.rmm.api.ApiMethod;
import org.dragon.rmm.api.ApiServer;
import org.dragon.rmm.model.InfoDryCleanServices;
import org.dragon.rmm.model.RespDryCleanServices;
import org.dragon.rmm.ui.drycleaning.model.DryCleaningReservedItem;
import org.dragon.rmm.ui.drycleaning.model.DryCleaningReservedList;
import org.dragon.rmm.ui.drycleaning.model.DryCleaningService;
import org.dragon.rmm.ui.drycleaning.model.DryCleaningUtils;
import org.dragon.rmm.ui.widget.ProgreadListActivity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DryCleaningPortal extends ProgreadListActivity<DryCleaningService> {
	
	public final String UID_SERVICE_TITLE         = "title";
	
	public final String UID_SERVICE_PRICE         = "price";
	
	public final String UID_SERVICE_SUBTOTAL      = "subtotal";
	
	public final String UID_SERVICE_AMOUNT        = "amount";
	
	public final String UID_SERVICE_ADDITION      = "addition";
	
	public final String UID_SERVICE_SUBSTRACTION  = "substraction";
	
	private final int LIMITS_SERVICES_MAXIMUM    = 100;
	
	private final String SERVICE_PREFIX           = "service-";
	
	private Map<String, DryCleaningReservedItem> mReservedItems;
	
	public static final String INTENT_EXTRA_RESERVED_LIST = "extra_reserved_list";
	
	public DryCleaningPortal() {
		super(R.layout.activity_dry_cleaning_portal, R.layout.activity_dry_cleaning_portal_item);
		
		String[] fields = { UID_SERVICE_TITLE, UID_SERVICE_PRICE, UID_SERVICE_SUBTOTAL,
								UID_SERVICE_AMOUNT, UID_SERVICE_ADDITION, UID_SERVICE_SUBSTRACTION };
		
		int[] elements = { R.id.title, R.id.price, R.id.subtotal, R.id.amount, R.id.addition, R.id.subtraction };
		
		setFields(fields);
		setElements(elements);
		
		setTitleLabel(R.string.dry_cleaning_navigator_title);
		
		setForwardIcon(R.drawable.icon_share);
		
		mReservedItems = new HashMap<String, DryCleaningReservedItem>();
		mReservedItems.clear();
	}
	
	private String getPriceText(DryCleaningService data) {
		return "￥" + data.getPrice() + "/" + data.getUnit();
	}
	
	private String getSubtotalText(DryCleaningService data) {
		return "￥" + data.getPrice() * getAmountSafe(data.getId());
	}

	@Override
	protected void updateDataSet(DryCleaningService data, HashMap<String, Object> mapping) {
		mapping.put(UID_SERVICE_TITLE        , data.getName());
		mapping.put(UID_SERVICE_PRICE        , getPriceText(data));
		mapping.put(UID_SERVICE_SUBTOTAL     , getSubtotalText(data));
		mapping.put(UID_SERVICE_AMOUNT       , getAmountSafe(data.getId()));
		mapping.put(UID_SERVICE_ADDITION     , data);
		mapping.put(UID_SERVICE_SUBSTRACTION , data);
	}

	@Override
	protected void requestDataSource() {
		InfoDryCleanServices request = new InfoDryCleanServices();
		
		request.setStart(0);
		request.setLimit(LIMITS_SERVICES_MAXIMUM);
		
		ApiServer.getInstance(this).loadDryCleanServices(request, this);
	}

	@Override
	protected void updateDataSource(ApiMethod which, String bundle, List<DryCleaningService> dataSource) {
		if(which != ApiMethod.API_LOAD_DRY_CLEAN_SERVICES) { return; }
		RespDryCleanServices resp = ApiServer.getGson().fromJson(bundle, RespDryCleanServices.class);
		dataSource.addAll(resp.getBody());
		System.out.println("---");
	}

	@Override
	protected boolean updateViewValue(int id, View view, Object data) {
		switch (id) {
//			((NetworkImageView)view).setImageUrl((String) data, ApiServer.getImageLoader(this));
		case R.id.title:
			((TextView) view).setText((String) data);
			break;
		case R.id.price:
			((TextView) view).setText((String) data);
			break;
		case R.id.subtotal:
			((TextView) view).setText((String) data);
			break;
		case R.id.amount:
			((TextView) view).setText(((Integer)data).toString());
			break;
		case R.id.addition:
		case R.id.subtraction:
			view.setTag(data);
			view.setOnClickListener(this);
		}
		return true;
	}

	@Override
	protected void onForwardBtnClick() {
		
		if(mReservedItems.isEmpty()) {
			Toast.makeText(this, R.string.dry_cleaning_reserve_notification, Toast.LENGTH_SHORT).show(); return;
		}
		
		Intent intent = new Intent(this, DryCleaningReservation.class);
		
		List<DryCleaningReservedItem> items = new ArrayList<DryCleaningReservedItem>();
		items.addAll(mReservedItems.values());
		
		DryCleaningReservedList list = new DryCleaningReservedList();
		list.setReservedItems(items);
		
		intent.putExtra(INTENT_EXTRA_RESERVED_LIST, DryCleaningUtils.toJson(list));
		
		startActivity(intent);
	}

	@Override
	protected void onClick(int id, View view) {
		switch (id) {
		case R.id.addition:
			onAdditionBtnClick((DryCleaningService)view.getTag());
			break;
		case R.id.subtraction:
			onSubstactionBtnClick((DryCleaningService)view.getTag());
			break;
		}
	}
	
	private String key(int id) {
		return SERVICE_PREFIX + id;
	}
	
	private int getAmountSafe(int id) {
		DryCleaningReservedItem item = mReservedItems.get(key(id));
		if(item == null) { return 0; }
		return item.getAmount();
	}

	private void onSubstactionBtnClick(DryCleaningService service) {
		DryCleaningReservedItem item = mReservedItems.get(key(service.getId()));
		if(item != null) {
			int orginal = item.getAmount();
			if(--orginal <= 0) {
				mReservedItems.remove(key(service.getId()));
			} else {
				item.setAmount(orginal);
			}
		}
		updateViewLayout();
	}

	private void onAdditionBtnClick(DryCleaningService service) {
		DryCleaningReservedItem item = mReservedItems.get(key(service.getId()));
		if(item == null) {
			item = new DryCleaningReservedItem();
			
			item.setItemid(service.getId());
			item.setName(service.getName());
			item.setPrice(service.getPrice());
			
			mReservedItems.put(key(service.getId()), item);
		}
		
		int orginal = item.getAmount();
		item.setAmount(++orginal);
		updateViewLayout();
	}
	
}
