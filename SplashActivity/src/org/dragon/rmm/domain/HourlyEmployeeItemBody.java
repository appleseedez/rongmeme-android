/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.domain;

import java.util.List;


/**
 * Body内容
 * 
 * @author dengjie
 * @version 1.0, 2012-08-28
 * @since 1.0
 */
public class HourlyEmployeeItemBody implements java.io.Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = -1559870688076838258L;
    private List<HourlyEmployeeItemVO> extra;

    public List<HourlyEmployeeItemVO> getExtra() {
        return extra;
    }

    public void setExtra(List<HourlyEmployeeItemVO> extra) {
        this.extra = extra;
    }

}