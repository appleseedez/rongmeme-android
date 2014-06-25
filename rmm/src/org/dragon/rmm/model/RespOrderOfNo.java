package org.dragon.rmm.model;

import org.dragon.rmm.ui.center.model.UserOrder;

public class RespOrderOfNo {
	
	private RespHeader head;
	
	private UserOrder body;

	public RespHeader getHead() {
		return head;
	}

	public void setHead(RespHeader head) {
		this.head = head;
	}

	public UserOrder getBody() {
		return body;
	}

	public void setBody(UserOrder body) {
		this.body = body;
	}
	
}
