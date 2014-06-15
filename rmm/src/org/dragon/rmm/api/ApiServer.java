package org.dragon.rmm.api;

import java.io.UnsupportedEncodingException;

import org.dragon.rmm.model.BaseModel;
import org.dragon.rmm.model.InfoComment;
import org.dragon.rmm.model.InfoCommentList;
import org.dragon.rmm.model.InfoHeader;
import org.dragon.rmm.model.InfoRegist;
import org.dragon.rmm.model.InfoShop;
import org.dragon.rmm.model.InfoUserLogin;
import org.dragon.rmm.model.InfoUserLogout;
import org.dragon.rmm.model.InfoVerycode;
import org.dragon.rmm.model.ModelResUser;
import org.dragon.rmm.model.ResUser;
import org.dragon.rmm.volley.PostRequest;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ApiServer {

	public static final boolean DEBUG = true;
	public static final String API_HOST = "http://218.244.130.240:8080";
	public static final int PAGE = 1;
	public static final int PSIZE = 100;
	private static ApiServer mInstance;
	private RequestQueue mQueue;
	private InfoHeader mHeader;
	public static ResUser mUser;
	public static ImageLoader mImageLoader;
	private static Gson mGson;

	private ApiServer(Context context) {
		mQueue = Volley.newRequestQueue(context);
		mHeader = new InfoHeader();
		ImageCache imageCache = new ImageCache() {

			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				// TODO Auto-generated method stub

			}

			@Override
			public Bitmap getBitmap(String url) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		mImageLoader = new ImageLoader(Volley.newRequestQueue(context), imageCache);
	}

	public static ApiServer getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new ApiServer(context);

		}
		return mInstance;
	}

	public static Gson getGson() {
		if (null == mGson) {
			mGson = new Gson();
		}
		return mGson;
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
				if (ApiServer.DEBUG) {
					System.out.println("<<<<Error>>>>" + apiMethod.getValue() + ":" + error.getMessage());
				}
				switch (apiMethod) {
				case API_LOGIN:
					mHeader.sessionToken = "";
					break;
				}
				reponseListener.fail(apiMethod, error);
			}
		};
		Listener<String> response = new Listener<String>() {

			@Override
			public void onResponse(String response) {
				if (DEBUG) {
					System.out.println("<<<<Response>>>>" + apiMethod.getValue() + ":" + response);
				}
				switch (apiMethod) {
				case API_LOGIN:
					ModelResUser result = ApiServer.getGson().fromJson(response, ModelResUser.class);
					mHeader.sessionToken = result.head.sessionToken;
					mUser = result.body;
					break;
				case API_LOGOUT:
					mHeader.sessionToken = null;
					break;
				}
				reponseListener.success(apiMethod, response);
			}
		};

		BaseModel<T> model = new BaseModel<T>(postJson, mHeader);
		PostRequest request = new PostRequest(url, ApiServer.getGson().toJson(model), response, error);
		request.setShouldCache(true);
		if (ApiServer.DEBUG) {
			System.out.println("<<<<Request URL>>>>" + request.toString());
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
	public void shopInfo(InfoShop info, ResponseListener reponseListener) {
		request(ApiMethod.API_SHOPINFO, info, reponseListener, true);
	}

	/**
	 * 获取评论列表
	 * 
	 * @param info
	 * @param reponseListener
	 */
	public void commentList(InfoCommentList info, ResponseListener reponseListener) {
		request(ApiMethod.API_COMMENT_LIST, info, reponseListener, true);
	}

	/**
	 * 评论
	 * 
	 * @param info
	 * @param reponseListener
	 */
	public void comment(InfoComment info, ResponseListener reponseListener) {
		request(ApiMethod.API_COMMENT, info, reponseListener, true);
	}
}
