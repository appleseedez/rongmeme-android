/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

/**
 * 保洁服务实体
 * 
 * @author dengjie
 */
public class CleaningItemVO implements java.io.Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = -7893758627968329706L;
    private long id;
    private String type;
    private String name;
    private int starlevel;
    private String unit;
    private double price;
    private double specialprice;
    private String description;

    public CleaningItemVO(long id, String type, String name, int starlevel, String unit, double price,
            double specialprice, String description) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.starlevel = starlevel;
        this.unit = unit;
        this.price = price;
        this.specialprice = specialprice;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
