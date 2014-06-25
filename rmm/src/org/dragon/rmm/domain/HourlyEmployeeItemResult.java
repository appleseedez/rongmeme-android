/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.domain;

import java.util.Map;

import org.dragon.rmm.domain.common.Head;

/**
 * 所有JSON结果的序列化实体
 * 
 * @author dengjie
 * @version 1.0, 2012-08-28
 * @since 1.0
 */
public class HourlyEmployeeItemResult implements java.io.Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 2668356569227527827L;
    /**
     * 成功、失败需要发送携带的字符串信息给前端
     */
    private Head head;
    /**
     * 结果对象封装携带的附件
     */
    private HourlyEmployeeItemBody body;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public HourlyEmployeeItemBody getBody() {
        return body;
    }

    public void setBody(HourlyEmployeeItemBody body) {
        this.body = body;
    }

}