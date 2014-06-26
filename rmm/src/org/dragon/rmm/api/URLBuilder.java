package org.dragon.rmm.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.text.TextUtils;

public class URLBuilder {

	public static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

	public static String getURLBuilder(String host, String subPath,
			Map<String, String> getParams) throws NullPointerException,
			UnsupportedEncodingException {
		if (TextUtils.isEmpty(host) || TextUtils.isEmpty(subPath)) {
			throw new NullPointerException(
					"parameter 'host' and 'subPath' can't be null !");
		}
		StringBuilder sb = null;
		if (host.startsWith("http") || host.startsWith("https")) {
			sb = new StringBuilder(host).append(subPath);
		} else {
			sb = new StringBuilder("http://").append(host).append(subPath);
		}
		if (null == getParams) {
			return sb.toString();
		}
		if (!sb.toString().contains("?")) {
			sb.append("?");
		} else {
			sb.append("&");
		}
		Iterator<Entry<String, String>> iterator = getParams.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			sb.append(URLEncoder.encode(next.getKey(), DEFAULT_PARAMS_ENCODING));
			sb.append("=");
			sb.append(URLEncoder.encode(next.getValue(),
					DEFAULT_PARAMS_ENCODING));
			sb.append("&");
		}
		return sb.toString();
	}
}
