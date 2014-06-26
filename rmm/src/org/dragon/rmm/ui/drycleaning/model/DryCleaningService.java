package org.dragon.rmm.ui.drycleaning.model;

public class DryCleaningService {
	
	private int id;
	
	private String type;
	
	private String name;
	
	private int starlevel;
	
	private String unit;
	
	private int price;
	
	private double specialprice;
	
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStarlevel() {
		return starlevel;
	}

	public void setStarlevel(int starlevel) {
		this.starlevel = starlevel;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public double getSpecialprice() {
		return specialprice;
	}

	public void setSpecialprice(double specialprice) {
		this.specialprice = specialprice;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
