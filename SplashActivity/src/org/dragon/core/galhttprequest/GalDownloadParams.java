package org.dragon.core.galhttprequest;

import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

/**
 * GalDownloadParams
 * 
 * @author EricTan
 * 
 */
public class GalDownloadParams {

    /** 保存文件名 */
    private String fileName;

    private Context context;

    private int notifyId = 0;

    /**** 下载路径 **/
    private URL downLoadUrl;

    /**** 在状态栏的标题 **/
    private String titleName;

    /**
     * constructor
     * 
     * @param downLoadUrl
     *            download url
     * @param titleName
     *            title name
     * @param fileName
     *            file name
     * @param context
     *            context
     * @param notifyId
     *            notify id
     */
    public GalDownloadParams(URL downLoadUrl, String titleName, String fileName, Context context, int notifyId) {
        super();
        this.downLoadUrl = downLoadUrl;
        this.setTitleName(titleName);
        this.context = context;
        this.fileName = fileName;
        this.notifyId = notifyId;
    }

    /**
     * constructor
     * 
     * @param downLoadUrl
     *            download url
     * @param titleName
     *            title name
     * @param fileName
     *            file name
     * @param context
     *            context
     * @param notifyId
     *            notify id
     */
    public GalDownloadParams(String downLoadUrl, String titleName, String fileName, Context context, int notifyId) {
        super();
        try {
            this.downLoadUrl = new URL(downLoadUrl);
        } catch (MalformedURLException e) {
            LogUtil.e(e.getMessage(), e);
        }
        this.setTitleName(titleName);
        this.context = context;
        this.fileName = fileName;
        this.notifyId = notifyId;
    }

    public void setDownLoadUrl(URL downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    public URL getDownLoadUrl() {
        return downLoadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(int notifyId) {
        this.notifyId = notifyId;
    }

}
