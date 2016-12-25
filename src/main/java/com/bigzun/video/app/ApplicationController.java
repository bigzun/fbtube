package com.bigzun.video.app;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.bigzun.video.BuildConfig;
import com.bigzun.video.R;
import com.bigzun.video.database.DatabaseHelper;
import com.bigzun.video.helper.NetworkHelper;
import com.bigzun.video.util.Constants;
import com.bigzun.video.util.Log;
import com.bigzun.video.util.SharedPref;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Locale;

public class ApplicationController extends Application {

    public static final String TAG = "ApplicationController";
    private static ApplicationController mInstance;
    private Context currentContext;
    private static DatabaseHelper mDatabaseHelper;
    private static String userAgent;
    private boolean isConnected;
    private boolean isWifiConnected;
    private String apiKey;
    private String languageCode;
    private String regionCode;
    private String youtubeKey;

    private FirebaseRemoteConfig mFirebaseConfig;
    private DatabaseReference mFilebaseDatabase;

    public ApplicationController() {
        mInstance = this;
    }

    public static synchronized ApplicationController getInstance() {
        return mInstance;
    }

    public Context getCurrentContext() {
        if (currentContext == null && mInstance != null)
            currentContext = mInstance.getBaseContext();
        return currentContext;
    }

    public void setCurrentContext(Context currentContext) {
        this.currentContext = currentContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (mInstance == null)
            mInstance = this;
        mDatabaseHelper = DatabaseHelper.getInstance(this);
        userAgent = Util.getUserAgent(this, getPackageName());
        isConnected = NetworkHelper.isConnected(this);
        isWifiConnected = NetworkHelper.isConnectedWifi(this);
        loadConfig();
        loadConfigFromFilebase();
        //loadDatabaseFromFilebase();
    }

    public static DatabaseHelper getDatabaseHelper() {
        return mDatabaseHelper;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultDataSourceFactory(mInstance, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public static HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }

    public boolean isWifiConnected() {
        return isWifiConnected;
    }

    public void setWifiConnected(boolean isWifiConnected) {
        this.isWifiConnected = isWifiConnected;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    public void loadConfigFromFilebase() {
        mFirebaseConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseConfig.setConfigSettings(configSettings);
        mFirebaseConfig.setDefaults(R.xml.remote_config_defaults);
        long cacheExpiration = 3600; // 1 hour in seconds.
        if (mFirebaseConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseConfig.fetch(cacheExpiration).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "getFirebaseConfig onSuccess");
                mFirebaseConfig.activateFetched();
                saveConfigFromFilebase(mFirebaseConfig);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "getFirebaseConfig onFailure", e);
                saveConfigFromFilebase(mFirebaseConfig);
            }
        });
    }

    private void saveConfigFromFilebase(FirebaseRemoteConfig config) {
        if (config == null)
            return;

        String tmpKey;
        SharedPref mPref = new SharedPref(mInstance);

        tmpKey = config.getString(Constants.API_KEY);
        Log.d(TAG, "getFirebaseConfig apiKey: " + tmpKey);
        if (!TextUtils.isEmpty(tmpKey)) {
            setApiKey(tmpKey);
            mPref.putString(Constants.API_KEY, getApiKey());
        }

        tmpKey = config.getString(Constants.YOUTUBE_KEY);
        Log.d(TAG, "getFirebaseConfig youtubeKey: " + tmpKey);
        if (!TextUtils.isEmpty(tmpKey)) {
            setYoutubeKey(tmpKey);
            mPref.putString(Constants.YOUTUBE_KEY, getYoutubeKey());
        }

        tmpKey = config.getString("android_store_revision");
        Log.d(TAG, "getFirebaseConfig android_store_revision: " + tmpKey);

        tmpKey = config.getString("android_store_version");
        Log.d(TAG, "getFirebaseConfig android_store_version: " + tmpKey);
    }

    private void loadConfig() {
        SharedPref mPref = new SharedPref(mInstance);
        Locale locale = Locale.getDefault();
        setApiKey(mPref.getString(Constants.API_KEY, Constants.API_KEY_DEFAULT));
        setYoutubeKey(mPref.getString(Constants.YOUTUBE_KEY, Constants.YOUTUBE_KEY_DEFAULT));
        setLanguageCode(mPref.getString(Constants.PREF_LANGUAGE_CODE, locale.getLanguage()));
        setRegionCode(mPref.getString(Constants.PREF_REGION_CODE, locale.getCountry()));

        Log.d(TAG, "loadConfig: "
                + "apikey: " + getApiKey() + ",\t"
                + "getYoutubeKey: " + getYoutubeKey() + ",\t"
                + "languageCode: " + getLanguageCode() + ",\t"
                + "regionCode: " + getRegionCode() + ",\t"
        );
    }

    private void loadDatabaseFromFilebase() {
        mFilebaseDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference djDB = mFilebaseDatabase.child("dj_channels");
        Log.e(TAG, "key db: " + djDB.getKey());
        FirebaseDatabase mFirebaseDatabase = djDB.getDatabase();
        if (mFirebaseDatabase != null)
            Log.e(TAG, "mFirebaseDatabase: " + mFirebaseDatabase.toString());
        else
            Log.e(TAG, "mFirebaseDatabase null ");
    }

}
