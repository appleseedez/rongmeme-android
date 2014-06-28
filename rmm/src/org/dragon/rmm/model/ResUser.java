package org.dragon.rmm.model;

public class ResUser extends InfoUserLogin {

	public int userid;
	public String nickname;
	public String address;

	public ResUser(String username, String password, int userid, String nickname, String address) {
		super(username, password);
		this.userid = userid;
		this.nickname = nickname;
		this.address = address;
	}

}
