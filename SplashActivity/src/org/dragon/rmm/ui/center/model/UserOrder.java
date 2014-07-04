package org.dragon.rmm.ui.center.model;

import java.util.List;

public class UserOrder {
	
	private int id;
	
	private int storeid;
	
	private String storename;
	
	private String ordertype;
	
	private double allprice;
	
	private String orderno;
	
	private int status;
	
	private String updatetime;
	
	private List<UserOrderService> services;
	
	private List<UserOrderServer> servers;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStoreid() {
		return storeid;
	}

	public void setStoreid(int storeid) {
		this.storeid = storeid;
	}

	public String getStorename() {
		return storename;
	}

	public void setStorename(String storename) {
		this.storename = storename;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public double getAllprice() {
		return allprice;
	}

	public void setAllprice(double allprice) {
		this.allprice = allprice;
	}

	public String getOrderno() {
		return orderno;
	}

	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public List<UserOrderService> getServices() {
		return services;
	}

	public void setServices(List<UserOrderService> services) {
		this.services = services;
	}

	public List<UserOrderServer> getServers() {
		return servers;
	}

	public void setServers(List<UserOrderServer> servers) {
		this.servers = servers;
	}
	
}
