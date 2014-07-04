/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

/**
 * 保洁服务实体 id: 1001, name: "八里小区店", address:"东一街", logo:"http://picture.com/jd/332/22/101.jpg", coordinatex: 5678,
 * coordinatey: 345, serviceids:"1,2,3", services: "保洁,钟点工,干洗", serviceconcept: "这是服务理念" , servicetenets: "这是服务宗旨"
 * 
 * @author dengjie
 */
public class ShakeStoreItemVO implements java.io.Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = -7893758627968329706L;
    private long id;
    private String name;
    private String address;
    private String logo;
    private double coordinatex;
    private double coordinatey;
    private String serviceids;
    private String services;
    private String serviceconcept;
    private String servicetenets;

    public ShakeStoreItemVO(long id, String name, String address, String logo, double coordinatex, double coordinatey,
            String serviceids, String services, String serviceconcept, String servicetenets) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.logo = logo;
        this.coordinatex = coordinatex;
        this.coordinatey = coordinatey;
        this.serviceids = serviceids;
        this.services = services;
        this.serviceconcept = serviceconcept;
        this.servicetenets = servicetenets;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public double getCoordinatex() {
        return coordinatex;
    }

    public void setCoordinatex(double coordinatex) {
        this.coordinatex = coordinatex;
    }

    public double getCoordinatey() {
        return coordinatey;
    }

    public void setCoordinatey(double coordinatey) {
        this.coordinatey = coordinatey;
    }

    public String getServiceids() {
        return serviceids;
    }

    public void setServiceids(String serviceids) {
        this.serviceids = serviceids;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getServiceconcept() {
        return serviceconcept;
    }

    public void setServiceconcept(String serviceconcept) {
        this.serviceconcept = serviceconcept;
    }

    public String getServicetenets() {
        return servicetenets;
    }

    public void setServicetenets(String servicetenets) {
        this.servicetenets = servicetenets;
    }

}
