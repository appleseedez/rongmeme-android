package org.dragon.rmm.model;

import java.util.List;

import org.dragon.rmm.ui.drycleaning.model.DryCleaningService;


public class RespDryCleanServices {
	
	private RespHeader head;
	
	private List<DryCleaningService> body;

	public RespHeader getHead() {
		return head;
	}

	public void setHead(RespHeader head) {
		this.head = head;
	}

	public List<DryCleaningService> getBody() {
		return body;
	}

	public void setBody(List<DryCleaningService> body) {
		this.body = body;
	}
}
