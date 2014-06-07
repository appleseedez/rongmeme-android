package org.dragon.core.galhttprequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;

/**
 * GalDownLoadTask
 * @author EricTan
 *
 */
public class GalDownLoadTask extends AsyncTask<GalDownloadParams, Integer, Void> {
    /**
     * buffer size
     */
    public static final int BUFFERSIZE = 512 * 1024;
    private static HashMap<String, GalDownLoadTask> runningMap = new HashMap<String, GalDownLoadTask>();

    private Context context;
    private GalDownloadParams downloadParams = null;
    private GalDownLoadTaskListener listener;
    /** 保存的绝对路径+文件名 */
    private String filePath;
    /** 显示的文件名 */
    private String titleName;
    private int notifyId;
    private boolean isFailed;
    private boolean isCancel = false;
    private File saveFile;
    private int filesize;

    private int errCode = 0;
    private int speed;

    // private NotificationManager mNotificationManager;

    /**
     * constructor
     * 
     * @param context
     *            context
     * @param listener
     *            listener
     */
    public GalDownLoadTask(Context context, GalDownLoadTaskListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(GalDownloadParams... params) {
        downloadParams = params[0];

        URL downloadURL = downloadParams.getDownLoadUrl();
        context = downloadParams.getContext();
        notifyId = downloadParams.getNotifyId();

        filePath = downloadParams.getFileName();
        titleName = downloadParams.getTitleName();

        File file = null;
        file = new File(filePath);

        if (runningMap.containsKey(filePath)) {
            cancel(true);
            return null;
        }
        runningMap.put(filePath, this);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        // 先删除久的内容
        saveFile = file;

        if (saveFile.exists() && this.listener != null && this.listener.onLoadFileExisting(context, downloadParams)) {
            return null;
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
            GALURL galurl = GalDBHelper.getInstance(context).getURL(downloadURL.toString());
            if (galurl != null) {
                if (!GalStringUtil.isEmpty(galurl.getLastModified())) {
                    conn.setRequestProperty("If-Modified-Since", galurl.getLastModified());
                }
                if (!GalStringUtil.isEmpty(galurl.getEtag())) {
                    conn.setRequestProperty("If-None-Match", galurl.getEtag());
                }
            }
            int statucode = conn.getResponseCode();
            switch (statucode) {
            case HttpURLConnection.HTTP_OK: {
                if (saveFile.exists()) {
                    saveFile.delete();
                    saveFile.createNewFile();
                }
                bis = new BufferedInputStream(conn.getInputStream(), BUFFERSIZE);
                bos = new BufferedOutputStream(new FileOutputStream(saveFile), BUFFERSIZE);
                int totalSize = conn.getContentLength();
                filesize = totalSize;
                readFromInputStream(bis, bos);
                break;
            }
            case HttpURLConnection.HTTP_NOT_MODIFIED: {
                // 从缓存读取数据
                File cacheFile = new File(galurl.getLocalData());
                conn.disconnect();
                bis = new BufferedInputStream(new FileInputStream(cacheFile), BUFFERSIZE);
                bos = new BufferedOutputStream(new FileOutputStream(saveFile), BUFFERSIZE);
                int totalSize = (int) cacheFile.length();
                filesize = totalSize;
                readFromInputStream(bis, bos);
                break;
            }
            default:
                isFailed = true;
                errCode = statucode;
                break;
            }
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.getMessage(), e);
            isFailed = true;
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                if (bis != null) {
                    bis.close();
                }

            } catch (IOException e) {
                LogUtil.e(e.getMessage(), e);
            }
        }

        return null;
    }

    private void readFromInputStream(BufferedInputStream bis, BufferedOutputStream bos) throws IOException {
        byte[] buf = new byte[BUFFERSIZE];
        int progress = 0;
        int finishedSize = 0;
        int readLen = -1;
        long time = System.currentTimeMillis();
        int lencount = 0;
        while ((readLen = bis.read(buf)) != -1 && !isCancel) {
            bos.write(buf, 0, readLen);
            finishedSize += readLen;
            lencount += readLen;
            // 计算新进度
            int newProgress = (int) (((double) finishedSize / filesize) * 100);
            long curTime = System.currentTimeMillis();
            if (newProgress - progress > 0) {
                if (curTime - time > 1000) {
                    speed = (int) (((lencount * 1000) >> 10) / (curTime - time));
                    lencount = 0;
                    time = curTime;
                }
                publishProgress(newProgress);
            }
            progress = newProgress;
        }
        if (!isCancel && finishedSize == filesize) {
            publishProgress(100);// 下载完成
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (runningMap != null) {
            runningMap.remove(filePath);
        }
        if (isFailed) {
            if (this.listener != null) {
                this.listener.onLoadFailed(context, downloadParams, errCode);
            }
            return;
        }
        if (isCancel) {
            if (this.listener != null) {
                this.listener.onLoadCancel(context, downloadParams);
            }
            return;
        }
        if (this.listener != null) {
            this.listener.onLoadFinish(context, downloadParams);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];
        // float size = ((int) ((filesize >> 10) * 10.0f / 1024)) / 10.0f;
        // CharSequence contentTitle = titleName + " [" + size + "M]";
        // CharSequence contentText = "正在下载，已完成  " + progress + "%," + speed
        // + "k/s";
        if (this.listener != null) {
            this.listener.onLoadProgress(context, downloadParams, progress, filesize, speed);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        if (runningMap != null) {
            runningMap.remove(filePath);
        }
        if (this.listener != null) {
            this.listener.onLoadCancel(context, downloadParams);
        }
        super.onCancelled();
    }

    /**
     * 是否正在下载，fileName不包含路径名
     * 
     * @param fileName
     *            file name
     * @return boolean
     */
    public static boolean isDownLoadingFile(String fileName) {
        return runningMap.containsKey(fileName);
    }

    /**
     * cancel download
     * 
     * @param filename
     *            file name
     */
    public static void cancelDownload(String filename) {
        try {
            if (isDownLoadingFile(filename)) {
                runningMap.get(filename).isCancel = true;
            }
        } catch (Exception e) {
            LogUtil.e(" Exception " + e.getMessage(), e);
        }
    }

    /**
     * GalDownLoadTaskListener
     * 
     * @author EricTan
     * 
     */
    public interface GalDownLoadTaskListener {
        /**
         *  如果下载不再重新下载则返回true,在子线程调用 
         *  @param context context
         *  @param params GalDownloadParams
         *  @return boolean
         */
        public boolean onLoadFileExisting(Context context, GalDownloadParams params);

        /**
         * onLoad Progress
         * @param context context
         * @param params GalDownloadParams instance
         * @param progress progress
         * @param allsize all size
         * @param kbpersecond KB per second
         */
        public void onLoadProgress(Context context, GalDownloadParams params, int progress, long allsize,
                int kbpersecond);

        /**
         * onLoad Finish
         * @param context context
         * @param params GalDownloadParams
         */
        public void onLoadFinish(Context context, GalDownloadParams params);

        /**
         * onLoad Failed
         * @param context context 
         * @param params GalDownloadParams
         * @param err error
         */
        public void onLoadFailed(Context context, GalDownloadParams params, int err);

        /**
         * onLoad Cancel
         * @param context context
         * @param params GalDownloadParams
         */
        public void onLoadCancel(Context context, GalDownloadParams params);
    }

}
