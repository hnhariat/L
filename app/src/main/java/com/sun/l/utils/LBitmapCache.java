package com.sun.l.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by sunje on 2016-03-04.
 */
public class LBitmapCache extends LruCache<String, Bitmap> {
    public static LBitmapCache sInstance;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public LBitmapCache(int maxSize) {
        super(maxSize);
    }


    public static LBitmapCache getInstance(Context context) {
        int memoryClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int maxSize = 1024 * 1024 * memoryClass / 4;
        if (sInstance == null) {
            synchronized (LBitmapCache.class) {
                sInstance = new LBitmapCache(maxSize);
            }
        }
        return sInstance;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getWidth() * value.getHeight() * 4;
    }
}
