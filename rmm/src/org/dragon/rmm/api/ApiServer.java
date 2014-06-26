package org.dragon.rmm.api;

import java.io.UnsupportedEncodingException;

import org.dragon.rmm.model.BaseModel;
import org.dragon.rmm.model.InfoComment;
import org.dragon.rmm.model.InfoCommentList;
import org.dragon.rmm.model.InfoDryCleanReservation;
import org.dragon.rmm.model.InfoDryCleanServices;
import org.dragon.rmm.model.InfoEditUserInfo;
import org.dragon.rmm.model.InfoHeader;
import org.dragon.rmm.model.InfoOrderOfNo;
import org.dragon.rmm.model.InfoOrderOfUser;
import org.dragon.rmm.model.InfoPlace;
import org.dragon.rmm.model.InfoRegist;
import org.dragon.rmm.model.InfoShop;
import org.dragon.rmm.model.InfoUserLogin;
import org.dragon.rmm.model.InfoUserLogout;
import org.dragon.rmm.model.InfoVerycode;
import org.dragon.rmm.model.ModelResUser;
import org.dragon.rmm.model.ResShop;
import org.dragon.rmm.model.ResUser;
import org.dragon.rmm.utils.PreferenceUtils;
import org.dragon.rmm.volley.BitmapLruCache;
import org.dragon.rmm.volley.PostRequest;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ApiServer {

	public static final boolean DEBUG = true;
	public static final String API_HOST = "http://218.244.130.240:8080";
	public static final int PSIZE = 100;
	private static ApiServer mInstance;
	private RequestQueue mQueue;
	private InfoHeader mHeader;
	public static ResUser mUser;
	public static ResShop mShopInfo;
	private static ImageLoader mImageLoader;
	private static Gson mGson;
	private Context mContext;

	private ApiServer(Context context) {
		mQueue = Volley.newRequestQueue(context);
		mHeader = new InfoHeader();
		mContext = context;
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

	public static ImageLoader getImageLoader(Context context) {
		if (null == mImageLoader) {
			mImageLoader = new ImageLoader(Volley.newRequestQueue(context), new BitmapLruCache(10 * 1024 * 1024, context.getFilesDir()));
		}
		return mImageLoader;
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
					PreferenceUtils.saveUser(mContext, mUser);
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
	 * 周边门店列表
	 * 
	 * @param info
	 * @param reponseListener
	 */
	public void shopList(InfoPlace info, ResponseListener reponseListener) {
		request(ApiMethod.API_SHOPLIST, info, reponseListener, true);
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

	/**
	 * 查看我的所有订单
	 * 
	 * @param info
	 *            request parameters
	 * @param listener
	 *            callback invoked when response has arrived
	 */
	public void loadOrdersOfUser(InfoOrderOfUser info, ResponseListener listener) {
		request(ApiMethod.API_LOAD_ORDERS_OF_USER, info, listener, true);
	}

	/**
	 * 查询订单
	 * 
	 * @param info
	 *            request parameters
	 * @param listener
	 *            callback invoked when response has arrived
	 */
	public void findOrderByNo(InfoOrderOfNo info, ResponseListener listener) {
		request(ApiMethod.API_FIND_ORDER_BY_NO, info, listener, true);
	}

	/**
	 * 编辑常用地址信息
	 * 
	 * @param info
	 *            request parameters
	 * @param listener
	 *            callback invoked when response has arrived
	 */
	public void editUserInfo(InfoEditUserInfo info, ResponseListener listener) {
		request(ApiMethod.API_EDIT_USER_INFO, info, listener, true);
	}

	/**
	 * 加载干洗信息
	 * 
	 * @param info
	 *            request parameters
	 * @param listener
	 *            callback invoked when response has arrived
	 */
	public void loadDryCleanServices(InfoDryCleanServices info, ResponseListener listener) {
		request(ApiMethod.API_LOAD_DRY_CLEAN_SERVICES, info, listener, true);
	}

	/**
	 * 生成干洗预约
	 * 
	 * @param info
	 *            request parameters
	 * @param listener
	 *            callback invoked when response has arrived
	 */
	public void createDryCleanReservation(InfoDryCleanReservation info, ResponseListener listener) {
		request(ApiMethod.API_CREATE_DRY_CLEAN_APPOINTENT, info, listener, true);
	}
}
