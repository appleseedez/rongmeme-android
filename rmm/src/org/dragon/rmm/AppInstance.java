package org.dragon.rmm;

import org.dragon.rmm.utils.StringResource;

import android.app.Application;

public class AppInstance extends Application {
	
	@Override
	public void onCreate() {
		StringResource.setContext(getBaseContext());
	}

	@Override
	public void onTerminate() {
		StringResource.setContext(null);
	}

}
