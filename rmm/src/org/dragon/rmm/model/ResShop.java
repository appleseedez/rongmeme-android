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
}
