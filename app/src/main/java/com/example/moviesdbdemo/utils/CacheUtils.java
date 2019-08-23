package com.example.moviesdbdemo.utils;

import android.content.Context;

import java.io.File;

import okhttp3.Cache;

public class CacheUtils {
    private static Cache httpCache;

    public static Cache getHttpCache(Context context){
        if(httpCache == null){
            File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
            int cacheSize = 10*1024*1024; // 10 MB
            httpCache = new Cache(httpCacheDirectory, cacheSize);
        }
        return httpCache;
    }
}
