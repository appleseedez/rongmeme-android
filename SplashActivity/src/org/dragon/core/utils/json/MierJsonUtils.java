/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2012-7-17
 * 修改内容：
 */
package org.dragon.core.utils.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.dragon.core.galhttprequest.GalHttpRequest;
import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.collections.ArrayUtils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Gson工具类，一些业务操作直接使用
 * 
 * @author dengjie
 * @version 1.0, 2013-2-22
 * @since 1.0
 */
public final class MierJsonUtils {

    private static String TAG = "MierJsonUtils";
    // -- content-type 常量定义 --//
    private static final String JSON_TYPE = "application/json";

    /**
     * 设置成私有的构造方法防止被继承
     */
    private MierJsonUtils() {

    }

    /**
     * 工厂方法实例一个Gson对象，用来操作序列化与反序列化
     * 
     * @return Gson对象
     */
    public static Gson initGson() {
        Gson gson = new Gson();
        return gson;
    }

    /**
     * 把对象转成JSON，默认字符集是UTF-8
     * 
     * @param object
     *            需要被转换成JSON的Object对象
     */
    public static String toJson(Object object) {
        Gson gson = initGson();
        String json = gson.toJson(object);
        // 发送数据完成，清空数据资源
        clearObject(object);
        return json;
    }

    /**
     * POST写数据到指定的request方法，默认字符集是UTF-8
     * 
     * @param object
     *            需要被转换成JSON的Object对象
     * @param request
     *            封装的GalHttpRequest,要求必须经过GalHttpRequest.requestWithURL(this, POST_URL);方法得到的
     * @param callBack
     *            GalHttpLoadTextCallBack回调对象
     */
    public static void writeValue(Object object, GalHttpRequest request, GalHttpLoadTextCallBack callBack) {
        Gson gson = initGson();
        String json = gson.toJson(object);
        // 设置post内容
        request.setPostValueForKey("postData", json);
        request.startAsynRequestString(callBack);
        // 发送数据完成，清空数据资源
        clearObject(object);
        gson = null;
    }

    /**
     * 根据指定的对象属性，进行序列化JSON。白名单属性，只输出指定了的属性
     * 
     * @param object
     *            对象
     * @param userfulProperties
     *            userfulProperties 过滤属性的白名单，如果数组为null或是空元素，则不进行白名单过滤
     * @return Json字符串
     */
    public static String toJsonByUserfulProperties(Object object, final Map<Class<?>, String[]> userfulProperties) {
        // 创建一个带过滤条件的gson对象
        Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
            /**
             * 设置要过滤的属性
             */
            @Override
            public boolean shouldSkipField(FieldAttributes attr) {
                // 我们只过滤User类的id属性，而Type类的id属性还是要输出的
                boolean b = false;
                // 根据配置的Class key获取对应的白名单属性
                String[] userfulPropertiesArr = userfulProperties.get(attr.getDeclaringClass());
                // 根据属性对应的Class获取不到配置的时候，则不进行过滤直接展示
                if (userfulPropertiesArr == null) {
                    b = true;
                } else {
                    // 判断该属性是否在对应Class的白名单中，如果在则不过滤。
                    b = ArrayUtils.contains(userfulPropertiesArr, attr.getName());
                }

                // 这里，如果返回true就表示此属性要过滤，否则就输出
                return !b;
            }

            /**
             * 设置要过滤的类
             */
            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                // 这里，如果返回true就表示此类要过滤，否则就输出
                return false;
            }
        }).create();
        String json = gson.toJson(object);
        gson = null;
        clearObject(object);
        return json;
    }

    /**
     * 根据指定的对象属性，进行序列化JSON并且进行异步POST输出。白名单属性，只输出指定了的属性，默认字符集是UTF-8
     * 
     * @param object
     *            对象
     * @param request
     *            封装的GalHttpRequest,要求必须经过GalHttpRequest.requestWithURL(this, POST_URL);方法得到的
     * @param callBack
     *            GalHttpLoadTextCallBack回调对象
     * @param userfulProperties
     *            userfulProperties 过滤属性的白名单，如果数组为null或是空元素，则不进行白名单过滤
     */
    public static void writeValue(Object object, GalHttpRequest request, GalHttpLoadTextCallBack callBack, final Map<Class<?>, String[]> userfulProperties) {

        String json = MierJsonUtils.toJsonByUserfulProperties(object, userfulProperties);
        // 设置post内容
        request.setPostValueForKey("postData", json);
        request.startAsynRequestString(callBack);
    }

    /**
     * 读取JSON转换成指定类型的对象，默认字符集是UTF-8
     * 
     * @param json
     *            JSON字符串
     * @param typeOfT
     *            指定JSON转换的类型
     * @return 根据json转换成typeToken指定类型的对象
     */
    public static <Object> Object readValue(String json, Type typeOfT) {
        Gson gson = initGson();
        return gson.fromJson(json, typeOfT);
    }

    /**
     * 清除对象，自动判断是什么类型.保证构造JSON的对象能及时清空得到GC收回。集合采用clear方法。
     * 
     * @param object
     *            需要清空设置为NULL的对象
     */
    private static void clearObject(Object object) {
        // 如果本来就是null则直接返回
        if (object == null) {
            return;
        }
        Class<?> obClass = object.getClass();
        if (obClass == ArrayList.class) {
            List<?> o = (ArrayList<?>) object;
            o.clear();
        }
        if (obClass == LinkedList.class) {
            List<?> o = (LinkedList<?>) object;
            o.clear();
        }
        if (obClass == Vector.class) {
            List<?> o = (Vector<?>) object;
            o.clear();
        }
        if (obClass == HashSet.class) {
            Set<?> o = (HashSet<?>) object;
            o.clear();
        }
        if (obClass == LinkedHashSet.class) {
            Set<?> o = (LinkedHashSet<?>) object;
            o.clear();
        }
        if (obClass == LinkedHashSet.class) {
            Set<?> o = (LinkedHashSet<?>) object;
            o.clear();
        }
        if (obClass == HashMap.class) {
            Map<?, ?> o = (HashMap<?, ?>) object;
            o.clear();
        }
        object = null;

    }

}
