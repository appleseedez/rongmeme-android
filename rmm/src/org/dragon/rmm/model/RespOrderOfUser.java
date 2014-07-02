package org.dragon.rmm.model;

import java.util.List;

import org.dragon.rmm.ui.center.model.UserOrder;


public class RespOrderOfUser {
	
	private RespHeader head;
	
	private List<UserOrder> body;

	public RespHeader getHead() {
		return head;
	}

	public void setHead(RespHeader head) {
		this.head = head;
	}

	public List<UserOrder> getBody() {
		return body;
	}

	public void setBody(List<UserOrder> body) {
		this.body = body;
	}
	
}
