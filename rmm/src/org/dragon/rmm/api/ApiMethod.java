package org.dragon.rmm.api;

public enum ApiMethod {

	API_LOGIN("/eclean/login.json");
	
	
	private String mUrl;

	ApiMethod(String url) {
		mUrl = url;
	}

	public String getValue() {
		return mUrl;
	}
}
