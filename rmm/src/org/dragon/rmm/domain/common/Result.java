/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.domain.common;

import org.dragon.rmm.Constants;

/**
 * 登陆、注册等动作型的接口使用的返回值，用来序列化JSON。
 * 
 * @author dengjie
 * @version 1.0, 2012-08-28
 * @since 1.0
 */
public class Result implements java.io.Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 644563057018200234L;

    /**
     * 1表示成功，0表示失败.默认为0失败
     */
    private int status = Constants.RESULT_UN_SUCCESSED_NO;

    /**
     * 成功、失败需要发送携带的字符串信息给前端
     */
    private String msg;
    /**
     * 结果对象封装携带的附件
     */
    private Object extra;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getExtra() {
        return extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }

}