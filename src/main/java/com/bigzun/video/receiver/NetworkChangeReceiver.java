package com.bigzun.video.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bigzun.video.app.ApplicationController;
import com.bigzun.video.helper.NetworkHelper;

/**
 * Created by namnh40 on 12/21/2016.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ApplicationController.getInstance().setConnected(NetworkHelper.isConnected(context));
        ApplicationController.getInstance().setWifiConnected(NetworkHelper.isConnectedWifi(context));
    }
}
