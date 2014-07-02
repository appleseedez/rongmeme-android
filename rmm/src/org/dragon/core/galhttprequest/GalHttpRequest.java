package org.dragon.core.galhttprequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.dragon.core.galhttprequest.GalDownLoadTask.GalDownLoadTaskListener;
import org.dragon.core.utils.net.NetUtils;
import org.dragon.rmm.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.Toast;

/**
 * GalHtppRequest.java
 * 
 * @author 林秋明
 * @version V1.0
 */
public class GalHttpRequest {
    /**
     * cache root
     */
    public static final String CACHE_ROOT = "galhttprequest_cache";
    /**
     * buffer size
     */
    public static final int BUFFER_SIZE = 4 * 1024;
    /**
     * HttpClient采用StringEntity，而不是带参数的。
     */
    public static final String NO_PARAMETERS = "no_parameters";
    // 使用线程池，来重复利用线程，优化内存
    private static final int DEFAULT_THREAD_POOL_SIZE = 10;
    private static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    private static Handler mHandler = null;
    private Context mContext;
    private GalHttpRequestListener callBack;
    private GalHttpLoadImageCallBack imageLoadCallBack;
    private GalHttpLoadTextCallBack textLoadCallBack;
    private GALURL galurl;
    private ArrayList<Header> headers;
    private boolean cacheEnable = true;
    private boolean writeTocache = true;

    /**
     * constructor
     * 
     * @param context
     *            context
     * @param url
     *            url
     */
    public GalHttpRequest(Context context, String url) {
        this.mContext = context;
        initGalURL(url);
    }

    /**
     * constructor
     * 
     * @param context
     *            context
     */
    public GalHttpRequest(Context context) {
        this.mContext = context;
    }

    /**
     * 带参数的url，支持组装参数
     * 
     * @param context
     *            context
     * @param baseUrl
     *            base url
     * @param params
     *            传入参数名字
     * @return GalHttpRequest 返回类型
     * @date 2012-3-23 上午11:31:54
     * @throw
     */
    public static GalHttpRequest requestWithURL(Context context, String baseUrl, NameValuePair... params) {
        String url = baseUrl + concatParams(params);
        GalHttpRequest request = new GalHttpRequest(context, url);
        return request;
    }

    /**
     * 通过URL请求
     * 
     * @param context
     *            context
     * @param url
     *            url
     * @return GalHttpRequest 返回类型
     * @date 2012-3-23 上午11:31:54
     */
    public static GalHttpRequest requestWithURL(Context context, String url) {
        GalHttpRequest request = new GalHttpRequest(context, url);
        return request;
    }

    /**
     * 通过URL请求
     * 
     * @param context
     *            context
     * @param url
     *            url
     * @param headers
     *            headers
     * @return GalHttpRequest 返回类型
     * @date 2012-3-23 上午11:31:54
     */
    public static GalHttpRequest requestWithURL(Context context, String url, Header... headers) {
        GalHttpRequest request = new GalHttpRequest(context, url);
        ArrayList<Header> headList = new ArrayList<Header>();
        for (Header header : headers) {
            headList.add(header);
        }
        request.setHeaders(headList);
        return request;
    }

    static void checkHandler() {
        try {
            if (mHandler == null) {
                mHandler = new Handler();
            }
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.getMessage(), e);
            mHandler = null;
        }
    }

    private static String concatParams(NameValuePair[] params) {
        if (params == null || params.length <= 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            NameValuePair param = params[i];
            if (i == 0) {
                sb.append("?");
                sb.append(URLEncoder.encode(param.getName()) + "=" + URLEncoder.encode(param.getValue()));
            } else {
                sb.append("&");
                sb.append(URLEncoder.encode(param.getName()) + "=" + URLEncoder.encode(param.getValue()));
            }
        }
        return sb.toString();
    }

    /**
     * set url
     * 
     * @param url
     *            url
     */
    public void setURL(String url) {
        initGalURL(url);
    }

    private void initGalURL(String url) {
        galurl = GalDBHelper.getInstance(mContext).getURL(url);
        if (galurl == null) {
            galurl = new GALURL();
            galurl.setUrl(url);
        }
    }

    /**
     * 设置POST参数值。如果key是GalHttpRequest.NO_PARAMETERS，则表示后面的value不是带参数的值，而是直接使用StringEntity提交的数据，一般多用于JSON和对象映射。
     * 
     * @param key
     *            如果key是GalHttpRequest.NO_PARAMETERS，则表示后面的value不是带参数的值，而是直接使用StringEntity提交的数据，一般多用于JSON和对象映射。
     * @param value
     *            字符串值
     */
    public void setPostValueForKey(String key, String value) {
        if (galurl != null) {
            galurl.getPostData().put(key, value);
        }
    }

    /**
     * 异步请求
     */
    public void startAsynchronous() {
        checkHandler();
        // 先检查网络是否连接正常，如果不正常直接Toast提示，并且不发送请求
        if (!NetUtils.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, R.string.no_network_connected_status_pop, Toast.LENGTH_SHORT).show();
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                HttpResponse response = requestHttp(true, true);
                if (callBack == null) {
                    return;
                }
                if (response == null) {
                    callBack.loadFailed(response, null);
                    return;
                }
                try {
                    int statusCode = response.getStatusLine().getStatusCode();
                    switch (statusCode) {
                    case 200: {
                        InputStream is = getISFromRespone(response);
                        callBack.loadFinished(is, false);
                        break;
                    }
                    case 304: {
                        InputStream is = getISFromCache();
                        if (is != null) {
                            callBack.loadFinished(is, true);
                        } else {
                            HttpResponse strickResponse = requestHttp(false, false);
                            is = getISFromRespone(strickResponse);
                            callBack.loadFinished(is, false);
                        }
                        break;
                    }
                    default: {
                        if (!cacheEnable) {
                            return;
                        }
                        try {
                            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(galurl.getLocalData()));
                            callBack.loadFailed(response, bis);
                        } catch (Exception e) {
                            callBack.loadFailed(response, null);
                        }
                        break;
                    }
                    }
                } catch (Exception e) {
                    LogUtil.e(" Exception " + e.getMessage(), e);
                }
            }
        });
    }

    /**
     * start Asynchronise request String
     * 
     * @param gcallBack
     *            GalHttpLoadTextCallBack
     */
    public void startAsynRequestString(GalHttpLoadTextCallBack gcallBack) {
        checkHandler();
        // 先检查网络是否连接正常，如果不正常直接Toast提示，并且不发送请求
        if (!NetUtils.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, R.string.no_network_connected_status_pop, Toast.LENGTH_SHORT).show();
            return;
        }
        setTextLoadCallBack(gcallBack);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final String content = startSyncRequestString();
                if (textLoadCallBack != null && content != null) {
                    if (mHandler != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                textLoadCallBack.textLoaded(content);
                            }
                        });
                    } else {
                        textLoadCallBack.textLoaded(content);
                    }
                }
            }
        });
    };

    /**
     * start Asynchronise request bitmap
     * 
     * @param gcallBack
     *            GalHttpLoadImageCallBack
     */
    public void startAsynRequestBitmap(GalHttpLoadImageCallBack gcallBack) {
        checkHandler();
        // 先检查网络是否连接正常，如果不正常直接Toast提示，并且不发送请求
        if (!NetUtils.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, R.string.no_network_connected_status_pop, Toast.LENGTH_SHORT).show();
            return;
        }
        setImageLoadCallBack(gcallBack);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = startSyncRequestBitmap();
                if (imageLoadCallBack != null && bitmap != null) {
                    if (mHandler != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageLoadCallBack.imageLoaded(bitmap);
                            }
                        });
                    } else {
                        imageLoadCallBack.imageLoaded(bitmap);
                    }
                }
            }
        });
    };

    /**
     * 同步请求，返回的是有缓冲的InputStream
     * 
     * @return InputStream 返回类型
     * @date 2012-3-23 上午10:29:10
     */
    public InputStream startSynchronous() {
        // 先检查网络是否连接正常，如果不正常直接Toast提示，并且不发送请求
        if (!NetUtils.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, R.string.no_network_connected_status_pop, Toast.LENGTH_SHORT).show();
            return null;
        }
        HttpResponse response = requestHttp(true, true);
        if (response == null) {
            return null;
        }
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            switch (statusCode) {
            case 200: {
                InputStream is = getISFromRespone(response);
                return is;
            }
            case 304: {
                InputStream is = getISFromCache();
                if (is != null) {
                    return is;
                } else {
                    response = requestHttp(false, false);
                    is = getISFromRespone(response);
                    return is;
                }
            }
            default:
                if (!cacheEnable) {
                    return null;
                }
                try {
                    InputStream is = getISFromCache();
                    return is;
                } catch (Exception e) {
                    return null;
                }
            }
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * startDownLoadFile 必须在主线程调用
     * 
     * @param context
     *            context
     * @param params
     *            GalDownloadParams instance
     * @param listener
     *            传入参数名字
     * @date 2012-3-29 下午2:40:47
     * @throw
     */
    public static void startDownLoadFile(Context context, GalDownloadParams params, GalDownLoadTaskListener listener) {
        // 先检查网络是否连接正常，如果不正常直接Toast提示，并且不发送请求
        if (!NetUtils.isNetworkConnected(context)) {
            Toast.makeText(context, R.string.no_network_connected_status_pop, Toast.LENGTH_SHORT).show();
            return;
        }
        if (listener == null) {
            listener = new SimpleDownLoadTaskListener(context);
        }
        GalDownLoadTask task = new GalDownLoadTask(context, listener);
        task.execute(params);
    }

    /**
     * startGetStringSynchronous 同步请求String
     * 
     * @date 2012-3-23 下午4:26:31
     * @return String
     * @throw
     */
    public String startSyncRequestString() {
        // 先检查网络是否连接正常，如果不正常直接Toast提示，并且不发送请求
        if (!NetUtils.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, R.string.no_network_connected_status_pop, Toast.LENGTH_SHORT).show();
            return null;
        }
        InputStream is = startSynchronous();
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len = 0;
        try {
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.getMessage(), e);
        }
        return baos.toString();
    }

    /**
     * 同步请求图片
     * 
     * @return Bitmap 返回类型
     * @date 2012-3-23 下午4:25:43
     * @throw
     */
    public Bitmap startSyncRequestBitmap() {
        // 先检查网络是否连接正常，如果不正常直接Toast提示，并且不发送请求
        if (!NetUtils.isNetworkConnected(mContext)) {
            Toast.makeText(mContext, R.string.no_network_connected_status_pop, Toast.LENGTH_SHORT).show();
            return null;
        }
        Bitmap cache = getBitmapFromCache();
        if (cache != null) {
            return cache;
        }
        InputStream is = startSynchronous();
        if (is == null) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    public GALURL getGalurl() {
        return galurl;
    }

    private HttpResponse requestHttp(boolean haveLastModified, boolean haveEtag) {
        if (galurl == null || GalStringUtil.isEmpty(galurl.getUrl()) || "null".equals(galurl.getUrl())) {
            LogUtil.i("galurl 为空");
            return null;
        }
        HttpResponse response = null;
        try {
            if (galurl.getPostData().size() > 0) {
                HttpPost request = new HttpPost(galurl.getUrl());
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                HashMap<String, String> params = galurl.getPostData();

                Set<String> keyset = params.keySet();
                for (String key : keyset) {
                    String value = params.get(key);
                    // 不带参数的POST，一般内容体发送都是JSON字符串，采用StringEntity。否则采用参数提交，UrlEncodedFormEntity。
                    if (key.equals(NO_PARAMETERS)) {
                        request.addHeader("Content-Type", "application/json");
                        StringEntity se = new StringEntity(value, "UTF-8");
                        request.setEntity(se);
                    } else {
                        nameValuePairs.add(new BasicNameValuePair(key, value));
                        request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    }

                }

                if (!GalStringUtil.isEmpty(galurl.getLastModified()) && haveLastModified) {
                    request.addHeader("If-Modified-Since", galurl.getLastModified());
                }
                if (!GalStringUtil.isEmpty(galurl.getEtag()) && haveEtag) {
                    request.addHeader("If-None-Match", galurl.getEtag());
                }
                if (headers != null) {
                    for (Header header : headers) {
                        request.addHeader(header);
                    }
                }

                response = MyHttpClient.execute(mContext, request);
            } else {
                HttpGet request = new HttpGet(galurl.getUrl());
                if (!GalStringUtil.isEmpty(galurl.getLastModified())) {
                    request.addHeader("If-Modified-Since", galurl.getLastModified());
                }
                if (!GalStringUtil.isEmpty(galurl.getEtag())) {
                    request.addHeader("If-None-Match", galurl.getEtag());
                }
                response = MyHttpClient.execute(mContext, request);
            }
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.getMessage(), e);
        }
        return response;
    }

    private InputStream getISFromRespone(HttpResponse response) {
        try {
            if (writeTocache) {
                String filepath = writeInputSteamToCache(response.getEntity().getContent());
                if (!GalStringUtil.isEmpty(filepath)) {
                    String lastModified = getHeader(response, "Last-Modified");
                    String etag = getHeader(response, "ETag");
                    galurl.setLastModified(lastModified);
                    galurl.setEtag(etag);
                    galurl.setLocalData(filepath);
                    if (GalDBHelper.getInstance(mContext).existURL(galurl.getUrl())) {
                        GalDBHelper.getInstance(mContext).updateURL(galurl);
                    } else {
                        GalDBHelper.getInstance(mContext).insertURL(galurl);
                    }
                    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(galurl.getLocalData()));
                    return bis;
                }
            } else {
                return new BufferedInputStream(response.getEntity().getContent());
            }
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.getMessage(), e);
            return null;
        }
        return null;
    }

    private Bitmap getBitmapFromCache() {
        if (galurl == null || GalStringUtil.isEmpty(galurl.getLocalData())) {
            return null;
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(galurl.getLocalData());
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    private InputStream getISFromCache() {
        if (galurl == null || GalStringUtil.isEmpty(galurl.getLocalData())) {
            return null;
        }
        File cache = new File(galurl.getLocalData());
        try {
            return new BufferedInputStream(new FileInputStream(cache));
        } catch (Exception e) {
            return null;
        }
    }

    private String writeInputSteamToCache(InputStream is) {
        try {
            File cachedir = mContext.getDir(CACHE_ROOT, 0);
            BufferedInputStream bis = new BufferedInputStream(is);
            final String fileName = MD5.encodeMD5String(galurl.getUrl());
            File file = new File(cachedir, fileName);
            if (file.exists()) {
                file.delete();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            int len = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            bis.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.getMessage(), e);
            return null;
        }
    }

    private String getHeader(HttpResponse responese, String headerName) {
        if (GalStringUtil.isEmpty(headerName) || responese == null) {
            return null;
        }
        Header[] headerTemp = responese.getHeaders(headerName);
        if (headerTemp != null && headerTemp.length > 0) {
            return headerTemp[0].getValue();
        }
        return null;
    }

    public void setGalurl(GALURL galurl) {
        this.galurl = galurl;
    }

    public GalHttpRequestListener getListener() {
        return callBack;
    }

    public void setListener(GalHttpRequestListener gCallBack) {
        this.callBack = gCallBack;
    }

    public GalHttpLoadImageCallBack getImageLoadCallBack() {
        return imageLoadCallBack;
    }

    public void setImageLoadCallBack(GalHttpLoadImageCallBack imageLoadCallBack) {
        this.imageLoadCallBack = imageLoadCallBack;
    }

    public GalHttpLoadTextCallBack getTextLoadCallBack() {
        return textLoadCallBack;
    }

    public void setTextLoadCallBack(GalHttpLoadTextCallBack textLoadCallBack) {
        this.textLoadCallBack = textLoadCallBack;
    }

    public ArrayList<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(ArrayList<Header> headers) {
        this.headers = headers;
    }

    public boolean isCacheEnable() {
        return cacheEnable;
    }

    public void setCacheEnable(boolean cacheEnable) {
        this.cacheEnable = cacheEnable;
    }

    public boolean isWriteTocache() {
        return writeTocache;
    }

    public void setWriteTocache(boolean writeTocache) {
        this.writeTocache = writeTocache;
    }

    /**
     * GalHttpRequestListener
     * 
     * @author EricTan
     * 
     */
    public interface GalHttpRequestListener {
        /**
         * loadFinished
         * 
         * @param is
         *            input stream
         * @param fromcache
         *            from cache or not
         */
        public void loadFinished(InputStream is, boolean fromcache);

        /**
         * loadFailed
         * 
         * @param respone
         *            HttpResponse
         * @param cacheInputStream
         *            InputStream
         */
        public void loadFailed(HttpResponse respone, InputStream cacheInputStream);
    }

    /**
     * GalHttpLoadImageCallBack
     * 
     * @author EricTan
     * 
     */
    public interface GalHttpLoadImageCallBack {
        /**
         * imageLoaded
         * 
         * @param bitmap
         *            bitmap
         */
        public void imageLoaded(Bitmap bitmap);
    }

    /**
     * GalHttpLoadTextCallBack
     * 
     * @author EricTan
     * 
     */
    public interface GalHttpLoadTextCallBack {
        /**
         * textLoaded
         * 
         * @param text
         *            text
         */
        public void textLoaded(String text);
    }
}
