package org.dragon.rmm.utils;

import org.dragon.rmm.model.ResShop;
import org.dragon.rmm.model.ResUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtils {

	// 首选项存储sessionToken的key:curSessionToken.存储当前用户Id是:curUserId.存储当前用户名字是:curUserName.存储当前用户电话为：curUserPhone.
	// 存储当前用户地址为：curUserAddress(这个可能是空的，是在个人设置里面设置但是有些请求时需要的).
	// 存储当前门店ID:curStoreId.存储当前门店Name:curStoreName.

	public static final String PREFERENCE = "preference";
	public static final String PREFERENCE_USERID = "curUserId";
	public static final String PREFERENCE_USERNAME = "curUserName";
	public static final String PREFERENCE_USERPHONE = "curUserPhone";
	public static final String PREFERENCE_USERPWD = "preferenceUserPwd";
	public static final String PREFERENCE_USERADDR = "curUserAddress";

	public static final String PREFERENCE_SHOPID = "curStoreId";
	public static final String PREFERENCE_SHOPNAME = "curStoreName";
	public static final String PREFERENCE_SHOPADDR = "curAddress";
	public static final String EXTRA_LONGITUDE = "longitude";
	public static final String EXTRA_LATITUDE = "latitude";

	public static void saveUser(Context context, ResUser user) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_WORLD_WRITEABLE);
		Editor edit = sharedPreferences.edit();
		edit.putInt(PREFERENCE_USERID, user.userid);
		edit.putString(PREFERENCE_USERNAME, user.username);
		// 服务器返回的对象没有密码，所以不保存密码，在登录界面进行保存。
		edit.putString(PREFERENCE_USERPHONE, user.username);
		edit.putString(PREFERENCE_USERADDR, user.address);
		edit.commit();
	}

	public static ResUser getUser(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_WORLD_READABLE);
		int userid = sharedPreferences.getInt(PREFERENCE_USERID, -1);
		if (-1 == userid) {
			return null;
		}
		String name = sharedPreferences.getString(PREFERENCE_USERNAME, null);
		String password = sharedPreferences.getString(PREFERENCE_USERPWD, null);
		String address = sharedPreferences.getString(PREFERENCE_USERADDR, null);
		return new ResUser(name, password, userid, "", address);
	}

	public static void save(Context context, String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_WORLD_WRITEABLE);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static ResShop getShop(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_WORLD_READABLE);
		long shopid = sharedPreferences.getLong(PREFERENCE_SHOPID, -1);
		if (-1 == shopid) {
			return null;
		}
		String name = sharedPreferences.getString(PREFERENCE_SHOPNAME, null);
		String address = sharedPreferences.getString(PREFERENCE_SHOPADDR, null);
		ResShop shop = new ResShop();
		shop.id = shopid;
		shop.name = name;
		shop.address = address;
		return shop;
	}

}
