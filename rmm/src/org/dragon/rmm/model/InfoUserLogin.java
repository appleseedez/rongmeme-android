package org.dragon.rmm.model;

public class InfoUserLogin {
	public String username;
	public String password; // MD5

	public InfoUserLogin(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

}
