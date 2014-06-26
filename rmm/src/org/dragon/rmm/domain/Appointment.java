/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 预约实体，客户端提交的
 * 
 * @ClassName: Appointment This is hourly worker appointment.
 *             <p/>
 *             { servicetype:"bj", storeid: 12, storename:"圣菲店", allprice: 500, userid: 101, name: "张女士", phone: "联系电话",
 *             address:"八里小区2栋3单元1023", source:"app", services:[ {itemid:1,name:"居家保洁"}, {itemid:2,name:"沙发维护"} ] }
 *             <p/>
 * 
 * @author dengjie
 * @date 2014年5月27日 下午4:29:12
 */
public class Appointment implements Serializable {


    /**
     * 
     */
    private static final long serialVersionUID = -1256788306786927964L;
    private String servicetype;
    private long storeid;
    private String storename;
    private BigDecimal allprice;
    private long userid;
    private String name;
    private String phone;
    private String address;
    private String source;

    private Item[] services;

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

    public Item[] getServices() {
        return services;
    }

    public void setServices(Item[] services) {
        this.services = services;
    }

    public BigDecimal getAllprice() {
        return allprice;
    }

    public void setAllprice(BigDecimal allprice) {
        this.allprice = allprice;
    }

}
