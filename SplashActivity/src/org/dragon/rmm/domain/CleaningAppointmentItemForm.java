/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.domain;

/**
 * Form item信息保洁
 * 
 * @author dengjie
 * @version 1.0, 2012-08-28
 * @since 1.0
 */
public class CleaningAppointmentItemForm implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2836277814993897822L;
    private long itemid;
    private String name;

    public long getItemid() {
        return itemid;
    }

    public void setItemid(long itemid) {
        this.itemid = itemid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}