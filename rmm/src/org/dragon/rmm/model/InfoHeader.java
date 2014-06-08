package org.dragon.rmm.model;

public class InfoHeader {
	
	
	public InfoHeader() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public InfoHeader(int signalType, int status, String sessionToken) {
		super();
		this.signalType = signalType;
		this.status = status;
		this.sessionToken = sessionToken;
	}

	public int signalType = 1;
	public int status = 0;
	public String sessionToken = "";
}
