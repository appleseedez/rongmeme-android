package org.dragon.rmm.model;

public class InfoRegist {

	public String username;
	public String password;
	public String code;
	public double coordinatex;
	public double coordinatey;

	public InfoRegist(String username, String password, String code, double coordinatex, double coordinatey) {
		this.username = username;
		this.password = password;
		this.code = code;
		this.coordinatex = coordinatex;
		this.coordinatey = coordinatey;
	}
}
