package org.dragon.rmm.api;

public enum ApiMethod {

	API_LOGIN("/eclean/login.json"),//登录
	API_LOGOUT("/eclean/logout.json"), //注销
	API_SHOPINFO("/eclean/showOneStore.json"), //门店详情
	API_REGIST_VERIFYCODE("/eclean/generateVerifyCode.json"),//注册验证码
	API_REGIST("/eclean/registry.json");//注册
	
	
	private String mUrl;

	ApiMethod(String url) {
		mUrl = url;
	}

	public String getValue() {
		return mUrl;
	}
}
