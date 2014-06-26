/**
 * Copyright HZCW (He Zhong Chuang Wei) Technologies Co.,Ltd. 2013-2015. All rights reserved.
 */

package org.dragon.rmm.domain;

/**
 * 订单实体，我一般暂时用的不多
 * @ClassName: OrderBill
 * @author dengjie
 * @date 2014年6月12日 下午2:30:49
 */
public class OrderBill {

    public enum Status {
        APPOINT_WAIT_HANDLE(10, "未处理"), APPOINT_BECOME_ORDER(11, "已生成订单"),

        ORDER_WAITING_EXCUTE(0, "将要执行"), ORDER_WAITING_EXCUTE_WITHOUT_ARRANGE_SERVER(1, "将要执行，未分配人员"), ORDER_IN_EXCUTING(
                2, "正在进行服务"), ORDER_IS_COMPLETED(3, "订单完成"), ORDER_IS_CANCELED(4, "订单已经取消");

        private Integer code;
        private String value;

        private Status(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public Integer getCode() {
            return code;
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
    public static String takeValueOfCode(Integer code) {
        for (Status status : Status.values()) {
            if (status.getCode() == code) {
                return status.getValue();
            }
        }
        return null;
    }

}
