/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

import java.util.List;

/**
 * 保洁服务实体
 * 
 * @author dengjie
 */
public class CleaningBigItemVO implements java.io.Serializable {

    /**
     * 序列化
     */
    private static final long serialVersionUID = -810113932802352227L;
    private String name;
    private int starlevel;

    private List<CleaningItemVO> cleaningItems;

    public CleaningBigItemVO(String name,int starlevel){
        this.name=name;
        this.starlevel=starlevel;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(int starlevel) {
        this.starlevel = starlevel;
    }

    public List<CleaningItemVO> getCleaningItems() {
        return cleaningItems;
    }

    public void setCleaningItems(List<CleaningItemVO> cleaningItems) {
        this.cleaningItems = cleaningItems;
    }

}
