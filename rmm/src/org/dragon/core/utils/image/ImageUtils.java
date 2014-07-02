/*
 * 版权：Copyright 2010-2015 dragon Tech. Co. Ltd. All Rights Reserved.
 * 修改人：邓杰
 * 修改时间：2013-2-25
 * 修改内容：
 */
package org.dragon.core.utils.image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.dragon.core.utils.collections.ArrayUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.text.format.DateFormat;

/**
 * 图片工具类
 * <ul>
 * Bitmap、byte数组、drawable之间转换
 * </ul>
 * 
 * @author dengjie
 */
public class ImageUtils {

    /**
     * Bitmap转换为byte数组
     * 
     * @param b
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * byte数组转换为Bitmap
     * 
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return ArrayUtils.isEmpty(b) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * 
     * @param bmp
     * @param needRecycle
     * @return
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.JPEG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Drawable转换为Bitmap
     * 
     * @param d
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable) d).getBitmap();
    }

    /**
     * Bitmap转换为Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }

    /**
     * Drawable转换为byte数组
     * 
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

    /**
     * byte数组转换为Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b) {
        return bitmapToDrawable(byteToBitmap(b));
    }

    /**
     * 根据imageUrl获得InputStream，需要自己手动关闭InputStream
     * 
     * @param imageUrl
     *            图片url
     * @return
     */
    public static InputStream getInputStreamFromUrl(String imageUrl) {
        InputStream stream = null;
        try {
            URL url = new URL(imageUrl);
            stream = (InputStream) url.getContent();
        } catch (MalformedURLException e) {
            closeInputStream(stream);
            throw new RuntimeException("MalformedURLException occurred. ", e);
        } catch (IOException e) {
            closeInputStream(stream);
            throw new RuntimeException("IOException occurred. ", e);
        }
        return stream;
    }

    /**
     * 根据imageUrl获得Drawable
     * 
     * @param imageUrl
     *            图片url
     * @return
     */
    public static Drawable getDrawableFromUrl(String imageUrl) {
        InputStream stream = getInputStreamFromUrl(imageUrl);
        Drawable d = Drawable.createFromStream(stream, "src");
        closeInputStream(stream);
        return d;
    }

    /**
     * 根据imageUrl获得Bitmap
     * 
     * @param imageUrl
     *            图片url
     * @return
     */
    public static Bitmap getBitmapFromUrl(String imageUrl) {
        InputStream stream = getInputStreamFromUrl(imageUrl);
        Bitmap b = BitmapFactory.decodeStream(stream);
        closeInputStream(stream);
        return b;
    }

    /**
     * 根据imageUrl获得网络图片的高度和宽度集合
     * 
     * @param imageUrl
     *            图片url
     * @return
     */
    public static Map<String, Float> getWidthAndHeightFromUrl(String imageUrl) {
        Map<String, Float> resMap = new HashMap<String, Float>();
        if (imageUrl != null) {
            String[] imageUrlArr = imageUrl.split("!");
            String widthAndHeightRealValue = imageUrlArr[2];
            String[] widthAndHeightRealValueArr = widthAndHeightRealValue.split("x");

            resMap.put("width", Float.valueOf(widthAndHeightRealValueArr[0]));
            resMap.put("height", Float.valueOf(widthAndHeightRealValueArr[1]));
        }
        return resMap;
    }

    /**
     * 根据imageUrl获得网络图片的显示高度
     * 
     * @param imageUrl
     *            图片url
     * @return 根据宽度最大为系统宽度进行缩放的原则，得到真实显示的图片高度
     */
    public static int getShowHeightFromUrl(String imageUrl, int imageViewWidth) {
        Map<String, Float> resMap = getWidthAndHeightFromUrl(imageUrl);
        float height = resMap.get("height");
        float width = resMap.get("width");
        // 当真实图片的大小超过了图片视图组件imageview在屏幕中宽度的时候，则进行同步缩放。高度也进行等比例缩小
        if (width > imageViewWidth) {
            float scale = imageViewWidth / width;
            return Float.valueOf(height * scale).intValue();
        }
        return Float.valueOf(height).intValue();
    }

    /**
     * 将Uri转换成Bitmap，方便ImageView使用
     * 
     * @param contentResolver
     * @param uri
     *            图片uri
     * @return
     */
    public static Bitmap getBitmapFromUri(ContentResolver contentResolver, Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭InputStream
     * 
     * @param s
     */
    private static void closeInputStream(InputStream s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException occurred. ", e);
            }
        }
    }

    /**
     * 保存图片到系统相册中
     * 
     */
    public static void saveImageToPhoto(Context context, Bitmap mBmpForSave) {
        ContentValues values = new ContentValues(8);
        String newname = DateFormat.format("yyyy-MM-dd kk.mm.ss", System.currentTimeMillis()).toString();
        values.put(MediaStore.Images.Media.TITLE, newname);// 名称，随便
        values.put(MediaStore.Images.Media.DISPLAY_NAME, newname);
        values.put(MediaStore.Images.Media.DESCRIPTION, "保存");// 描述，随便
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());// 图像的拍摄时间，显示时根据这个排序
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");// 默认为jpg格式
        values.put(MediaStore.Images.Media.ORIENTATION, 0);//

        final String CAMERA_IMAGE_BUCKET_NAME = "/sdcard/dcim/camera";
        final String CAMERA_IMAGE_BUCKET_ID = String.valueOf(CAMERA_IMAGE_BUCKET_NAME.hashCode());
        File parentFile = new File(CAMERA_IMAGE_BUCKET_NAME);
        String name = parentFile.getName().toLowerCase();

        values.put(Images.ImageColumns.BUCKET_ID, CAMERA_IMAGE_BUCKET_ID);// id
        values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);

        // 先得到新的URI
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            // 写入数据
            OutputStream outStream = context.getContentResolver().openOutputStream(uri);
            mBmpForSave.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.close();
            mBmpForSave.recycle();
            return;
        } catch (Exception e) {

        }
    }

    /**
     * 放大缩小图片
     * 
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }

    /**
     * 获得圆角图片的方法
     * 
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获得带倒影的图片方法
     * 
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
                TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 根据已有图片进行平铺和拉伸
     * 
     * @param width
     * @param src
     * @return
     */
    public static Bitmap createRepeater(int width, int height, Bitmap src) {
        int count = (width + src.getWidth() - 1) / src.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int idx = 0; idx < count; ++idx) {
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }

        return bitmap;
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding bitmaps using the
     * decode* methods from {@link BitmapFactory}. This implementation calculates the closest inSampleSize that will
     * result in the final decoded bitmap having a width and height equal to or larger than the requested width and
     * height. This implementation does not ensure a power of 2 is returned for inSampleSize which can be faster when
     * decoding but results in a larger bitmap which isn't as useful for caching purposes.
     * 
     * @param options
     *            An options object with out* params already populated (run through a decode* method with
     *            inJustDecodeBounds==true
     * @param reqWidth
     *            The requested width of the resulting bitmap
     * @param reqHeight
     *            The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

}
