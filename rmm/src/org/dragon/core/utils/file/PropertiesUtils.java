/*
 * 版权：Copyright 2010-2011 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰（dKF37984）
 * 修改时间：2010-12-14
 * 修改内容：
 */
package org.dragon.core.utils.file;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

/**
 * 用于获取任意资源文件的工具类
 * 
 * @author dengjie
 * @version 1.0, 2012-08-10
 * @since 1.0
 */
public abstract class PropertiesUtils {
    private static final String TAG = "PropertiesUtils";

    /**
     * 加载资源文件
     * 
     * @param filepath
     *            资源文件地址
     * @return 加载后的properties对象
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private static Properties loadProperties(String filepath) {
        Properties p = new Properties();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filepath);
            p.load(fileReader);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "loadProperties文件未找到异常", e);
        } catch (IOException e) {
            Log.e(TAG, "loadProperties io异常", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e1) {
                    Log.e(TAG, "loadProperties关闭输入流异常", e1);
                }
            }
        }
        return p;
    }

    /**
     * 设置指定键的值
     * 
     * @param filepath
     *            资源文件地址
     * @param key
     *            指定的key
     * @param value
     *            需要设置的值
     */
    public static void setProperty(String filepath, String key, String value) {
        Properties p = loadProperties(filepath);
        p.setProperty(key, value);
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(filepath);
            p.store(fos, "new language");
        } catch (IOException e) {
            Log.e(TAG, "setProperty修改环境文件的语言出错", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    Log.e(TAG, "setProperty关闭输出流异常", e1);
                }
            }
        }
    }

    /**
     * 根据给定的资源文件和key名称获取key对应的value
     * 
     * @param filepath
     *            资源文件路径
     * @param key
     *            资源文件的key
     * @return 对应key的value字符串
     */
    public static String getProperty(String filepath, String key) {
        Properties p = loadProperties(filepath);
        return p.getProperty(key);
    }
}
