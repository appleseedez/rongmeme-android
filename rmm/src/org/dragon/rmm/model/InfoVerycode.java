package org.dragon.rmm.model;

public class InfoVerycode {
	public String type = "registry";
	public String phone;

	public InfoVerycode() {
	}

	public InfoVerycode(String phone) {
		this.phone = phone;
	}

	public InfoVerycode(String type, String phone) {
		this.type = type;
		this.phone = phone;
	}

}
