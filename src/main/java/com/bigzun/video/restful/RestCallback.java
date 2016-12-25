package com.bigzun.video.restful;

import com.bigzun.video.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 9/27/2016.
 */

public abstract class RestCallback<T> implements Callback<T> {
    private final String TAG = "RestCallback";

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (call != null) {
            Log.d(TAG, "onResponse: call is not null");
        } else {
            Log.d(TAG, "onResponse: call is null.");
        }
        if (response != null) {
            Log.d(TAG, "onResponse code: " + response.code() + " | body: " + response.body());
        } else {
            Log.d(TAG, "onResponse: response is null.");
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        if (call != null) {
            Log.d(TAG, "onFailure: call is not null");
        } else {
            Log.d(TAG, "onFailure: call is null.");
        }
        if (throwable != null) {
            Log.e(TAG, "onFailure: " + throwable.getMessage(), throwable);
        }
    }
}
