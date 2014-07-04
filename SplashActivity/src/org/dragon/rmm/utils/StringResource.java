package org.dragon.rmm.utils;

import android.content.Context;

public class StringResource {
	
	private static Context baseContext;
	
	public static void setContext(Context context) {
		baseContext = context;
	}
	
	public static String getString(int id) {
		return baseContext.getResources().getString(id);
	}
}
