/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.dao;

import java.util.HashMap;
import java.util.Map;

import org.dragon.core.galhttprequest.GalHttpRequest;
import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.core.utils.json.MierJsonUtils;
import org.dragon.rmm.Constants;
import org.dragon.rmm.domain.common.CommonForm;
import org.dragon.rmm.domain.common.Head;

import android.content.Context;

/**
 * 摇一摇门店的DAO
 * 
 * @author dengjie
 * 
 */
public class ShakeStoreDAO {
    /**
     * TAG用来描述是哪个类的字符串常量，多用于日志
     */
    private static final String TAG = "ShakeStoreDAO";

    /**
     * 因为网络通讯组件依赖了android.content.Context
     */
    public static Context context;

    /**
     * 查找附近的门店
     * 
     * @param galHttpLoadTextCallBack
     *            回调函数
     */
    public static void loadRecentStores(String sessionToken, double coordinatex, double coordinatey,
            final GalHttpLoadTextCallBack galHttpLoadTextCallBack) {
        String requestUrl = Constants.SERVER_DOMAIN_AND_PORT + "/eclean/loadRecentStores.json";
        // 交给GalHttprequest自动组装url中的参数
        // 设置post内容
        CommonForm res = new CommonForm();
        Head head = new Head(1, 0, sessionToken);
        res.setHead(head);
        Map<String, Object> body = new HashMap<String, Object>();
        // 这个商店的ID是需要获取的
        body.put("coordinatex", coordinatex);
        body.put("coordinatey", coordinatey);
        res.setBody(body);
        String json = MierJsonUtils.toJson(res);

        GalHttpRequest request = GalHttpRequest.requestWithURL(context, requestUrl);
        request.setPostValueForKey(GalHttpRequest.NO_PARAMETERS, json);
        request.startAsynRequestString(galHttpLoadTextCallBack);
    }

}
