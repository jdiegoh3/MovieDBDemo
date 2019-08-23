package com.example.moviesdbdemo;

import android.app.Application;
import android.content.Context;

import com.example.moviesdbdemo.utils.CacheUtils;

public class MoviesApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        // Init HTTP Cache
        context = getApplicationContext();
        CacheUtils.getHttpCache(context);
    }

    public static Context provideContext(){
        return context;
    }
}
