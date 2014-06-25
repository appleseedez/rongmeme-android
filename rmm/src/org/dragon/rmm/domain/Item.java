/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

import java.math.BigDecimal;

/**
 * 商品实体
 * @ClassName: Item {itemid:1,name:"毛衣",amount:2,price:20}, 综合性定义，不能等同于item表
 * @author dengjie
 * @date 2014年5月27日 下午4:42:02
 */
public class Item {
    private long itemid;
    private String name;
    private int amount;
    private BigDecimal price;

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
