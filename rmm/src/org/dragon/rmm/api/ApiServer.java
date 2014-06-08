package org.dragon.rmm.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.dragon.rmm.model.BaseModel;
import org.dragon.rmm.model.InfoHeader;
import org.dragon.rmm.model.InfoUser;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ApiServer {

	public static final boolean DEBUG = true;
	public static final String API_HOST = "http://218.244.130.240:8080";
	public static final int PAGE = 1;
	public static final int PSIZE = 20;
	private static ApiServer mInstance;
	private RequestQueue mQueue;

	private ApiServer(Context context) {
		mQueue = Volley.newRequestQueue(context);
	}

	public static ApiServer getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new ApiServer(context);
		}
		return mInstance;
	}

	private void request(final ApiMethod apiMethod, Map<String, String> getParams, HashMap<String, String> postParams, final ResponseListener reponseListener, boolean refresh) {
		if (null == reponseListener) {
			return;
		}
		String url = null;
		try {
			url = URLBuilder.getURLBuilder(API_HOST, apiMethod.getValue(), getParams);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (null == url) {
			return;
		}
		final HashMap<String, String> post = postParams;

		ErrorListener error = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				reponseListener.fail(apiMethod, error);
			}
		};
		Listener<String> response = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				reponseListener.success(apiMethod, response);
			}
		};
		StringRequest request = new StringRequest(null == postParams ? Method.GET : Method.DEPRECATED_GET_OR_POST, url, response, error) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return post;
			}
		};
		request.setShouldCache(true);
		if (ApiServer.DEBUG) {
			System.out.println("\n\n-------------------request url before----------------------\n" + url + "\n-------------------request url after ----------------------\n\n");
		}
		request.setTag(url);
		if (refresh) {
			mQueue.getCache().remove(request.getCacheKey());
		}
		mQueue.add(request);
	}

	private void request(final ApiMethod apiMethod, Map<String, String> getParams, HashMap<String, String> postParams, final ResponseListener reponseListener) {
		request(apiMethod, getParams, postParams, reponseListener, false);
	}

	public void get(ApiMethod apiMethod, HashMap<String, String> getParams, ResponseListener reponseListener) {
		request(apiMethod, getParams, null, reponseListener);
	}

	public void post(ApiMethod apiMethod, HashMap<String, String> postParams, ResponseListener reponseListener) {
		request(apiMethod, null, postParams, reponseListener);
	}

	// *************************
	// ***** 客户端可以使用的 API*****
	// *************************

	public void login(InfoUser info, ResponseListener reponseListener) {
		BaseModel<InfoUser> model = new BaseModel<InfoUser>(info, new InfoHeader());
		HashMap<String, String> postParams = new HashMap<String, String>(1);
		postParams.put("no_parameters", new Gson().toJson(model));
		post(ApiMethod.API_LOGIN, postParams, reponseListener);
	}

}
