package org.dragon.rmm.api;

public enum ApiMethod {

	API_LOGIN("/eclean/login.json"),//登录
	API_LOGOUT("/eclean/logout.json"), //注销
	API_SHOPINFO("/eclean/showOneStore.json"), //门店详情
	API_SHOPLIST("/eclean/loadRecentStores.json"), //周边门店列表
	API_REGIST_VERIFYCODE("/eclean/generateVerifyCode.json"),//注册验证码
	API_COMMENT_LIST("/eclean/loadEvaluationsOfStore.json"),//评论列表
	API_COMMENT("/eclean/addEvaluation.json"),//评论
	API_REGIST("/eclean/registry.json"),//注册
	
	/**
	 * 查看我的所有订单
	 */
	API_LOAD_ORDERS_OF_USER("/eclean/loadOrdersOfUser.json"),
	
	/**
	 * 查询订单
	 */
	API_FIND_ORDER_BY_NO("/eclean/findOrderByNo.json"),
	
	/**
	 * 编辑常用地址信息
	 */
	API_EDIT_USER_INFO("/eclean/editUserInfo.json"),
	
	/**
	 * 加载干洗信息
	 */
	API_LOAD_DRY_CLEAN_SERVICES("/eclean/loadDryCleanServices.json"),
	
	/**
	 * 生成干洗预约
	 */
	API_CREATE_DRY_CLEAN_APPOINTENT("/eclean/createDryCleanAppointment.json");
	
	private String mUrl;

	ApiMethod(String url) {
		mUrl = url;
	}

	public String getValue() {
		return mUrl;
	}
}
