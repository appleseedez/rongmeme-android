package org.dragon.core.utils.sns;

import java.util.Calendar;

/**
 * 社交工具类
 * 
 * @author dengjie
 * @since 1.0
 */
public class SnsUtils {
    // 生肖数组生
    private static final String[] CHINESE_ZODIACS = {"猴","鸡","狗","猪","鼠","牛","虎","兔","龙","蛇","马","羊"};
    // 星座代码顺序数组，按照星座顺序依次从1到12代表：水瓶座，双鱼座，白羊座，金牛座，双子座，巨蟹座，狮子座，处女座，天平座，天蝎座，射手座，摩羯座。
    private static final int[] CHINESE_ZODIAC_CODES = {1,2,3,4,5,6,7,8,9,10,11,12};
    // 星座名称顺序数组
    private static final String[] CONSTELLATIONS = {"水瓶座","双鱼座","白羊座","金牛座","双子座","巨蟹座","狮子座","处女座","天秤座","天蝎座","射手座","魔羯座"};
    // 星座代码顺序数组，按照星座顺序依次从1到12代表：水瓶座，双鱼座，白羊座，金牛座，双子座，巨蟹座，狮子座，处女座，天平座，天蝎座，射手座，摩羯座。
    private static final int[] CONSTELLATION_CODES = {1,2,3,4,5,6,7,8,9,10,11,12};
    // 星座边界日期集合
    private static final int[] CONSTELLATIONS_EDGE_DAY = {20,19,21,21,21,22,23,23,23,23,22,22};

    // private static final Log log = LogFactory.getLog(SnsUtils.class);

    /**
     * 根据日期获取生肖
     * 
     * @param time
     *            日期
     * @return 生肖名称
     */
    public static String date2Zodica(Calendar time) {
        return CHINESE_ZODIACS[time.get(Calendar.YEAR) % 12];
    }

    /**
     * 根据索引获取星座文字
     * 
     * @param index
     *            索引,从1开始。1到12表示对应的："水瓶座","双鱼座","白羊座","金牛座","双子座","巨蟹座","狮子座","处女座","天秤座","天蝎座","射手座","魔羯座"
     * @return 星座文字
     */
    public static String index2CONSTELLATIONS(int index) {
        return CONSTELLATIONS[index - 1];
    }

    /**
     * 根据日期获取星座代码。 按照星座顺序依次从1到12代表：水瓶座，双鱼座，白羊座，金牛座，双子座，巨蟹座，狮子座，处女座，天平座，天蝎座，射手座，摩羯座。
     * 
     * @param time
     *            具体日期时间
     * @return 星座代码
     */
    public static int date2ConstellationCode(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < CONSTELLATIONS_EDGE_DAY[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return CONSTELLATION_CODES[month];
        }
        // default to return 魔羯
        return CONSTELLATION_CODES[11];
    }

    /**
     * 根据日期获取生肖
     * 
     * @param time
     *            日期
     * @return 生肖名称
     */
    public static String db2Zodica(Calendar time) {
        return CHINESE_ZODIACS[time.get(Calendar.YEAR) % 12];
    }

    /**
     * 根据日期获取星座名称
     * 
     * @param time
     *            具体日期时间
     * @return 星座名称
     */
    public static String date2Constellation(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < CONSTELLATIONS_EDGE_DAY[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return CONSTELLATIONS[month];
        }
        // default to return 魔羯
        return CONSTELLATIONS[11];
    }
}
