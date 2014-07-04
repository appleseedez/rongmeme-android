/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

import org.dragon.rmm.domain.common.Head;

/**
 * 钟点工服务实体
 * 
 * @author dengjie
 */
public class HourlyEmployeeAppointmentForm implements java.io.Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = -548573044175680496L;
    private Head head;
    private HourlyEmployeeAppointmentFormBody body;

    public HourlyEmployeeAppointmentForm(Head head, HourlyEmployeeAppointmentFormBody body) {
        this.head = head;
        this.body = body;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public HourlyEmployeeAppointmentFormBody getBody() {
        return body;
    }

    public void setBody(HourlyEmployeeAppointmentFormBody body) {
        this.body = body;
    }

}
