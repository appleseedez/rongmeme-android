package org.dragon.rmm.model;

import java.util.List;

import org.dragon.rmm.ui.drycleaning.model.DryCleaningReservedItem;


public class InfoDryCleanReservation {
	
	private String servicetype;
	
	private long storeid;
	
	private String storename;
	
	private int allprice;
	
	private long userid;
	
	private String name;
	
	private String phone;
	
	private String address;
	
	private String source;
	
	private List<DryCleaningReservedItem> services;

	public String getServicetype() {
		return servicetype;
	}

	public void setServicetype(String servicetype) {
		this.servicetype = servicetype;
	}

	public long getStoreid() {
		return storeid;
	}

	public void setStoreid(long storeid) {
		this.storeid = storeid;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public int getAllprice() {
		return allprice;
	}

	public void setAllprice(int allprice) {
		this.allprice = allprice;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public List<DryCleaningReservedItem> getServices() {
		return services;
	}

	public void setServices(List<DryCleaningReservedItem> services) {
		this.services = services;
	}
}
