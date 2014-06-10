package org.dragon.rmm.api;

import java.io.UnsupportedEncodingException;

import org.dragon.rmm.model.BaseModel;
import org.dragon.rmm.model.InfoHeader;
import org.dragon.rmm.model.InfoRegist;
import org.dragon.rmm.model.InfoStore;
import org.dragon.rmm.model.InfoUserLogin;
import org.dragon.rmm.model.InfoUserLogout;
import org.dragon.rmm.model.InfoVerycode;
import org.dragon.rmm.model.ModelResUser;
import org.dragon.rmm.volley.PostRequest;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ApiServer {

	public static final boolean DEBUG = true;
	public static final String API_HOST = "http://218.244.130.240:8080";
	public static final int PAGE = 1;
	public static final int PSIZE = 20;
	private static ApiServer mInstance;
	private RequestQueue mQueue;
	private InfoHeader mHeader;
	private Gson mGson;

	private ApiServer(Context context) {
		mQueue = Volley.newRequestQueue(context);
		mHeader = new InfoHeader();
		mGson = new Gson();
	}

	public static ApiServer getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new ApiServer(context);

		}
		return mInstance;
	}

	private <T> void request(final ApiMethod apiMethod, T postJson, final ResponseListener reponseListener, boolean forceRefresh) {
		if (null == reponseListener) {
			return;
		}
		String url = null;
		try {
			url = URLBuilder.getURLBuilder(API_HOST, apiMethod.getValue(), null);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (null == url) {
			return;
		}
		ErrorListener error = new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				reponseListener.fail(apiMethod, error);
			}
		};
		Listener<String> response = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				switch (apiMethod) {
				case API_LOGIN:
					mHeader.sessionToken = mGson.fromJson(response, ModelResUser.class).head.sessionToken;
					break;
				case API_LOGOUT:
					mHeader.sessionToken = null;
					break;
				}
				reponseListener.success(apiMethod, response);
			}
		};

		BaseModel<T> model = new BaseModel<T>(postJson, mHeader);
		PostRequest request = new PostRequest(url, new Gson().toJson(model), response, error);
		request.setShouldCache(true);
		if (ApiServer.DEBUG) {
			System.out.println("<<<Request URL>>>" + request.toString());
		}
		request.setTag(url);
		if (forceRefresh) {
			mQueue.getCache().remove(request.getCacheKey());
		}
		mQueue.add(request);
	}

	// *************************
	// ***** 客户端可以使用的 API*****
	// *************************

	/**
	 * 登录
	 * 
	 * @param info
	 * @param reponseListener
	 */
	public void login(InfoUserLogin info, ResponseListener reponseListener) {
		request(ApiMethod.API_LOGIN, info, reponseListener, true);
	}

	/**
	 * 注销
	 * 
	 * @param info
	 * @param reponseListener
	 */
	public void logout(InfoUserLogout info, ResponseListener reponseListener) {
		request(ApiMethod.API_LOGOUT, info, reponseListener, true);
	}

	/**
	 * 注册验证码
	 * 
	 * @param info
	 * @param reponseListener
	 */
	public void verifyCode(InfoVerycode info, ResponseListener reponseListener) {
		request(ApiMethod.API_REGIST_VERIFYCODE, info, reponseListener, true);
	}

	/**
	 * 注册
	 * 
	 * @param info
	 * @param reponseListener
	 */
	public void regist(InfoRegist info, ResponseListener reponseListener) {
		request(ApiMethod.API_REGIST, info, reponseListener, true);
	}

	/**
	 * 门店详情
	 * 
	 * @param info
	 * @param reponseListener
	 */
	public void shopInfo(InfoStore info, ResponseListener reponseListener) {
		request(ApiMethod.API_SHOPINFO, info, reponseListener, true);
	}
}
