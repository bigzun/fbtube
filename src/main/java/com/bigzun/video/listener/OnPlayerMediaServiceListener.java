package com.bigzun.video.listener;

import com.bigzun.video.model.MediaPlaying;

/**
 * Created by namnh40 on 11/14/2016.
 */

public interface OnPlayerMediaServiceListener {

    void onDataLoaded(MediaPlaying list, int position);

    void onDataLoaderFailed();

    void onPlayerPreparingState(int position);

    void onPlayerPreparedState(long duration);

    void onPlayerBufferingState();

    void onPlayerPlayingState();

    void onPlayerPausedState();

    void onProgressBarChanged(long currentPosition, int currentPercentage, int bufferedPercentage);

    void onRepeatStateChanged(int state);

    void onFinishAll();
}
