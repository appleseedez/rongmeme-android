package org.dragon.rmm.model;

public class InfoUser {
	public String username;
	public String password; // MD5

	public InfoUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InfoUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

}
