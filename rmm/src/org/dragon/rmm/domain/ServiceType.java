/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

/**
 * 服务类型
 * @ClassName: ServiceType
 * @author dengjie
 * @date 2014年6月16日 下午3:12:54
 */
public class ServiceType {
    public enum Types {
        CLEAN("bj", "保洁"), HOURLY_WORKER("zd", "钟点工"), DRY_CLEAN("gx", "干洗");

        private String type;
        private String value;

        private Types(String type, String value) {
            this.type = type;
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * Find the value of the type.
     * 
     * @param type
     * @return NULL if not find. String
     */
    public static String getValueOfType(String type) {
        for (Types typeone : Types.values()) {
            if (typeone.getType().equals(type)) {
                return typeone.getValue();
            }
        }
        return null;
    }

}
