package com.bigzun.video.activity;

import android.os.Bundle;
import android.os.Handler;

import com.bigzun.video.R;
import com.bigzun.video.helper.DateTimeHelper;
import com.bigzun.video.task.ClearImageCacheTask;
import com.bigzun.video.util.Constants;

public class SplashActivity extends BaseActivity {

    private final int DELAY_SPLASH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boolean hadChooseRegion = mPref.getBoolean(Constants.PREF_CHOOSE_REGION, false);
        if (hadChooseRegion)
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gotoMain();
                }
            }, DELAY_SPLASH);
        else new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoChooseRegion();
                finish();
            }
        }, DELAY_SPLASH);

        clearCacheImage();
    }

    private void clearCacheImage() {
        // TODO clear cache image
        long nowTime = System.currentTimeMillis();
        if (DateTimeHelper.checkDateTime(mPref.getLong(Constants.PREF_CLEAR_CACHE_LASTTIME, nowTime - DateTimeHelper.DAY), nowTime)) {
            new ClearImageCacheTask(this).execute();
        }
    }
}
