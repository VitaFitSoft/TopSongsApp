package com.example.topsongsapp.utils;

    import com.android.volley.toolbox.ImageLoader.ImageCache;

    import android.graphics.Bitmap;
    import android.support.v4.util.LruCache;

    public class LruBitmapCache extends LruCache<String, Bitmap> implements
            ImageCache {
        private static int getDefaultLruCacheSize() {
            final int cacheSize;
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            cacheSize = maxMemory / 8;
            return cacheSize;
        }

        public LruBitmapCache() {
            this(getDefaultLruCacheSize());
        }

        private LruBitmapCache(int sizeInKiloBytes) {
            super(sizeInKiloBytes);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }
}