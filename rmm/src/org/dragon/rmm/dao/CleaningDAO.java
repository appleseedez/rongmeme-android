/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.dao;

import java.util.List;

import org.dragon.core.galhttprequest.GalHttpRequest;
import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.Constants;
import org.dragon.rmm.domain.CleaningAppointmentForm;
import org.dragon.rmm.domain.CleaningAppointmentItemForm;
import org.dragon.rmm.domain.CleaningItemResult;
import org.dragon.rmm.domain.common.Head;

import android.content.Context;

/**
 * 用户信息的DAO
 * 
 * @author dengjie
 * 
 */
public class CleaningDAO {
    /**
     * TAG用来描述是哪个类的字符串常量，多用于日志
     */
    private static final String TAG = "CleaningDAO";

    /**
     * 因为网络通讯组件依赖了android.content.Context
     */
    public static Context context;

    /**
     * 加载星级服务内容
     * 
     * @param galHttpLoadTextCallBack
     *            回调函数
     */
    public static void loadServicePackages(String sessionToken, final GalHttpLoadTextCallBack galHttpLoadTextCallBack) {
        String requestUrl = Constants.SERVER_DOMAIN_AND_PORT + "/eclean/loadServicePackages.json";
        // 交给GalHttprequest自动组装url中的参数
        // 设置post内容
        CleaningItemResult res = new CleaningItemResult();
        Head head = new Head("android", 0, sessionToken);
        res.setHead(head);
        String json = MierJsonUtils.toJson(res);

        GalHttpRequest request = GalHttpRequest.requestWithURL(context, requestUrl);
        request.setPostValueForKey(GalHttpRequest.NO_PARAMETERS, json);
        request.startAsynRequestString(galHttpLoadTextCallBack);
    }

    /**
     * 加载所有保洁服务
     * 
     * @param galHttpLoadTextCallBack
     *            回调函数
     */
    public static void loadCleanServices(String sessionToken, final GalHttpLoadTextCallBack galHttpLoadTextCallBack) {
        String requestUrl = Constants.SERVER_DOMAIN_AND_PORT + "/eclean/loadCleanServices.json";
        // 交给GalHttprequest自动组装url中的参数
        // 设置post内容
        CleaningItemResult res = new CleaningItemResult();
        Head head = new Head("android", 0, sessionToken);
        res.setHead(head);
        String json = MierJsonUtils.toJson(res);

        GalHttpRequest request = GalHttpRequest.requestWithURL(context, requestUrl);
        request.setPostValueForKey(GalHttpRequest.NO_PARAMETERS, json);
        request.startAsynRequestString(galHttpLoadTextCallBack);
    }

    /**
     * 发送保洁预约内容
     * 
     * @param galHttpLoadTextCallBack
     *            回调函数
     */
    public static void createCleanAppointment(long storeid, String storename, double allprice, long userid,
            String name, String phone, String address, String sessionToken, List<CleaningAppointmentItemForm> services,
            final GalHttpLoadTextCallBack galHttpLoadTextCallBack) {
        String requestUrl = Constants.SERVER_DOMAIN_AND_PORT + "/eclean/createCleanAppointment.json";
        // 交给GalHttprequest自动组装url中的参数
        // 设置post内容
        Head head = new Head("android", 0, sessionToken);
        String servicetype = "bj";
        String source = "app";
        CleaningAppointmentForm form = new CleaningAppointmentForm(head, storeid, servicetype, storename, allprice,
                userid, name, phone, address, source, services);

        String json = MierJsonUtils.toJson(form);

        GalHttpRequest request = GalHttpRequest.requestWithURL(context, requestUrl);
        request.setPostValueForKey(GalHttpRequest.NO_PARAMETERS, json);
        request.startAsynRequestString(galHttpLoadTextCallBack);
    }

}
