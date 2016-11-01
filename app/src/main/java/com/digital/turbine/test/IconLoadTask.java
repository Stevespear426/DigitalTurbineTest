package com.digital.turbine.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * @Created 7/19/16
 */
public class IconLoadTask extends AsyncTask<Void, Void, Bitmap> {

    private static final String TAG = "IconLoadTask";
    private WeakReference<ImageView> mReference;
    private String mUrl;

    protected static final LruCache<String, Bitmap> sCache;

    static {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        sCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public IconLoadTask(@NonNull ImageView imageView, String url) {
        mReference = new WeakReference<>(imageView);
        mUrl = url;
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Bitmap bmp = null;
        try {
            URL url = new URL(mUrl);
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            Log.e(TAG, "IOException doInBackground", e);
        }

        if (bmp != null) sCache.put(mUrl, bmp);
        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        if (mReference != null && bitmap != null) {
            final ImageView imageView = mReference.get();
            final IconLoadTask iconLoadTask = getIconLoadTask(imageView);
            if (this == iconLoadTask) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<IconLoadTask> iconLoadTaskReference;

        public AsyncDrawable(IconLoadTask iconLoadTask) {
            super();
            iconLoadTaskReference = new WeakReference<>(iconLoadTask);
        }

        public IconLoadTask getIconLoadTask() {
            return iconLoadTaskReference.get();
        }
    }

    protected static IconLoadTask getIconLoadTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getIconLoadTask();
            }
        }
        return null;
    }

    protected boolean shouldCancel(String url) {
        return !TextUtils.equals(url, mUrl);
    }

    public static boolean cancelPotentialWork(String url, ImageView imageView) {
        if (sCache.get(url) != null) {
            imageView.setImageBitmap(sCache.get(url));
            return false;
        }
        final IconLoadTask iconLoadTask = getIconLoadTask(imageView);

        if (iconLoadTask != null) {
            // If bitmap data is not yet set or it differs from the new data
            if (iconLoadTask.shouldCancel(url)) {
                // Cancel previous task
                iconLoadTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
}