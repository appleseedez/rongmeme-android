/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.domain.message;

import java.io.Serializable;

/**
 * 通知实体
 * 
 * @author dengjie
 * 
 * @version 1.0 2012-11-28
 * @since 1.0
 * 
 */
public class Notification implements Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 7215026805275720817L;
    /**
     * 通知接收者
     */
    private long userId;
    /**
     * 通知类型
     */
    private NotificationType type;
    /**
     * 通知内容
     */
    private String msg;

    /**
     * 构造方法
     */
    public Notification() {

    }

    /**
     * 构造方法
     * 
     * @param userId
     *            userId
     * @param type
     *            消息类型：NotificationType
     */
    public Notification(long userId, NotificationType type) {
        this.userId = userId;
        this.type = type;
    }

    /**
     * 构造方法
     * 
     * @param userId
     *            userId
     * @param type
     *            消息类型：NotificationType
     * @param msg
     *            消息内容
     */
    public Notification(long userId, NotificationType type, String msg) {
        this.userId = userId;
        this.type = type;
        this.msg = msg;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getType() {
        return type.code();
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
