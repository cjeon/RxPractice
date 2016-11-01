package com.example.cjeon.rxpractice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cjeon on 10/31/16.
 */

public class ImageDownloader {
    private static LruCache<String, Bitmap> cache = null;

    public ImageDownloader() {}

    public void initCache() {
        if (cache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            cache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    public Observable<Bitmap> getImage(String imageUrl) {
        Bitmap bitmap = cache.get(imageUrl);
        Observable<Bitmap> bitmapObservable;
        if (bitmap != null) {
            bitmapObservable = Observable.just(bitmap);
        } else {
            bitmapObservable = downloadImage(imageUrl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return bitmapObservable;
    }

    private Observable<Bitmap> downloadImage(String imageUrl) {
        return Observable.defer(() -> {
            Bitmap bitmap = null;
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            ByteArrayOutputStream byteArrayOutputStream = null;
            try {
                httpURLConnection = (HttpURLConnection) new URL(imageUrl).openConnection();
                inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                putImage(imageUrl, bitmap);
                return Observable.just(bitmap);
            } catch (Throwable e) {
                return Observable.error(e);
            } finally {
                try {
                    if (httpURLConnection != null)
                        httpURLConnection.disconnect();
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.flush();
                        byteArrayOutputStream.close();
                    }
                    if (inputStream != null)
                        inputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void putImage(String imageUrl, Bitmap bitmap) {
        cache.put(imageUrl, bitmap);
    }
}
