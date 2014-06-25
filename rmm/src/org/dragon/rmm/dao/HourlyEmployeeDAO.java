/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dragon.core.galhttprequest.GalHttpRequest;
import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.Constants;
import org.dragon.rmm.domain.HourlyEmployeeAppointmentForm;
import org.dragon.rmm.domain.HourlyEmployeeSendAppointmentItemForm;
import org.dragon.rmm.domain.common.CommonForm;
import org.dragon.rmm.domain.common.Head;

import android.content.Context;

/**
 * 用户信息的DAO
 * 
 * @author dengjie
 * 
 */
public class HourlyEmployeeDAO {
    /**
     * TAG用来描述是哪个类的字符串常量，多用于日志
     */
    private static final String TAG = "HourlyEmployeeDAO";

    /**
     * 因为网络通讯组件依赖了android.content.Context
     */
    public static Context context;

    /**
     * 加载钟点工服务内容
     * 
     * @param galHttpLoadTextCallBack
     *            回调函数
     */
    public static void loadHourlyWorkers(final GalHttpLoadTextCallBack galHttpLoadTextCallBack) {
        String requestUrl = Constants.SERVER_DOMAIN_AND_PORT + "/eclean/loadHourlyWorkers.json";
        // 交给GalHttprequest自动组装url中的参数
        // 设置post内容
        CommonForm res = new CommonForm();
        Head head = new Head("android", 0, "ab34ciepk3456677");
        res.setHead(head);
        Map<String, Object> body = new HashMap<String, Object>();
        // 这个商店的ID是需要获取的
        body.put("storeid", 10);
        body.put("start", 0);
        body.put("limit", 1000);
        res.setBody(body);
        String json = MierJsonUtils.toJson(res);

        GalHttpRequest request = GalHttpRequest.requestWithURL(context, requestUrl);
        request.setPostValueForKey(GalHttpRequest.NO_PARAMETERS, json);
        request.startAsynRequestString(galHttpLoadTextCallBack);
    }

    /**
     * 发送钟点工预约内容
     * 
     * @param galHttpLoadTextCallBack
     *            回调函数
     */
    public static void createHourlyWorkerAppointment(long storeid, String storename, double allprice, long userid,
            String name, String phone, String address, List<HourlyEmployeeSendAppointmentItemForm> services,
            final GalHttpLoadTextCallBack galHttpLoadTextCallBack) {
        String requestUrl = Constants.SERVER_DOMAIN_AND_PORT + "/eclean/createHourlyWorkerAppointment.json";
        // 交给GalHttprequest自动组装url中的参数
        // 设置post内容
        Head head = new Head("android", 0, "ab34ciepk3456677");
        String servicetype = "bj";
        String source = "app";
        HourlyEmployeeAppointmentForm form = new HourlyEmployeeAppointmentForm(head, storeid, servicetype, storename,
                allprice, userid, name, phone, address, source, services);

        String json = MierJsonUtils.toJson(form);

        GalHttpRequest request = GalHttpRequest.requestWithURL(context, requestUrl);
        request.setPostValueForKey(GalHttpRequest.NO_PARAMETERS, json);
        request.startAsynRequestString(galHttpLoadTextCallBack);
    }
}
