package com.bigzun.video.restful;

import com.bigzun.video.BuildConfig;
import com.bigzun.video.app.ApplicationController;
import com.bigzun.video.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS;
import static okhttp3.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * Created by Admin on 12/20/2016.
 */

public class Injector {
    private static final String TAG = "Injector";
    private static final String CACHE_CONTROL = "Cache-Control";
    private static Map<String, Retrofit> mapRetrofit = new HashMap<>();

    public static Retrofit initRetrofit(String baseUrl) {
        Retrofit retrofit;
        if (mapRetrofit == null)
            mapRetrofit = new HashMap<>();

        retrofit = mapRetrofit.get(baseUrl);
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(provideOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            mapRetrofit.put(baseUrl, retrofit);
        }
        return retrofit;
    }

    private static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(provideHttpLoggingInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor())
                .addNetworkInterceptor(provideCacheInterceptor())
                .cache(provideCache())
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private static Cache provideCache() {
        Cache cache = null;
        try {
            cache = new Cache(new File(ApplicationController.getInstance().getCacheDir(), "http-cache"),
                    10 * 1024 * 1024); // 10 MB
        } catch (SecurityException e) {
            Log.e(TAG, "Could not create Cache!", e);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Cache!", e);
        }
        return cache;
    }

    private static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d(TAG, message);
                    }
                });
        httpLoggingInterceptor.setLevel(BuildConfig.DEBUG ? HEADERS : NONE);
        return httpLoggingInterceptor;
    }

    private static Interceptor provideCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                // re-write response header to force use of cache
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header(CACHE_CONTROL, cacheControl.toString())
                        .build();
            }
        };
    }

    private static Interceptor provideOfflineCacheInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (!ApplicationController.getInstance().isConnected()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }
}
