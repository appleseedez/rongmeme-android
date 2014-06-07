package org.dragon.core.galhttprequest;

import java.util.HashMap;

/**
 * GalHttprequest.java
 * @author 林秋明
 * @version V1.0
 */
public class GALURL {
    /**
     * URL Field
     *
     */
    public static class URLFiled {
        /**
         * URL
         */
        public static final String URL = "url";
        /**
         * last modified
         */
        public static final String LASTMODIFIED = "lastmodified";
        /**
         * e tag
         */
        public static final String ETAG = "etag";
        /**
         * local data
         */
        public static final String LOCALDATA = "localdata";
    }

    private String url;
    private String lastModified;
    private String etag;
    private String localData;
    private HashMap<String, String> postData;

    /**
     * constructor
     */
    public GALURL() {
    }

    /**
     * constructor 
     * @param url url
     * @param lastModified lastModified
     * @param etag etag
     * @param localData localData
     */
    public GALURL(String url, String lastModified, String etag, String localData) {
        this.url = url;
        this.lastModified = lastModified;
        this.etag = etag;
        this.localData = localData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getLocalData() {
        return localData;
    }

    public void setLocalData(String data) {
        this.localData = data;
    }

    /**
     * get post data
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getPostData() {
        if (postData == null) {
            postData = new HashMap<String, String>();
        }
        return postData;
    }

    public void setPostData(HashMap<String, String> postData) {
        this.postData = postData;
    }

    @Override
    public String toString() {
        if (url != null) {
            return url;
        }
        return super.toString();
    }
}
