package com.project.bittu.movieoclock.Network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.project.bittu.movieoclock.App.App;

/**
 * Created by Bittu on 6/21/2016.
 */
public class VolleySingleton {

    private static VolleySingleton singletonInstance = null;
    private static ImageLoader singletonImageLoader;
    private static RequestQueue singletonRequestQueue;


    private VolleySingleton(){
        singletonRequestQueue = Volley.newRequestQueue(App.getContext());
    }

    public static VolleySingleton getInstance(){
        if(singletonInstance == null){
            singletonInstance = new VolleySingleton();
            singletonImageLoader = new ImageLoader(singletonRequestQueue, new ImageLoader.ImageCache() {
                private LruCache<String, Bitmap> cache = new LruCache<>((int)(Runtime.getRuntime().maxMemory() / 1024) / 8);
                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }

                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
            });

        }
        return singletonInstance;
    }

    public ImageLoader getImageLoader(){
        return singletonImageLoader;
    }

    public RequestQueue getRequestQueue(){
        return singletonRequestQueue;
    }
}
