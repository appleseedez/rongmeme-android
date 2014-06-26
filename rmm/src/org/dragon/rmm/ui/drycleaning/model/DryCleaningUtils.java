package org.dragon.rmm.ui.drycleaning.model;

import org.dragon.rmm.api.ApiServer;

public class DryCleaningUtils {
	
	public static String toJson(DryCleaningReservedList list) {
		return ApiServer.getGson().toJson(list);
	}
	
	public static DryCleaningReservedList fromJson(String bundle) {
		return ApiServer.getGson().fromJson(bundle, DryCleaningReservedList.class);
	}
}
