/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.core.utils.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.dragon.rmm.RmmSettings;
import org.dragon.rmm.annotation.RmmRepository;

import android.content.Context;
import android.util.Log;
import dalvik.system.DexFile;
import dalvik.system.PathClassLoader;

/**
 * 包的工具类
 * 
 * @author dengjie
 * 
 */
public class PackageScanner {

    private static final String TAG = "PackageScanner";
    private static final boolean DEBUG = RmmSettings.DEBUG;

    private static Field dexField;

    static {
        try {
            dexField = PathClassLoader.class.getDeclaredField("mDexs");
            dexField.setAccessible(true);
        } catch (Exception e) {
            Log.e(TAG, "PackageScanner初始化异常");
        }
    }

    public static Map<String, Object> scan(Context c) {
        Map<String, Object> beanMap = new HashMap<String, Object>();
        try {
            PathClassLoader classLoader = (PathClassLoader) Thread.currentThread().getContextClassLoader();

            DexFile[] dexs = (DexFile[]) dexField.get(classLoader);
            for (DexFile dex : dexs) {
                Enumeration<String> entries = dex.entries();
                while (entries.hasMoreElements()) {
                    String entry = entries.nextElement();

                    Class<?> entryClass = dex.loadClass(entry, classLoader);
                    if (entryClass != null) {
                        RmmRepository annotation = entryClass.getAnnotation(RmmRepository.class);
                        if (annotation != null) {
                            if (DEBUG) {
                                Log.d(TAG, "name=" + annotation.value() + "; class=" + entryClass.getName());
                            }
                            Object beanValue = null;
                            // 先判断bean中是否存在带有传入android.content.Context的构造函数,如果存在则使用构造器进行实例化
                            if (annotation.isInitByConstructor()) {
                                Constructor constructor = entryClass.getConstructor(Context.class);
                                beanValue = constructor.newInstance(c);
                            } else {
                                beanValue = entryClass.newInstance();
                            }
                            beanMap.put(annotation.value().trim(), beanValue);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "PackageScanner scan方法异常");
        }
        return beanMap;
    }

}