package com.example.moviesdbdemo.utils;

import android.content.Context;
import android.content.res.Resources;

import com.example.moviesdbdemo.R;

public class StringUtils {

    public static String getStringResourceByName(Context context, String aString) {
        String packageName = context.getPackageName();
        String resString;
        try {
            int resId = context.getResources().getIdentifier(aString, "string", packageName);
            resString = context.getResources().getString(resId);
        } catch (Resources.NotFoundException e){
            resString = "not_found_resource";
        }
        return resString;
    }
}
