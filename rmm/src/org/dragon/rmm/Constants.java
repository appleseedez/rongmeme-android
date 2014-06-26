/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm;

/**
 * <p>
 * 系统公用的常量类。提供各类使用的常量，避免魔鬼数字。改进了新分支
 * </p>
 * 
 * @author dengjie
 * @since 1.0
 */
public class Constants {

    /**
     * 楼盘销售热线电话，一键咨询的电话
     */
    public static final String SALES_TELEPHONE = "085233313333";
    /**
     * 服务器IP地址或者域名,当多台的时候则是域名并且加上端口
     */
    public static final String SERVER_DOMAIN_AND_PORT = "http://218.244.130.240:8080/";
    /**
     * 发布作品的时候，计算GEOHASH通过经纬度生成的整体字符串长度，越长越精确.
     */
    public static final int PUB_GEOHASH_CHAR_NUM = 12;
    /**
     * 美品线附近查询作品的时候，计算GEOHASH通过经纬度生成的整体字符串长度，越长越精确.
     */
    public static final int QUERY_GEOHASH_CHAR_NUM = 5;

    /**
     * 请在此为每个常量使用如此的注释，方便JAVADOC引用
     */
    public static final int VALUE_TEMP = 1;
    /**
     * Result结果对象封装失败的数字码
     */
    public static final int RESULT_UN_SUCCESSED_NO = 0;
    /**
     * Result结果对象封装成功的数字码
     */
    public static final int RESULT_SUCCESSED_NO = 1;

    // ---------------------------更多页面组件的功能菜单字符串常量------------------------------//

    /**
     * 私有构造方法，使该类不能被继承
     */
    private Constants() {

    }
}
