package com.bigzun.video.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.bigzun.video.app.ApplicationController;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Set;

public class SharedPref {
    private final String DEFAULT_NAME = "SharedPreferences";
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private Editor editor;
    private boolean autoCommit = true;

    public SharedPref(Context context) {
        this.autoCommit = true;
        if (context == null)
            this.mContext = ApplicationController.getInstance().getCurrentContext();
        else
            this.mContext = context;
        this.mSharedPreferences = mContext.getSharedPreferences(DEFAULT_NAME, Activity.MODE_PRIVATE);
        this.editor = mSharedPreferences.edit();
    }

    public SharedPref(Context context, boolean autoCommit) {
        if (context == null)
            this.mContext = ApplicationController.getInstance().getCurrentContext();
        else
            this.mContext = context;
        this.autoCommit = autoCommit;
        this.mSharedPreferences = context.getSharedPreferences(DEFAULT_NAME, Activity.MODE_PRIVATE);
        this.editor = mSharedPreferences.edit();
    }

    public SharedPref(Context paramContext, String paramString) {
        if (paramContext == null)
            this.mContext = ApplicationController.getInstance().getCurrentContext();
        else
            this.mContext = paramContext;
        this.autoCommit = true;
        if (!TextUtils.isEmpty(paramString))
            this.mSharedPreferences = this.mContext.getSharedPreferences(paramString, Activity.MODE_PRIVATE);
        else
            this.mSharedPreferences = this.mContext.getSharedPreferences(DEFAULT_NAME, Activity.MODE_PRIVATE);
        this.editor = this.mSharedPreferences.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        if (autoCommit) {
            commit();
        }
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        if (autoCommit) {
            commit();
        }
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public void putLong(String key, long value) {
        editor.putLong(key, value);
        if (autoCommit) {
            commit();
        }
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        editor.putFloat(key, value);
        if (autoCommit) {
            commit();
        }
    }

    public float getFloat(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        if (autoCommit) {
            commit();
        }
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public int putListString(String key, ArrayList<String> list) {
        if (list == null)
            list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            editor.putString("List#String" + i + key, list.get(i));
        if (autoCommit)
            commit();
        return list.size();
    }

    public ArrayList<String> getListString(String key, int sizeList, String defaultValue) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < sizeList; i++)
            list.add(mSharedPreferences.getString("List#String" + i + key, defaultValue));
        return list;
    }

    public int putListLong(String key, ArrayList<Long> list) {
        if (list == null)
            list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
            editor.putLong("List#Long" + i + key, list.get(i));
        if (autoCommit)
            commit();
        return list.size();
    }

    public ArrayList<Long> getListLong(String key, int sizeList, long defaultValue) {
        ArrayList<Long> list = new ArrayList<>();
        for (int i = 0; i < sizeList; i++)
            list.add(mSharedPreferences.getLong("List#Long" + i + key, defaultValue));
        return list;
    }

    public void putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
        if (autoCommit) {
            commit();
        }
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return mSharedPreferences.getStringSet(key, defaultValue);
    }

    public void putObject(String key, Object value) {
        if (value == null)
            return;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(value);
            editor.putString(key, json);
            if (autoCommit) {
                commit();
            }
        } catch (Exception e) {
        }
    }

    // Commit-------------------------------------------------------------//
    public void commit() {
        editor.commit();
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
}
