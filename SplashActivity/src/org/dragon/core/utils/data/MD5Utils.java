/*
 * 版权：Copyright 2010-2015 leze Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2012-7-17
 * 修改内容：
 */
/*
 * 版权：Copyright 2010-2015 leze Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2012-7-17
 * 修改内容：
 */
package org.dragon.core.utils.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5消息摘要的工具类,支持加密
 * 
 * @author dengjie
 * @since 1.0
 */
public final class MD5Utils {

    private static final ThreadLocal<MD5Utils> LOCAL = new ThreadLocal<MD5Utils>();

    private MD5Utils() {
        super();
    }

    /**
     * 工厂方法获取MD5Utils实例
     * 
     * @return MD5Utils实例
     */
    public static MD5Utils getMD5Utils() {
        MD5Utils encrypt = LOCAL.get();
        if (encrypt == null) {
            encrypt = new MD5Utils();
            LOCAL.set(encrypt);
        }
        return encrypt;
    }

    /**
     * MD5前缀消息摘要
     * 
     * @param s
     *            需要加密的字符串
     * @return MD5前缀消息摘要
     */
    public static String encode(String s) {
        if (s == null) {
            return null;
        }
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(s.getBytes());// update处理
            byte[] encryContext = md.digest();// 调用该方法完成计算

            int i;
            buf = new StringBuffer("");
            for (int offset = 0; offset < encryContext.length; offset++) {// 做相应的转化（十六进制）
                i = encryContext[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
}
