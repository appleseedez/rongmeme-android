/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 评估
 * 
 * @ClassName: Evaluation
 * @author dengjie
 * @date 2014年6月9日 下午2:42:43
 */
public class Evaluation implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1507316872584862670L;
    private long id;
    private long orderid;
    private int level;
    private long userid;
    private String username;
    private String content;
    private Long adminid;
    private String adminname;
    private Timestamp revisittime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderid() {
        return orderid;
    }

    public void setOrderid(long orderid) {
        this.orderid = orderid;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAdminid() {
        return adminid;
    }

    public void setAdminid(Long adminid) {
        this.adminid = adminid;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public Timestamp getRevisittime() {
        return revisittime;
    }

    public void setRevisittime(Timestamp revisittime) {
        this.revisittime = revisittime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
