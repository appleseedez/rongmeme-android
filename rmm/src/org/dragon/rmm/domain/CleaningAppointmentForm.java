/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

import org.dragon.rmm.domain.common.Head;

/**
 * 保洁服务实体
 * 
 * @author dengjie
 */
public class CleaningAppointmentForm implements java.io.Serializable {
    /**
     * 序列化
     */
    private static final long serialVersionUID = -7893758627968329706L;
    private Head head;
    private CleaningAppointmentFormBody body;

    public CleaningAppointmentForm(Head head, CleaningAppointmentFormBody body) {
        this.head = head;
        this.body = body;
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public CleaningAppointmentFormBody getBody() {
        return body;
    }

    public void setBody(CleaningAppointmentFormBody body) {
        this.body = body;
    }

}
