package com.bigzun.video.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.text.TextUtils;

import com.bigzun.video.service.PlayMediaService;
import com.bigzun.video.util.Log;

public class
NoisyAudioStreamReceiver extends BroadcastReceiver {
    private PlayMediaService mMediaService;

    public NoisyAudioStreamReceiver() {
    }

    public void setMediaService(PlayMediaService mediaService) {
        this.mMediaService = mediaService;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action))
                return;

            switch (action) {
                case AudioManager.ACTION_AUDIO_BECOMING_NOISY:
                    if (mMediaService != null && mMediaService.isPlaying()) {
                        mMediaService.pauseMedia();
                    }

                    Log.e("NoisyAudioStreamReceiver", "stop music");
                    break;
                case AudioManager.ACTION_HDMI_AUDIO_PLUG:
                    Log.e("NoisyAudioStreamReceiver", "ACTION_HDMI_AUDIO_PLUG");
                    break;

                case AudioManager.ACTION_HEADSET_PLUG:
                    Log.e("NoisyAudioStreamReceiver", "ACTION_HEADSET_PLUG");
                    break;

                case AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED:
                    Log.e("NoisyAudioStreamReceiver", "ACTION_SCO_AUDIO_STATE_CHANGED");
                    break;

                case AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED:
                    Log.e("NoisyAudioStreamReceiver", "ACTION_SCO_AUDIO_STATE_UPDATED");
                    break;

                default:
                    break;
            }
        }

    }
}
