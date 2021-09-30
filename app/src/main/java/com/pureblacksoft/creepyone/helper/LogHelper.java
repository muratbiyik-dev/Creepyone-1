package com.pureblacksoft.creepyone.helper;

import android.util.Log;

import com.pureblacksoft.creepyone.BuildConfig;

public class LogHelper
{
    public static void LogD(String tag, String msg)
    {
        if (BuildConfig.DEBUG)
        {
            Log.d(tag, msg);
        }
    }
}
