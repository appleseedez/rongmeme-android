/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.domain.common;

/**
 * Head头
 * 
 * @author dengjie
 * @version 1.0, 2012-08-28
 * @since 1.0
 */
public class Head implements java.io.Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 1420624531824396520L;
    /**
     * 信号的类型
     */
    private String signalType;
    /**
     * 状态
     */
    private int status;
    /**
     * 会话token
     */
    private String sessionToken;

    public Head(String signalType, int status, String sessionToken) {
        this.signalType = signalType;
        this.status = status;
        this.sessionToken = sessionToken;
    }

    public String getSignalType() {
        return signalType;
    }

    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

}