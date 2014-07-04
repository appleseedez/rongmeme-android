package org.dragon.rmm.model;

public class ResShop {

	public long id;
	public String name;
	public String address;
	public String logo;
	public double coordinatex;
	public double coordinatey;
	public String serviceids;
	public String services;
	public String introduce;
	public String serviceconcept;// 这是服务理念
	public String servicetenets;// 这是服务宗旨
	public String phone;

	public boolean isSelect;// 店铺是否选中。用于本地数据。

	public ResShop() {
	}

	public ResShop(long id, String name, String address, String logo, double coordinatex, double coordinatey, String serviceids, String services, String introduce, String serviceconcept,
			String servicetenets, String phone) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.logo = logo;
		this.coordinatex = coordinatex;
		this.coordinatey = coordinatey;
		this.serviceids = serviceids;
		this.services = services;
		this.introduce = introduce;
		this.serviceconcept = serviceconcept;
		this.servicetenets = servicetenets;
		this.phone = phone;
	}
}
