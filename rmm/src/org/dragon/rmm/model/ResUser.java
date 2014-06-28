package org.dragon.rmm.model;

public class ResUser extends InfoUserLogin {

	public long userid;
	public String nickname;
	public String address;

	public ResUser(String username, String password, long userid, String nickname, String address) {
		super(username, password);
		this.userid = userid;
		this.nickname = nickname;
		this.address = address;
	}

}
