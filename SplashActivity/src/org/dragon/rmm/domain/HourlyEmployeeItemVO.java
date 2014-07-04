/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

/**
 * 钟点工服务实体
 * 
 * @author dengjie
 */
public class HourlyEmployeeItemVO implements java.io.Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 160932060373129846L;
    private long id;
    private long storeid;
    private String storename;
    private String name;
    private int sex;
    private int age;
    private int workyears;
    private String skills;
    private double lowprice;
    private double price;
    private String avatar;

    public HourlyEmployeeItemVO(long id, long storeid, String storename, String name, int sex, int age, int workyears,
            String skills, double lowprice, double price, String avatar) {
        this.id = id;
        this.storeid = storeid;
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.workyears = workyears;
        this.skills = skills;
        this.lowprice = lowprice;
        this.price = price;
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWorkyears() {
        return workyears;
    }

    public void setWorkyears(int workyears) {
        this.workyears = workyears;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public double getLowprice() {
        return lowprice;
    }

    public void setLowprice(double lowprice) {
        this.lowprice = lowprice;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
