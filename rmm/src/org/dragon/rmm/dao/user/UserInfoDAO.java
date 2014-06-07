/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.rmm.dao.user;

import org.dragon.core.galhttprequest.GalHttpRequest;
import org.dragon.core.galhttprequest.GalHttpRequest.GalHttpLoadTextCallBack;
import org.dragon.rmm.Constants;

import android.content.Context;

/**
 * 用户信息的DAO
 * 
 * @author dengjie
 * 
 */
public class UserInfoDAO {
    /**
     * TAG用来描述是哪个类的字符串常量，多用于日志
     */
    private static final String TAG = "UserInfoDAO";

    /**
     * 因为网络通讯组件依赖了android.content.Context
     */
    public static Context context;

    /**
     * 发送APP唯一ID和电话号码给后台服务器
     * 
     * @param remarkForm
     *            封装修改备注信息的表单数据
     * @param galHttpLoadTextCallBack
     *            回调函数
     */
    public static void saveAppIdAndPhoneNumber(String premisesId, String phoneNumber, final GalHttpLoadTextCallBack galHttpLoadTextCallBack) {
        String requestUrl = Constants.SERVER_DOMAIN_AND_PORT + "/appCustomer!doNotNeedSession_add.do";
        // 交给GalHttprequest自动组装url中的参数
        GalHttpRequest request = GalHttpRequest.requestWithURL(context, requestUrl);
        // 设置post内容
        request.setPostValueForKey("premisesId", premisesId);
        request.setPostValueForKey("phoneNumber", phoneNumber);
        request.startAsynRequestString(galHttpLoadTextCallBack);
    }

}
