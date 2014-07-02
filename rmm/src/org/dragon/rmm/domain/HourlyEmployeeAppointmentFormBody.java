/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

import java.util.List;

/**
 * 钟点工服务实体
 * 
 * @author dengjie
 */
public class HourlyEmployeeAppointmentFormBody implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8218693142226426533L;
    private long storeid;
    private String servicetype;
    private String storename;
    private double allprice;
    private long userid;
    private String name;
    private String phone;
    private String address;
    private String source;
    private List<HourlyEmployeeSendAppointmentItemForm> services;

    public HourlyEmployeeAppointmentFormBody(long storeid, String servicetype, String storename, double allprice,
            long userid, String name, String phone, String address, String source,
            List<HourlyEmployeeSendAppointmentItemForm> services) {
        this.storeid = storeid;
        this.servicetype = servicetype;
        this.storename = storename;
        this.allprice = allprice;
        this.userid = userid;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.source = source;
        this.services = services;
    }

    public long getStoreid() {
        return storeid;
    }

    public void setStoreid(long storeid) {
        this.storeid = storeid;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public double getAllprice() {
        return allprice;
    }

    public void setAllprice(double allprice) {
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

    public List<HourlyEmployeeSendAppointmentItemForm> getServices() {
        return services;
    }

    public void setServices(List<HourlyEmployeeSendAppointmentItemForm> services) {
        this.services = services;
    }

}
