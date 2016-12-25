package com.bigzun.video.util;

import com.bigzun.video.BuildConfig;

public class Log {
    private static final boolean ENABLE_LOG = BuildConfig.DEBUG;
    private static final String LOG_TAG = BuildConfig.LOG_TAG;

    public static void v(String TAG, String message) {
        if (ENABLE_LOG) {
            android.util.Log.v(TAG, LOG_TAG + message);
        }
    }

    public static void d(String TAG, String message) {
        if (ENABLE_LOG) {
            android.util.Log.d(TAG, LOG_TAG + message);
        }
    }

    public static void i(String TAG, String message) {
        if (ENABLE_LOG) {
            android.util.Log.i(TAG, LOG_TAG + message);
        }
    }

    public static void w(String TAG, String message) {
        if (ENABLE_LOG) {
            android.util.Log.w(TAG, LOG_TAG + message);
        }
    }

    public static void wtf(String TAG, String message) {
        if (ENABLE_LOG) {
            android.util.Log.wtf(TAG, LOG_TAG + message);
        }
    }

    public static void e(String message) {
        if (ENABLE_LOG) {
            android.util.Log.e(LOG_TAG, message);
        }
    }

    public static void e(String TAG, String message) {
        if (ENABLE_LOG) {
            android.util.Log.e(TAG, LOG_TAG + message);
        }
    }

    public static void e(String TAG, Throwable e) {
        if (ENABLE_LOG && e != null) {
            android.util.Log.e(TAG, LOG_TAG + "Throwable: " + e.getMessage(), e);
        }
    }

    public static void e(String TAG, String message, Throwable e) {
        if (ENABLE_LOG) {
            android.util.Log.e(TAG, LOG_TAG + message, e);
        }
    }

}