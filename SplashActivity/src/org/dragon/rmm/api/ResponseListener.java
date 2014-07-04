package org.dragon.rmm.api;

import com.android.volley.VolleyError;

public interface ResponseListener {

	@SuppressWarnings("rawtypes")
	public void success(ApiMethod api, String response);

	public void fail(ApiMethod api, VolleyError error);
}
