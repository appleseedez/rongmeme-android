package org.dragon.rmm.api;

public class ResponseResult {
	public ApiMethod apiMethod;
	public Object result;

	public ResponseResult(ApiMethod apiMethod, Object result) {
		this.apiMethod = apiMethod;
		this.result = result;
	}
}
