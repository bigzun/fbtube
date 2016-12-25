package com.bigzun.video.task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.bigzun.video.util.Constants;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.SharedPref;
import com.bumptech.glide.Glide;

/**
 * Created by namnh40 on 10/28/2016.
 */

public class ClearImageCacheTask extends AsyncTask<Void, Void, Void> {
    private static final String TAG = ClearImageCacheTask.class.getSimpleName();
    private Context mContext;

    public ClearImageCacheTask(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        try {
            Log.d(TAG, "ClearCacheImage clearMemory");
            Glide.get(mContext).clearMemory();
        } catch (SecurityException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Log.d(TAG, "ClearCacheImage clearDiskCache");
            Glide.get(mContext).clearDiskCache();
        } catch (SecurityException e) {
            Log.e(TAG, e);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        SharedPref mPref = new SharedPref(mContext);
        mPref.putLong(Constants.PREF_CLEAR_CACHE_LASTTIME, System.currentTimeMillis());
    }

    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task) {
        execute(task, (P[]) null);
    }

    @SuppressWarnings("unchecked")
    @SuppressLint("NewApi")
    public static <P, T extends AsyncTask<P, ?, ?>> void execute(T task, P... params) {
        if (task.getStatus() != Status.RUNNING) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
            } else {
                task.execute(params);
            }
        } else {
            Log.e(TAG, "task is running");
        }
    }
}