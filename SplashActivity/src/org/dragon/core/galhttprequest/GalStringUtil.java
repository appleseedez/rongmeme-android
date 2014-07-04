package org.dragon.core.galhttprequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.format.DateFormat;

/**
 * GALStringUtil
 * 
 * @author EricTan
 * 
 */
public class GalStringUtil {
    /**
     * \r
     */
    public static final char RCHAR = '\r';
    /**
     * \n
     */
    public static final char NCHAR = '\n';
    /**
     * space char
     */
    public static final char SPACECHAR = ' ';

    /**
     * decode UTF8 string
     * 
     * @param inputStream
     *            InputStream
     * @return string
     */
    public static String decodeUTF8String(InputStream inputStream) {
        try {
            if (inputStream == null) {
                return "";
            }

            if (inputStream.available() <= 0) {
                return "";
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buff = new byte[1024];

            int len = 0;
            while ((len = inputStream.read(buff, 0, buff.length)) > 0) {

                baos.write(buff, 0, len);
            }

            String str = new String(baos.toByteArray(), "utf-8");

            return str;

        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }

        return null;
    }

    /**
     * to utf8 string
     * 
     * @param str
     *            str
     * @return string
     */
    public static String toUtf8String(String str) {
        try {
            byte[] buff = str.getBytes("utf-8");

            return new String(buff, "utf-8");

        } catch (Exception e) {
            // TODO: handle exception
            LogUtil.e(e.getMessage());
        }
        return str;

    }

    /**
     * get Utf8 bytes
     * 
     * @param str
     *            string
     * @return byte[]
     */
    public static byte[] getUtf8bytes(String str) {
        try {
            byte[] buff = str.getBytes("utf-8");

            return buff;
        } catch (Exception e) {
            // TODO: handle exception
            LogUtil.e(e.getMessage());
        }
        return new byte[0];
    }

    /**
     * encode url
     * 
     * @param src
     *            src
     * @return String
     */
    public static String urlEncode(String src) {
        String str = "";
        try {
            str = URLEncoder.encode(src, "UTF-8");

            if (str.indexOf("+") > -1) {
                str = str.replace("+", "%20");
            }

        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return str;
    }

    /**
     * decode url
     * 
     * @param src
     *            src
     * @return String
     */
    public static String urlDeocde(String src) {
        String str = "";
        try {
            if (src.indexOf("%20") > -1) {
                src = src.replace("%20", "+");
            }

            str = URLDecoder.decode(src, "UTF-8");

        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return str;
    }

    /**
     * get SD path
     * 
     * @return String
     */
    public static String getSDPath() {
        File sdDir = null;
        // 判断sd卡是否存在
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            // 获取根目录
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        } else {
            return null;
        }
    }

    /**
     * is folder exists
     * 
     * @param strFolder
     *            folder path
     * @return boolean
     */
    public static boolean isFolderExists(String strFolder) {
        File file = new File(strFolder);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * copy to memory
     * 
     * @param context
     *            context
     * @param srcFilePath
     *            src file path
     * @param dictFileName
     *            dict file name
     * @throws IOException
     *             io exception
     */
    public static void copyToMemory(Context context, String srcFilePath, String dictFileName) throws IOException {
        File srcFile = new File(srcFilePath);
        if (!srcFile.exists() || srcFile.isDirectory()) {
            return;
        }
        BufferedInputStream inBufferedInputStream = new BufferedInputStream(new FileInputStream(srcFile));
        FileOutputStream fos = context.openFileOutput(dictFileName, 0);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        byte[] b = new byte[1024 * 4];
        int len;
        while ((len = inBufferedInputStream.read(b)) != -1) {
            bos.write(b, 0, len);
            bos.flush();
        }
        inBufferedInputStream.close();
        bos.close();
    }

    /**
     * get file output stream
     * 
     * @param context
     *            context
     * @param dictFileName
     *            dict file name
     * @return File
     * @throws IOException
     *             io exception
     */
    public static File getFileOutputStream(Context context, String dictFileName) throws IOException {
        File fos = context.getFileStreamPath(dictFileName);
        return fos;
    }

    /**
     * copy assets files
     * 
     * @param context
     *            context
     * @param fileName
     *            file name
     * @param targetName
     *            target name
     * @throws IOException
     *             io exception
     */
    public static void copyAssetsFile(Context context, String fileName, String targetName) throws IOException {
        File targetFile = new File(targetName);
        if (targetFile.exists()) {
            LogUtil.i("target file is exists!");
            return;
        }
        AssetManager am = context.getAssets();
        InputStream source = am.open(fileName);
        BufferedInputStream inBufferedInputStream = new BufferedInputStream(source);

        targetFile.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(targetFile);
        BufferedOutputStream outBufferedOutputStream = new BufferedOutputStream(outputStream);

        byte[] b = new byte[1024 * 4];
        int len;
        while ((len = inBufferedInputStream.read(b)) != -1) {
            outBufferedOutputStream.write(b, 0, len);
        }
        outBufferedOutputStream.flush();
        inBufferedInputStream.close();
        outBufferedOutputStream.close();
        outputStream.close();
        source.close();
    }

    /**
     * time string
     * 
     * @param dateTaken
     *            date
     * @return String
     */
    public static String timeString(long dateTaken) {
        return DateFormat.format("yyyy/MM/dd kk:mm:ss", dateTaken).toString();
    }

    /**
     * 
     * 半角转全角
     * 
     * @param input
     *            String.
     * 
     * @return 全角字符串.
     */

    public static String toSBC(String input) {

        // // 半角转全角：
        // char[] c = input.toCharArray();
        // for (int i = 0; i < c.length; i++) {
        // if (c[i] == 32) {
        // c[i] = (char) 12288;
        // continue;
        // }
        // if (c[i] < 127)
        // c[i] = (char) (c[i] + 65248);
        // }

        return input;
    }

    /**
     * <summary> 转半角的函数 全角空格为12288，半角空格为32 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     * 
     * @param input
     *            任意字符串
     * @return 半角字符串
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }

    /**
     * split to lines
     * 
     * @param content
     *            content
     * @return ArrayList<String>
     */
    public static ArrayList<String> splitToLines(char[] content) {
        ArrayList<String> lines = new ArrayList<String>();
        int len = content.length;

        StringBuffer line = new StringBuffer();
        for (int i = 0; i < len; i++) {
            char c = content[i];
            line.append(c);
            // 遇到\r要判断是否接着是\n
            if (c == RCHAR) {
                char nc = content[i + 1];
                if (nc == NCHAR) {
                    line.append(nc);
                    i++;
                }
                lines.add(line.toString());
                line.delete(0, line.length());
            } else if (c == NCHAR) {
                // 遇到\n直接换行
                lines.add(line.toString());
                line.delete(0, line.length());
            } else {
                if (i == len - 1) {
                    lines.add(line.toString());
                }
            }
        }
        return lines;
    }

    /**
     * 得到\n或\r换行的最大块
     * 
     * @param content
     *            content
     * @param headstring
     *            headstring
     * @return String
     */
    public static String getMaxChunked(char[] content, String[] headstring) {
        String contentStr = new String(content);
        int len = content.length;
        int index = len - 1;
        for (int i = len - 1; i >= 0; i--) {
            char c = content[i];
            if (c == RCHAR || c == NCHAR) {
                if (i < len - 1) {
                    index = i + 1;
                }
                index = i + 1;
                break;
            }
        }
        headstring[0] = contentStr.substring(index, len);

        return contentStr.substring(0, index);
    }

    /**
     * is a string empty
     * 
     * @param string
     *            string
     * @return boolean
     */
    public static boolean isEmpty(String string) {
        return (string == null || string.length() == 0) ? true : false;
    }
}
