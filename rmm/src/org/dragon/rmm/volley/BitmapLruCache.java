package org.dragon.rmm.volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import com.android.volley.Cache.Entry;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageCache {

	private DiskBasedCache diskCache;
	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	public BitmapLruCache(int maxSize, File path) {
		super(maxSize);
		diskCache = new DiskBasedCache(path);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url) {
		Bitmap bitmap = get(url);
		if (null == bitmap) {
			Entry entry = diskCache.get(url);
			if (null != entry && null != entry.data) {
				bitmap = bytes2Bimap(entry.data);
			}
		}
		return bitmap;
	}

	@Override
	public void putBitmap(final String url, final Bitmap bitmap) {
		if (null == bitmap || TextUtils.isEmpty(url)) {
			return;
		}
		cachedThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				Entry entry = new Entry();
				entry.data = bitmap2Bytes(bitmap);
				if (entry.data != null && entry.data.length > 0) {
					diskCache.put(url, entry);
					put(url, bitmap);
				}
			}
		});
	}

	private byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	public Bitmap bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}
}
