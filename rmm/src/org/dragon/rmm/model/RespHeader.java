package org.dragon.rmm.model;

public class RespHeader {
	
	private int signalType;
	
	private int status;
	
	private String sessionToken;

	public int getSignalType() {
		return signalType;
	}

	public void setSignalType(int signalType) {
		this.signalType = signalType;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
	
}
