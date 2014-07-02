/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RmmRepository是用来标示持久层资源bean的注解。只要标记上了，则启动APP应用的时候，会被单例实例成bean。 外部只需要利用BeanFactory进行根据名称进行获取即可。
 * RmmRepository这个特殊的名字是因为Android扫描的时候，需要进行全包扫描，所以故意规避其他使用了Repository注解名称的问题。
 * 
 * @author dengjie
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RmmRepository {
    /**
     * The value may indicate a suggestion for a logical component name, to be turned into a Spring bean in case of an
     * autodetected component.
     * 
     * @return the suggested component name, if any
     */
    String value() default "";

    /**
     * 是否优先使用构造器进行初始化
     */
    boolean isInitByConstructor() default false;
}
