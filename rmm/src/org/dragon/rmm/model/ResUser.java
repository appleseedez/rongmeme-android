package org.dragon.rmm.model;

public class ResUser extends InfoUserLogin {

	public int userid;
	public String nickname;
	public String address;
	public String sessionToken = "";

	public ResUser(String username, String password, int userid, String nickname, String address,String sessionTString) {
		super(username, password);
		this.userid = userid;
		this.nickname = nickname;
		this.address = address;
		this.sessionToken=sessionToken;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

}
