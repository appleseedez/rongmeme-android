package org.dragon.rmm.utils;

import org.dragon.rmm.model.ResUser;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceUtils {

	public static final String PREFERENCE = "preference";
	public static final String PREFERENCE_USERID = "preferenceUserId";
	public static final String PREFERENCE_USERNAME = "preferenceUserName";
	public static final String PREFERENCE_USERPWD = "preferenceUserPwd";
	public static final String PREFERENCE_USERNICKNAME = "preferenceUserNickname";
	public static final String PREFERENCE_USERADDR = "preferenceUserAddress";

	public static void saveUser(Context context, ResUser user) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_WORLD_WRITEABLE);
		Editor edit = sharedPreferences.edit();
		edit.putInt(PREFERENCE_USERID, user.userid);
		edit.putString(PREFERENCE_USERNAME, user.username);
		// 服务器返回的对象没有密码，所以不保存密码，在登录界面进行保存。
		edit.putString(PREFERENCE_USERNICKNAME, user.nickname);
		edit.putString(PREFERENCE_USERADDR, user.address);
		edit.commit();
	}

	public static ResUser getUser(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_WORLD_READABLE);
		int userid = sharedPreferences.getInt(PREFERENCE_USERID, -1);
		if (-1 == userid) {
			return null;
		}
		String username = sharedPreferences.getString(PREFERENCE_USERNAME, null);
		String password = sharedPreferences.getString(PREFERENCE_USERPWD, null);
		String nickname = sharedPreferences.getString(PREFERENCE_USERNICKNAME, null);
		String address = sharedPreferences.getString(PREFERENCE_USERADDR, null);
		return new ResUser(username, password, userid, nickname, address);
	}

	public static void save(Context context, String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_WORLD_WRITEABLE);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

}
