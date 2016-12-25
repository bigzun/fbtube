package com.bigzun.video.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bigzun.video.service.PlayMediaService;
import com.bigzun.video.util.Log;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by namnh40 on 11/22/2016.
 */

public class SwipeNotifyReceiver extends BroadcastReceiver {

    private static final String TAG = SwipeNotifyReceiver.class.getSimpleName();

    public SwipeNotifyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive -----------");
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(PlayMediaService.MEDIA_PLAYER_ID);
        context.stopService(new Intent(context, PlayMediaService.class));
    }
}
