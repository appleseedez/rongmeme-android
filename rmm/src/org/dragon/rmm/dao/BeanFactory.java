/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.dao;

import java.util.HashMap;
import java.util.Map;

import org.dragon.core.utils.bean.PackageScanner;
import org.dragon.rmm.dao.user.UserInfoDAO;

import android.content.Context;

/**
 * 所有DAO的工厂,所有DAO的实例都是通过DAOFactory进行生产。
 * 
 * @author dengjie
 * 
 */
public class BeanFactory {
    private static Map<String, Object> beanContext = new HashMap<String, Object>();

    /**
     * 创建所有DAO,增加一个DAO就在这个方法中进行配置。
     */
    public static void createAllBeans(Context c) {
        beanContext = PackageScanner.scan(c);
    }

    /**
     * 创建所有DAO,增加一个DAO就在这个方法中进行配置。
     */
    public static void setContextForAllDAOs(Context c) {
        UserInfoDAO.context = c;
    }

    /**
     * 获取Bean的方法
     * 
     * @param beanName
     * @return Bean
     */
    public synchronized static Object getBean(String beanName) {
        Object bean = null;
        if (beanContext != null) {
            bean = beanContext.get(beanName);
        }
        return bean;
    }
}
